<?php
header("Content-Type: application/json");
require '../../database/db.php';


if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $mavach = $_POST["mavach"] ?? '';
    $tensp = $_POST["tensp"] ?? '';
    $giagoc = $_POST["giagoc"] ?? 0;
    $giabanle = $_POST["giabanle"] ?? 0;
    $id_danhmuc = $_POST["id_danhmuc"] ?? 0;
    $ngaytao = date('Y-m-d H:i:s');

    if (empty($mavach) || empty($tensp) || empty($giagoc) || empty($giabanle) || empty($id_danhmuc)) {
        echo json_encode(["success" => false, "message" => "Vui lòng điền đầy đủ thông tin sản phẩm!"]);
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

    $img_name = null;
    if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] == UPLOAD_ERR_OK) {
        $img_tmp = $_FILES['file-upload']['tmp_name'];
        $img_name = $_FILES['file-upload']['name'];
        $img_ext = strtolower(pathinfo($img_name, PATHINFO_EXTENSION));
        $allowed_ext = ['jpg', 'jpeg', 'png', 'gif'];

        if (!in_array($img_ext, $allowed_ext)) {
            echo json_encode(["success" => false, "message" => "Định dạng hình ảnh không hợp lệ!"]);
            $conn->close();
            exit;
        }

        $new_img_name = uniqid() . '.' . $img_ext;
        $upload_path = '../images/' . $new_img_name;
        if (!move_uploaded_file($img_tmp, $upload_path)) {
            echo json_encode(["success" => false, "message" => "Lỗi khi tải lên hình ảnh!"]);
            $conn->close();
            exit;
        }
        $img_name = $new_img_name;
    }

    $query_danhmuc = $conn->prepare("SELECT tendm FROM danhmucsp WHERE id = ?");
    $query_danhmuc->bind_param("i", $id_danhmuc);
    $query_danhmuc->execute();
    $result_danhmuc = $query_danhmuc->get_result();
    $tendm = $result_danhmuc->num_rows > 0 ? $result_danhmuc->fetch_assoc()['tendm'] : 'Không xác định';
    $query_danhmuc->close();

    $query = $conn->prepare("INSERT INTO sanpham (mavach, tensp, giagoc, giabanle, img, id_danhmuc, ngaytao) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $query->bind_param("ssddsis", $mavach, $tensp, $giagoc, $giabanle, $img_name, $id_danhmuc, $ngaytao);

    if ($query->execute()) {
        echo json_encode([
            "success" => true,
            "message" => "Thêm sản phẩm thành công!",
            "mavach" => $mavach,
            "tensp" => $tensp,
            "giagoc" => $giagoc,
            "giabanle" => $giabanle,
            "id_danhmuc" => $id_danhmuc,
            "tendm" => $tendm,
            "img" => $img_name,
            "ngaytao" => $ngaytao
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