<?php 
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = intval($_POST["id"] ?? 0);
    $tendm = $_POST["tendm"] ?? '';
    $mota = $_POST["mota"] ?? '';
    $ngaytao = $_POST["ngaytao"] ?? '';
    $id_nguoitao = intval($_POST["id_nguoitao"] ?? 0);

    $conn = connectDB();

    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    $query = $conn->prepare("UPDATE `danhmucsp` SET `tendm` = ?, `mota` = ?, `ngaytao` = ?, `id_nguoitao` = ? WHERE `id` = ?");
    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("sssii", $tendm, $mota, $ngaytao, $id_nguoitao, $id); 

    if ($query->execute()) {
        echo json_encode(["success" => true, "message" => "Cập nhật danh mục thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Cập nhật thất bại!"]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>
