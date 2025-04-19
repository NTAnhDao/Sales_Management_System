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

    // Kiểm tra xem danh mục có tồn tại và người dùng có quyền chỉnh sửa không
    $query_check_access = $conn->prepare("SELECT * FROM danhmucsp WHERE id = ? AND id_nguoitao = ?");
    $query_check_access->bind_param("ii", $id, $id_nguoitao);
    $query_check_access->execute();
    $result_access = $query_check_access->get_result();
    if ($result_access->num_rows == 0) {
        echo json_encode(["success" => false, "message" => "Bạn không có quyền chỉnh sửa danh mục này!"]);
        $query_check_access->close();
        $conn->close();
        exit;
    }
    $query_check_access->close();

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

    // Lấy username của người tạo
    $query_user = $conn->prepare("SELECT username FROM taikhoan WHERE id = ?");
    $query_user->bind_param("i", $id_nguoitao);
    $query_user->execute();
    $result_user = $query_user->get_result();
    $ten_nguoitao = $result_user->num_rows > 0 ? $result_user->fetch_assoc()['username'] : 'Không xác định';
    $query_user->close();

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
            "ten_nguoitao" => $ten_nguoitao
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