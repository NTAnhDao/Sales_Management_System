<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
    exit;
}

$mavach = trim($_POST["mavach"] ?? '');
$tensp = trim($_POST["tensp"] ?? '');
$giagoc = floatval($_POST["giagoc"] ?? 0);
$giabanle = floatval($_POST["giabanle"] ?? 0);
$id_danhmuc = intval($_POST["id_danhmuc"] ?? 0);

// Kiểm tra dữ liệu đầu vào
if (empty($mavach) || empty($tensp) || $giagoc <= 0 || $giabanle <= 0 || $id_danhmuc <= 0) {
    echo json_encode(["success" => false, "message" => "Vui lòng nhập đầy đủ thông tin sản phẩm và mã vạch hợp lệ!"]);
    exit;
}

$conn = connectDB();
if (!$conn) {
    echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
    exit;
}

// Kiểm tra mã vạch trùng
$stmt = $conn->prepare("SELECT mavach FROM sanpham WHERE mavach = ?");
$stmt->bind_param("s", $mavach);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Mã vạch đã tồn tại, vui lòng nhập mã khác!"]);
    $stmt->close();
    $conn->close();
    exit;
}
$stmt->close();

// Xử lý upload hình ảnh
$img = '';
if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] === UPLOAD_ERR_OK) {
    $fileTmpPath = $_FILES['file-upload']['tmp_name'];
    $fileName = basename($_FILES['file-upload']['name']);
    $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
    $allowedExts = ['jpg', 'jpeg', 'png', 'gif', 'webp'];

    if (!in_array($fileExt, $allowedExts)) {
        echo json_encode(["success" => false, "message" => "Định dạng ảnh không hợp lệ!"]);
        $conn->close();
        exit;
    }

    $newFileName = uniqid('sp_', true) . '.' . $fileExt;
    $uploadDir = '../images/';
    if (!is_dir($uploadDir)) {
        mkdir($uploadDir, 0777, true);
    }
    $destPath = $uploadDir . $newFileName;

    if (move_uploaded_file($fileTmpPath, $destPath)) {
        $img = $newFileName;
    } else {
        echo json_encode(["success" => false, "message" => "Không thể lưu file hình ảnh!"]);
        $conn->close();
        exit;
    }
} else {
    echo json_encode(["success" => false, "message" => "Vui lòng chọn hình ảnh cho sản phẩm!"]);
    $conn->close();
    exit;
}

// Thêm sản phẩm
$query = $conn->prepare("INSERT INTO sanpham (mavach, tensp, giagoc, giabanle, img, id_danhmuc, ngaytao) 
                         VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP())");
if (!$query) {
    echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
    $conn->close();
    exit;
}

$query->bind_param("ssddsi", $mavach, $tensp, $giagoc, $giabanle, $img, $id_danhmuc);
$query->execute();

if ($query->affected_rows > 0) {
    echo json_encode([
        "success" => true,
        "message" => "Thêm sản phẩm thành công!",
        "mavach" => $mavach,
        "img" => $img
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Không thể thêm sản phẩm: " . $query->error]);
}

$query->close();
$conn->close();
?>
