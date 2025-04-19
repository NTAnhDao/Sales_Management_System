<?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để thêm danh mục!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $name = $_POST["name"] ?? '';
    $description = $_POST["description"] ?? '';
    $date = $_POST["date"] ?? date('Y-m-d');
    $id_nguoitao = $_SESSION['id']; // Lấy ID người dùng từ session

    if (empty($name)) {
        echo json_encode(["success" => false, "message" => "Tên danh mục không được để trống!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem tên danh mục đã tồn tại chưa
    $query_check = $conn->prepare("SELECT tendm FROM danhmucsp WHERE tendm = ?");
    $query_check->bind_param("s", $name);
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

    // Thêm danh mục mới
    $query = $conn->prepare("INSERT INTO danhmucsp (tendm, mota, ngaytao, id_nguoitao) VALUES (?, ?, ?, ?)");
    $query->bind_param("sssi", $name, $description, $date, $id_nguoitao);

    if ($query->execute()) {
        $new_id = $conn->insert_id;
        echo json_encode([
            "success" => true,
            "message" => "Thêm danh mục thành công!",
            "id" => $new_id,
            "name" => $name,
            "description" => $description,
            "date" => $date,
            "ten_nguoitao" => $ten_nguoitao
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