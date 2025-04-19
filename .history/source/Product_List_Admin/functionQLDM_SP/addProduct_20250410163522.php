<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach = $_POST["mavach"] ?? '';
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = floatval($_POST["giagoc"] ?? 0);
    $giabanle = floatval($_POST["giabanle"] ?? 0);
    $img = $_POST["img"] ?? null; // Đường dẫn ảnh hoặc tên file
    $id_danhmuc = intval($_POST["id_danhmuc"] ?? 0);

    // Kiểm tra dữ liệu đầu vào
    if (empty($mavach) || empty($tensp) || $giagoc <= 0 || $giabanle <= 0 || $id_danhmuc <= 0) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập đầy đủ thông tin sản phẩm, bao gồm mã vạch!"]);
        exit;
    }

    $conn = connectDB();

    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem mã vạch đã tồn tại chưa
    $query_check = $conn->prepare("SELECT mavach FROM sanpham WHERE mavach = ?");
    $query_check->bind_param("s", $mavach);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Mã vạch đã tồn tại, vui lòng chọn mã vạch khác!"]);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    $query = $conn->prepare("INSERT INTO sanpham (mavach, tensp, giagoc, giabanle, img, id_danhmuc, ngaytao) 
                            VALUES (?, ?, ?, ?, ?, ?, current_timestamp())");

    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssddsi", $mavach, $tensp, $giagoc, $giabanle, $img, $id_danhmuc);
    $query->execute();

    if ($query->affected_rows > 0) {
        echo json_encode([
            "success" => true,
            "message" => "Thêm sản phẩm thành công!",
            "mavach" => $mavach // Trả về mã vạch để client sử dụng
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Thêm sản phẩm thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>