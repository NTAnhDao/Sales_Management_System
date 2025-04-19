<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach_old = $_POST["mavach_old"] ?? ''; // Mã vạch cũ để kiểm tra
    $mavach = $_POST["mavach"] ?? ''; // Mã vạch mới
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = floatval($_POST["giagoc"] ?? 0);
    $giabanle = floatval($_POST["giabanle"] ?? 0);
    $img = $_POST["img"] ?? null;
    $id_danhmuc = intval($_POST["id_danhmuc"] ?? 0);

    // Kiểm tra dữ liệu đầu vào
    if (empty($mavach_old) || empty($mavach) || empty($tensp) || $giagoc <= 0 || $giabanle <= 0 || $id_danhmuc <= 0) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập đầy đủ thông tin sản phẩm, bao gồm mã vạch!"]);
        exit;
    }

    $conn = connectDB();

    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Kiểm tra xem mã vạch mới có bị trùng với mã vạch khác không (ngoại trừ chính sản phẩm đang chỉnh sửa)
    if ($mavach !== $mavach_old) {
        $query_check = $conn->prepare("SELECT mavach FROM sanpham WHERE mavach = ? AND mavach != ?");
        $query_check->bind_param("ss", $mavach, $mavach_old);
        $query_check->execute();
        $result_check = $query_check->get_result();
        if ($result_check->num_rows > 0) {
            echo json_encode(["success" => false, "message" => "Mã vạch mới đã tồn tại, vui lòng chọn mã vạch khác!"]);
            $query_check->close();
            $conn->close();
            exit;
        }
        $query_check->close();
    }

    $query = $conn->prepare("UPDATE sanpham SET mavach = ?, tensp = ?, giagoc = ?, giabanle = ?, img = ?, id_danhmuc = ? WHERE mavach = ?");
    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssddsii", $mavach, $tensp, $giagoc, $giabanle, $img, $id_danhmuc, $mavach_old);

    if ($query->execute()) {
        echo json_encode([
            "success" => true,
            "message" => "Cập nhật sản phẩm thành công!",
            "mavach" => $mavach // Trả về mã vạch mới để client cập nhật
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Cập nhật thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>