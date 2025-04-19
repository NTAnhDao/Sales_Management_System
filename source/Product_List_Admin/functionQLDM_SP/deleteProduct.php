<?php
session_start();
require '../database/db.php';

header('Content-Type: application/json');

if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'Admin') {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền thực hiện thao tác này!']);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['mavach'])) {
    $mavach = $_POST['mavach'] ?? '';

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(['success' => false, 'message' => 'Lỗi kết nối cơ sở dữ liệu!']);
        exit;
    }

    // Kiểm tra sản phẩm có trong đơn hàng không
    $checkOrderQuery = $conn->prepare("SELECT * FROM chitietdonhang WHERE id_sanpham = ?");
    $checkOrderQuery->bind_param("s", $mavach);
    $checkOrderQuery->execute();
    $checkOrderResult = $checkOrderQuery->get_result();
    if ($checkOrderResult->num_rows > 0) {
        echo json_encode(['success' => false, 'message' => 'Sản phẩm đã tồn tại trong đơn hàng, không thể xóa!']);
        $checkOrderQuery->close();
        $conn->close();
        exit;
    }
    $checkOrderQuery->close();

    // Lấy hình ảnh để xóa file
    $imgQuery = $conn->prepare("SELECT img FROM sanpham WHERE mavach = ?");
    $imgQuery->bind_param("s", $mavach);
    $imgQuery->execute();
    $imgResult = $imgQuery->get_result();
    $img = $imgResult->fetch_assoc()['img'];
    $imgQuery->close();

    // Xóa sản phẩm
    $query = $conn->prepare("DELETE FROM sanpham WHERE mavach = ?");
    $query->bind_param("s", $mavach);
    $query->execute();

    if ($query->affected_rows > 0) {
        // Xóa file hình ảnh nếu có
        if ($img && file_exists("../images/$img")) {
            unlink("../images/$img");
        }
        echo json_encode(['success' => true, 'message' => 'Xóa sản phẩm thành công!']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Xóa sản phẩm thất bại: ' . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(['success' => false, 'message' => 'Yêu cầu không hợp lệ!']);
}
?>