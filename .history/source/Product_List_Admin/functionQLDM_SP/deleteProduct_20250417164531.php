<?php
header("Content-Type: application/json");
require '../../database/db.php';

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
        if ($img && file_exists("imagess/" . $img)) {
            unlink("image/" . $img);
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