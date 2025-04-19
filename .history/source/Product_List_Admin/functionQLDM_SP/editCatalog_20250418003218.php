<?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để chỉnh sửa danh mục!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = $_POST["id"] ?? '';
    $name = $_POST["name"] ?? '';
    $description = $_POST["description"] ?? '';
    $date = $_POST["date"] ?? date('Y-m-d');
    $id_nguoitao = $_SESSION['id']; // Lấy ID người dùng từ session

    if (empty($id) || empty($name)) {
        echo json_encode(["success" => false, "message" => "Tên danh mục và ID không được để trống!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem tên danh mục đã tồn tại chưa (trừ danh mục hiện tại)
    $query_check = $conn->prepare("SELECT tendm FROM danhmucsp WHERE tendm = ? AND id != ?");
    $query_check->bind_param("si", $name, $id);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Tên danh mục đã tồn tại!"]);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    // Cập nhật danh mục
    $query = $conn->prepare("UPDATE danhmucsp SET tendm = ?, mota = ?, ngaytao = ?, id_nguoitao = ? WHERE id = ?");
    $query->bind_param("sssii", $name, $description, $date, $id_nguoitao, $id);

    if ($query->execute()) {
        echo json_encode([
            "success" => true,
            "message" => "Cập nhật danh mục thành công!",
            "id" => $id,
            "name" => $name,
            "description" => $description,
            "date" => $date,
            "id_nguoitao" => $id_nguoitao
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Cập nhật danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>