<?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để xóa danh mục!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = $_POST["id"] ?? '';

    if (empty($id)) {
        echo json_encode(["success" => false, "message" => "ID danh mục không hợp lệ!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Xóa danh mục
    $query = $conn->prepare("DELETE FROM danhmucsp WHERE id = ?");
    $query->bind_param("i", $id);

    if ($query->execute()) {
        echo json_encode(["success" => true, "message" => "Xóa danh mục thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Xóa danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?><?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để xóa danh mục!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = $_POST["id"] ?? '';

    if (empty($id)) {
        echo json_encode(["success" => false, "message" => "ID danh mục không hợp lệ!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Xóa danh mục
    $query = $conn->prepare("DELETE FROM danhmucsp WHERE id = ?");
    $query->bind_param("i", $id);

    if ($query->execute()) {
        echo json_encode(["success" => true, "message" => "Xóa danh mục thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Xóa danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?><?php
session_start();
header("Content-Type: application/json");
require '../../database/db.php';

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để xóa danh mục!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = $_POST["id"] ?? '';

    if (empty($id)) {
        echo json_encode(["success" => false, "message" => "ID danh mục không hợp lệ!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    // Xóa danh mục
    $query = $conn->prepare("DELETE FROM danhmucsp WHERE id = ?");
    $query->bind_param("i", $id);

    if ($query->execute()) {
        echo json_encode(["success" => true, "message" => "Xóa danh mục thành công!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Xóa danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>