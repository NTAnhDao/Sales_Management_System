<?php
session_start();
require '../../database/db.php';
require 'permission.php'; // Đường dẫn tới file permission.php trong cùng thư mục

header('Content-Type: application/json');

// Kiểm tra quyền xóa danh mục
if (!canManageCategories()) {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền xóa danh mục sản phẩm!']);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['id'])) {
    $id = intval($_POST['id']);

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(['success' => false, 'message' => 'Lỗi kết nối cơ sở dữ liệu!']);
        exit;
    }

    // Kiểm tra danh mục có sản phẩm không
    $checkProductQuery = $conn->prepare("SELECT * FROM sanpham WHERE id_danhmuc = ?");
    $checkProductQuery->bind_param("i", $id);
    $checkProductQuery->execute();
    $checkProductResult = $checkProductQuery->get_result();
    if ($checkProductResult->num_rows > 0) {
        echo json_encode(['success' => false, 'message' => 'Danh mục đang có sản phẩm, không thể xóa!']);
        $checkProductQuery->close();
        $conn->close();
        exit;
    }
    $checkProductQuery->close();

    // Xóa danh mục
    $query = $conn->prepare("DELETE FROM danhmucsp WHERE id = ?");
    $query->bind_param("i", $id);
    $query->execute();

    if ($query->affected_rows > 0) {
        echo json_encode(['success' => true, 'message' => 'Xóa danh mục thành công!']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Xóa danh mục thất bại: ' . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(['success' => false, 'message' => 'Yêu cầu không hợp lệ!']);
}
?>