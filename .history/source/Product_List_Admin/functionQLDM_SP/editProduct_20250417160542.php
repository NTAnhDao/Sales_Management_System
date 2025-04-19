<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach_old = $_POST["mavach_old"] ?? '';
    $mavach = $_POST["mavach"] ?? '';
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = floatval($_POST["giagoc"] ?? 0);
    $giabanle = floatval($_POST["giabanle"] ?? 0);
    $id_danhmuc = intval($_POST["id_danhmuc"] ?? 0);
    $ngaytao = $_POST["ngaytao"] ?? date('Y-m-d');

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

    // Kiểm tra xem mã vạch mới có bị trùng không
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

    // Lấy hình ảnh cũ
    $oldImgQuery = $conn->prepare("SELECT img FROM sanpham WHERE mavach = ?");
    $oldImgQuery->bind_param("s", $mavach_old);
    $oldImgQuery->execute();
    $oldImgQuery->bind_result($oldImg);
    $oldImgQuery->fetch();
    $oldImgQuery->close();

    // Xử lý upload file mới (nếu có)
    $img = $oldImg;
    if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['file-upload']['tmp_name'];
        $fileName = $_FILES['file-upload']['name'];
        $fileExt = pathinfo($fileName, PATHINFO_EXTENSION);
        $newFileName = uniqid() . '.' . $fileExt;
        $uploadDir = 'image/';
        
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0777, true);
        }

        $destPath = $uploadDir . $newFileName;

        if (move_uploaded_file($fileTmpPath, $destPath)) {
            $img = $newFileName;
            // Xóa file cũ nếu tồn tại
            if ($oldImg && file_exists($uploadDir . $oldImg)) {
                unlink($uploadDir . $oldImg);
            }
        }
    }

    // Cập nhật sản phẩm
    $query = $conn->prepare("UPDATE sanpham SET mavach = ?, tensp = ?, giagoc = ?, giabanle = ?, img = ?, id_danhmuc = ?, ngaytao = ? WHERE mavach = ?");
    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssddsisss", $mavach, $tensp, $giagoc, $giabanle, $img, $id_danhmuc, $ngaytao, $mavach_old);

    if ($query->execute()) {
        echo json_encode([
            "success" => true,
            "message" => "Cập nhật sản phẩm thành công!",
            "mavach" => $mavach,
            "img" => $img
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