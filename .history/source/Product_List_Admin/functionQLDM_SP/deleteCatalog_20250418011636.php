<?php
session_start();
include 'connection.php'; // Kết nối database

if (!isset($_SESSION['user_id'])) {
    echo json_encode(['success' => false, 'message' => 'Bạn cần đăng nhập để thực hiện thao tác này!']);
    exit;
}

$id = isset($_POST['id']) ? $_POST['id'] : null;
if (!$id) {
    echo json_encode(['success' => false, 'message' => 'ID danh mục không hợp lệ!']);
    exit;
}

// Kiểm tra quyền của người dùng
$id_nhan_vien = $_SESSION['id_nhanvien']; // Lấy id_nhân_viên từ session
$sql_check = "SELECT * FROM taikhoan WHERE id = ? AND id_nhan_vien = ?";
$stmt_check = $conn->prepare($sql_check);
if (!$stmt_check) {
    echo json_encode(['success' => false, 'message' => 'Lỗi truy vấn cơ sở dữ liệu!']);
    exit;
}
$stmt_check->bind_param("is", $id, $id_nhan_vien);
$stmt_check->execute();
$result = $stmt_check->get_result();

if ($result->num_rows == 0) {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền xóa danh mục này hoặc danh mục không tồn tại!']);
    exit;
}

// Xóa danh mục
$sql_delete = "DELETE FROM category WHERE id = ?";
$stmt_delete = $conn->prepare($sql_delete);
$stmt_delete->bind_param("i", $id);
if ($stmt_delete->execute()) {
    echo json_encode(['success' => true, 'message' => 'Xóa danh mục thành công!']);
} else {
    echo json_encode(['success' => false, 'message' => 'Lỗi khi xóa danh mục!']);
}

$stmt_check->close();
$stmt_delete->close();
$conn->close();
?>