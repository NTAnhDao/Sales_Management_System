<?php
header("Content-Type: application/json");
require '../database/db.php'; // Điều chỉnh đường dẫn đến file kết nối cơ sở dữ liệu

$weekInput = $_GET['week'] ?? date('Y-\WW'); // Định dạng: YYYY-Www
$weekParts = explode('-W', $weekInput);
$year = $weekParts[0];
$week = $weekParts[1];

$conn = connectDB();

if (!$conn) {
    echo json_encode(["error" => "Lỗi kết nối cơ sở dữ liệu"]);
    exit;
}

// Tính ngày bắt đầu (thứ Hai) và ngày kết thúc (Chủ Nhật) của tuần
$firstDayOfYear = new DateTime("$year-01-01");
$firstDayOfYear->modify('first day of January +' . ($week - 1) . ' weeks');
$firstDayOfYear->modify('monday this week');
$startDate = $firstDayOfYear->format('Y-m-d');
$endDate = $firstDayOfYear->modify('sunday this week')->format('Y-m-d');

// Tính ngày bắt đầu và kết thúc của tuần trước để tính tỷ lệ tăng giảm
$prevWeekStart = (new DateTime($startDate))->modify('-7 days')->format('Y-m-d');
$prevWeekEnd = (new DateTime($endDate))->modify('-7 days')->format('Y-m-d');

// Lấy tổng số lượng sản phẩm
$query = "SELECT COUNT(*) as total FROM sanpham";
$result = $conn->query($query);
$totalProducts = $result->fetch_assoc()['total'];

// Lấy tổng số danh mục
$query = "SELECT COUNT(*) as total FROM danhmucsp";
$result = $conn->query($query);
$totalCategories = $result->fetch_assoc()['total'];

// Lấy tổng số nhân viên (giả sử có bảng nhanvien)
$query = "SELECT COUNT(*) as total FROM nhanvien";
$result = $conn->query($query);
$totalEmployees = $result->fetch_assoc()['total'];

// Lấy dữ liệu doanh thu và số lượng bán ra (7 ngày trong tuần)
$revenueData = [];
$salesData = [];
$currentDate = new DateTime($startDate);
for ($i = 0; $i < 7; $i++) {
    $dateStr = $currentDate->format('Y-m-d');
    // Doanh thu (giả sử có bảng donhang)
    $query = "SELECT SUM(tongtien) as revenue FROM donhang WHERE DATE(ngaydat) = '$dateStr'";
    $result = $conn->query($query);
    $revenueData[] = $result->fetch_assoc()['revenue'] ?? 0;

    // Số lượng sản phẩm bán ra (giả sử có bảng chitietdonhang)
    $query = "SELECT SUM(soluong) as sales FROM chitietdonhang 
              JOIN donhang ON chitietdonhang.madon = donhang.madon 
              WHERE DATE(donhang.ngaydat) = '$dateStr'";
    $result = $conn->query($query);
    $salesData[] = $result->fetch_assoc()['sales'] ?? 0;

    $currentDate->modify('+1 day');
}

// Tính tỷ lệ tăng giảm so với tuần trước (giả sử tính trên doanh thu)
$query = "SELECT SUM(tongtien) as revenue FROM donhang WHERE DATE(ngaydat) BETWEEN '$prevWeekStart' AND '$prevWeekEnd'";
$result = $conn->query($query);
$prevRevenue = $result->fetch_assoc()['revenue'] ?? 0;
$currentRevenue = array_sum($revenueData);
$productsChange = $prevRevenue > 0 ? round((($currentRevenue - $prevRevenue) / $prevRevenue) * 100, 1) : 0;

// Giả lập tỷ lệ cho danh mục và nhân viên (trong thực tế cần tính toán tương tự)
$categoriesChange = 2.1; // Thay bằng logic thực tế
$employeesChange = 1.8; // Thay bằng logic thực tế

echo json_encode([
    "totalProducts" => $totalProducts,
    "totalCategories" => $totalCategories,
    "totalEmployees" => $totalEmployees,
    "productsChange" => $productsChange,
    "categoriesChange" => $categoriesChange,
    "employeesChange" => $employeesChange,
    "revenueData" => $revenueData,
    "salesData" => $salesData
]);

$conn->close();
?>