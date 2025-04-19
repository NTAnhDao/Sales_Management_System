<?php
require '../../database/db.php';
header("Content-Type: application/json");

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = intval($_POST["id"] ?? 0);
    $conn = connectDB();

    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối DB"]);
        exit;
    }

    $query = $conn->prepare("DELETE FROM danhmucsp WHERE id = ?");
    $query->bind_param("i", $id);

    if ($query->execute()) {
        echo json_encode(["success" => true, "message" => "Xóa thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Không thể xóa"]);
    }

    $query->close();
    $conn->close();
}
?>