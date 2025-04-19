<?php
session_start();
require '../database/db.php';

header('Content-Type: application/json');

if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'Admin') {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền thực hiện thao tác này!']);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $mavach = $_POST['mavach'] ?? '';
    $tensp = $_POST['tensp'] ?? '';
    $giagoc = floatval($_POST['giagoc'] ?? 0);
    $giabanle = floatval($_POST['giabanle'] ?? 0);
    $id_danhmuc = intval($_POST['id_danhmuc'] ?? 0);

    // Kiểm tra dữ liệu đầu vào
    if (empty($mavach) || empty($tensp) || $giagoc <= 0 || $giabanle <= 0 || $id_danhmuc <= 0) {
        echo json_encode(['success' => false, 'message' => 'Vui lòng điền đầy đủ thông tin sản phẩm!']);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(['success' => false, 'message' => 'Lỗi kết nối cơ sở dữ liệu!']);
        exit;
    }

    // Kiểm tra mã vạch trùng
    $query_check = $conn->prepare("SELECT mavach FROM sanpham WHERE mavach = ?");
    $query_check->bind_param("s", $mavach);
    $query_check->execute();
    $result_check = $query_check->get_result();
    if ($result_check->num_rows > 0) {
        echo json_encode(['success' => false, 'message' => 'Mã vạch đã tồn tại!']);
        $query_check->close();
        $conn->close();
        exit;
    }
    $query_check->close();

    // Xử lý upload hình ảnh
    $img_name = null;
    if (isset($_FILES['file']) && $_FILES['file']['error'] === UPLOAD_ERR_OK) {
        $img_tmp = $_FILES['file']['tmp_name'];
        $img_name = $_FILES['file']['name'];
        $img_ext = strtolower(pathinfo($img_name, PATHINFO_EXTENSION));
        $allowed_ext = ['jpg', 'jpeg', 'png', 'gif'];

        if (!in_array($img_ext, $allowed_ext)) {
            echo json_encode(['success' => false, 'message' => 'Định dạng hình ảnh không hợp lệ!']);
            $conn->close();
            exit;
        }

        $new_img_name = uniqid() . '.' . $img_ext;
        $upload_dir = dirname(__DIR__) . '/images/';
        $upload_path = $upload_dir . $new_img_name;

        // Tạo thư mục nếu chưa tồn tại
        if (!is_dir($upload_dir)) {
            if (!mkdir($upload_dir, 0777, true)) {
                error_log("Lỗi khi tạo thư mục: $upload_dir");
                echo json_encode(['success' => false, 'message' => 'Lỗi khi tạo thư mục images!']);
                $conn->close();
                exit;
            }
        }

        // Kiểm tra quyền ghi thư mục
        if (!is_writable($upload_dir)) {
            error_log("Thư mục không có quyền ghi: $upload_dir");
            echo json_encode(['success' => false, 'message' => 'Thư mục images không có quyền ghi!']);
            $conn->close();
            exit;
        }

        if (!move_uploaded_file($img_tmp, $upload_path)) {
            error_log("Lỗi khi lưu tệp hình ảnh: $upload_path");
            echo json_encode(['success' => false, 'message' => 'Lỗi khi lưu tệp hình ảnh!']);
            $conn->close();
            exit;
        }
        $img_name = $new_img_name;
    }

    // Lấy tên danh mục
    $query_danhmuc = $conn->prepare("SELECT tendm FROM danhmucsp WHERE id = ?");
    $query_danhmuc->bind_param("i", $id_danhmuc);
    $query_danhmuc->execute();
    $result_danhmuc = $query_danhmuc->get_result();
    $tendm = $result_danhmuc->num_rows > 0 ? $result_danhmuc->fetch_assoc()['tendm'] : 'Không xác định';
    $query_danhmuc->close();

    // Thêm sản phẩm
    $query = $conn->prepare("INSERT INTO sanpham (mavach, tensp, giagoc, giabanle, img, id_danhmuc, ngaytao) 
                             VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP())");
    $query->bind_param("ssddsi", $mavach, $tensp, $giagoc, $giabanle, $img_name, $id_danhmuc);

    if ($query->execute()) {
        $selectQuery = "SELECT sanpham.*, danhmucsp.tendm 
                        FROM sanpham 
                        LEFT JOIN danhmucsp ON sanpham.id_danhmuc = danhmucsp.id 
                        WHERE mavach = ?";
        $selectStmt = $conn->prepare($selectQuery);
        $selectStmt->bind_param("s", $mavach);
        $selectStmt->execute();
        $result = $selectStmt->get_result();
        $product = $result->fetch_assoc();

        echo json_encode([
            'success' => true,
            'message' => 'Thêm sản phẩm thành công!',
            'mavach' => $product['mavach'],
            'tensp' => $product['tensp'],
            'giagoc' => $product['giagoc'],
            'giabanle' => $product['giabanle'],
            'id_danhmuc' => $product['id_danhmuc'],
            'tendm' => $product['tendm'],
            'img' => $product['img'],
            'ngaytao' => $product['ngaytao']
        ]);
        $selectStmt->close();
    } else {
        echo json_encode(['success' => false, 'message' => 'Thêm sản phẩm thất bại: ' . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(['success' => false, 'message' => 'Yêu cầu không hợp lệ!']);
}
?>