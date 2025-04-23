<?php
session_start();
require '../../database/db.php';
require 'permission.php'; // Đường dẫn tới file permission.php trong cùng thư mục

header('Content-Type: application/json');

// Kiểm tra quyền xóa sản phẩm
if (!canManageProducts()) {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền xóa sản phẩm!']);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach = $_POST["mavach"] ?? '';

    if (empty($mavach)) {
        echo json_encode(["success" => false, "message" => "Mã vạch sản phẩm không hợp lệ!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra nếu sản phẩm nằm trong đơn hàng
    $checkSql = "SELECT COUNT(*) as count FROM chitietdonhang WHERE id_sanpham = ?";
    $stmt = $conn->prepare($checkSql);
    $stmt->bind_param("s", $mavach);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();

    if ($row['count'] > 0) {
        echo json_encode([
            'success' => false,
            'message' => 'Không thể xóa sản phẩm vì sản phẩm đã được mua trong chi tiết đơn hàng!'
        ]);
        $stmt->close();
        $conn->close();
        exit();
    }


    
    // Lấy hình ảnh để xóa file
    $imgQuery = $conn->prepare("SELECT img FROM sanpham WHERE mavach = ?");
    $imgQuery->bind_param("s", $mavach);
    $imgQuery->execute();
    $imgQuery->bind_result($img);
    $imgQuery->fetch();
    $imgQuery->close();

    // Xóa sản phẩm
    $query = $conn->prepare("DELETE FROM sanpham WHERE mavach = ?");
    $query->bind_param("s", $mavach);

    if ($query->execute()) {
        // Xóa file hình ảnh nếu tồn tại
        $imagePath = __DIR__ . '/images/'; // Thư mục images nằm cùng cấp với file PHP

        if ($img && file_exists($imagePath . $img)) {
            unlink($imagePath . $img);
        }
        echo json_encode(["success" => true, "message" => "Xóa sản phẩm thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Xóa sản phẩm thất bại: " . $query->error]);
    }


    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>