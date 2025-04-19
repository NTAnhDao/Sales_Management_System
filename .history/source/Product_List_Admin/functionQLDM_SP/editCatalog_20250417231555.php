<?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Kiểm tra xem admin đã đăng nhập chưa
    if (!isset($_SESSION['admin_id'])) {
        echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để chỉnh sửa danh mục!"]);
        exit;
    }

    $id = intval($_POST["id"] ?? 0);
    $tendm = $_POST["tendm"] ?? '';
    $mota = $_POST["mota"] ?? '';
    $ngaytao = $_POST["ngaytao"] ?? '';
    $id_nguoitao = intval($_SESSION['admin_id']); // Lấy ID admin từ session

    // Kiểm tra dữ liệu đầu vào
    if (empty($id) || empty($tendm)) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập đầy đủ thông tin!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem danh mục có tồn tại không
    $query_check = $conn->prepare("SELECT id FROM danhmucsp WHERE id = ?");
    $query_check->bind_param("i", $id);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows === 0) {
        echo json_encode(["success" => false, "message" => "Danh mục không tồn tại!"]);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    // Cập nhật danh mục
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
        echo json_encode(["success" => false, "message" => "Cập nhật thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>