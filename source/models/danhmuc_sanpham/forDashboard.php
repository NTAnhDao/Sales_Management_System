<?php
// Không cần session_start() vì đã gọi trong index.php

// Kết nối cơ sở dữ liệu
require '../database/db.php'; // Đường dẫn từ Function/ quay ra ngoài và vào database/

$conn = connectDB();

if (!$conn) {
    die("Lỗi kết nối cơ sở dữ liệu: " . $conn->connect_error);
}

// Tính thời gian hết hạn của cookie (hết ngày hiện tại)
$expire = strtotime('tomorrow');
$_SESSION["1day"] = $expire;

// Đếm tổng số lượng sản phẩm
$query = "SELECT COUNT(*) as total FROM sanpham";
$result = $conn->query($query);
$totalProducts = $result->fetch_assoc()['total'] ?? 0;
setcookie("totalProducts", $totalProducts, $expire, "/");

// Đếm tổng số danh mục
$query = "SELECT COUNT(*) as total FROM danhmucsp";
$result = $conn->query($query);
$totalCategories = $result->fetch_assoc()['total'] ?? 0;
setcookie("totalCategories", $totalCategories, $expire, "/");

// Đếm tổng số nhân viên
$query = "SELECT COUNT(*) as total FROM nhanvien";
$result = $conn->query($query);
$totalEmployees = $result->fetch_assoc()['total'] ?? 0;
setcookie("totalEmployees", $totalEmployees, $expire, "/"); // Sửa lỗi ở đây

$conn->close();
?>