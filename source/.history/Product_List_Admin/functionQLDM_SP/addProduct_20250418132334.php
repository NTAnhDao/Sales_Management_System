<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach = $_POST["mavach"] ?? '';
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = floatval($_POST["giagoc"] ?? 0);
    $giabanle = floatval($_POST["giabanle"] ?? 0);
    $id_danhmuc = intval($_POST["id_danhmuc"] ?? 0);

    if (empty($mavach) || empty($tensp) || $giagoc <= 0 || $giabanle <= 0 || $id_danhmuc <= 0) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập đầy đủ thông tin sản phẩm!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    $query_check = $conn->prepare("SELECT mavach FROM sanpham WHERE mavach = ?");
    $query_check->bind_param("s", $mavach);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Mã vạch đã tồn tại!"]);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    $img = '';
    if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['file-upload']['tmp_name'];
        $fileName = $_FILES['file-upload']['name'];
        $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        $allowedExt = ['jpg', 'jpeg', 'png', 'gif'];
        if (!in_array($fileExt, $allowedExt)) {
            echo json_encode(["success" => false, "message" => "Định dạng hình ảnh không hợp lệ!"]);
            $conn->close();
            exit;
        }
        if ($_FILES['file-upload']['size'] > 5 * 1024 * 1024) {
            echo json_encode(["success" => false, "message" => "Hình ảnh quá lớn! Tối đa 5MB."]);
            $conn->close();
            exit;
        }

        $uploadDir = '../images/';
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0755, true);
        }
        if (!is_writable($uploadDir)) {
            echo json_encode(["success" => false, "message" => "Thư mục images không có quyền ghi!"]);
            $conn->close();
            exit;
        }

        $newFileName = uniqid() . '.' . $fileExt;
        $destPath = $uploadDir . $newFileName;

        if (move_uploaded_file($fileTmpPath, $destPath)) {
            $img = $newFileName;
        } else {
            echo json_encode(["success" => false, "message" => "Lỗi khi lưu file hình ảnh!"]);
            $conn->close();
            exit;
        }
    } else {
        echo json_encode(["success" => false, "message" => "Vui lòng chọn hình ảnh!"]);
        $conn->close();
        exit;
    }

    $query = $conn->prepare("INSERT INTO sanpham (mavach, tensp, giagoc, giabanle, img, id_danhmuc, ngaytao) VALUES (?, ?, ?, ?, ?, ?, current_timestamp())");
    $query->bind_param("ssddsi", $mavach, $tensp, $giagoc, $giabanle, $img, $id_danhmuc);
    if ($query->execute()) {
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