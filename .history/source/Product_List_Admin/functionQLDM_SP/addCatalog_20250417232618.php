<?php 
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $tendm = $_POST["tendm"] ?? '';
    $mota = $_POST["mota"] ?? '';
    $id_nguoitao = intval($_POST["id_nguoitao"] ?? 0);

    $conn = connectDB();

    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    $query = $conn->prepare("INSERT INTO danhmucsp (id, tendm, mota, ngaytao, id_nguoitao) 
                        VALUES (NULL, ?, ?, current_timestamp(), ?)");

    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssi", $tendm, $mota, $id_nguoitao);
    $query->execute();

    if ($query->affected_rows > 0) {
        $new_id = $conn->insert_id; // Lấy ID của danh mục vừa thêm
        echo json_encode([
            "success" => true,
            "message" => "Thêm danh mục thành công!",
            "id" => $new_id // Trả về ID
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Thêm danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>