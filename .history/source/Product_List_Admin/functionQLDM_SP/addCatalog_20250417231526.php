<?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Kiểm tra xem admin đã đăng nhập chưa
    if (!isset($_SESSION['admin_id'])) {
        echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để thêm danh mục!"]);
        exit;
    }

    $tendm = $_POST["tendm"] ?? '';
    $mota = $_POST["mota"] ?? '';
    $id_nguoitao = intval($_SESSION['admin_id']); // Lấy ID admin từ session

    // Kiểm tra dữ liệu đầu vào
    if (empty($tendm)) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập tên danh mục!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem danh mục đã tồn tại chưa
    $query_check = $conn->prepare("SELECT tendm FROM danhmucsp WHERE tendm = ?");
    $query_check->bind_param("s", $tendm);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Danh mục đã tồn tại!"]);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    // Thêm danh mục vào cơ sở dữ liệu
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