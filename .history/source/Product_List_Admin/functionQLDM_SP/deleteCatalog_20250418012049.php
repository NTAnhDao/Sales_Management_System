<?php
session_start();
include 'connection.php'; // Kết nối database

if (!isset($_SESSION['id'])) {
    echo json_encode(['success' => false, 'message' => 'Bạn cần đăng nhập để thực hiện thao tác này!']);
    exit;
}

$id = isset($_POST['id']) ? $_POST['id'] : null;
if (!$id) {
    echo json_encode(['success' => false, 'message' => 'ID danh mục không hợp lệ!']);
    exit;
}

// Kiểm tra quyền của người dùng
$id_nguoitao = $_SESSION['id']; // Lấy id_nguoitao từ session (taikhoan.id)
$sql_check = "SELECT * FROM danhmucsp WHERE id = ? AND id_nguoitao = ?";
$stmt_check = $conn->prepare($sql_check);
if (!$stmt_check) {
    echo json_encode(['success' => false, 'message' => 'Lỗi truy vấn cơ sở dữ liệu!']);
    exit;
}
$stmt_check->bind_param("ii", $id, $id_nguoitao);
$stmt_check->execute();
$result = $stmt_check->get_result();

if ($result->num_rows == 0) {
    // Nếu không tìm thấy danh mục, kiểm tra xem danh mục có tồn tại không
    $sql_fallback = "SELECT * FROM danhmucsp WHERE id = ?";
    $stmt_fallback = $conn->prepare($sql_fallback);
    $stmt_fallback->bind_param("i", $id);
    $stmt_fallback->execute();
    $result_fallback = $stmt_fallback->get_result();
    if ($result_fallback->num_rows == 0) {
        echo json_encode(['success' => false, 'message' => 'Danh mục không tồn tại!']);
        exit;
    } else {
        echo json_encode(['success' => false, 'message' => 'Bạn không có quyền xóa danh mục này!']);
        exit;
    }
}

// Xóa danh mục
$sql_delete = "DELETE FROM danhmucsp WHERE id = ?";
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