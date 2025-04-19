<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach = $_POST["mavach"] ?? '';
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = floatval($_POST["giagoc"] ?? 0);
    $giabanle = floatval($_POST["giabanle"] ?? 0);
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

    // Xử lý upload file hình ảnh
    $img = '';
    if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['file-upload']['tmp_name'];
        $fileName = $_FILES['file-upload']['name'];
        $fileExt = pathinfo($fileName, PATHINFO_EXTENSION);
        $newFileName = uniqid() . '.' . $fileExt; // Tạo tên file duy nhất
        $uploadDir = '../images/'; // Đúng thư mục image/

        // Đảm bảo thư mục tồn tại
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0777, true);
        }

        $destPath = $uploadDir . $newFileName;

        if (move_uploaded_file($fileTmpPath, $destPath)) {
            $img = $newFileName;
        } else {
            echo json_encode(["success" => false, "message" => "Lỗi khi lưu file hình ảnh!"]);
            $conn->close();
            exit;
        }
    } else {
        echo json_encode(["success" => false, "message" => "Vui lòng chọn hình ảnh cho sản phẩm!"]);
        $conn->close();
        exit;
    }

    // Thêm sản phẩm vào cơ sở dữ liệu
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
            "mavach" => $mavach,
            "img" => $img
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