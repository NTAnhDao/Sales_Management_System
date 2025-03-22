<?php
session_start(); // Bắt đầu session

// Hủy toàn bộ session
session_unset(); // Xóa biến session
session_destroy(); // Hủy session

// Chuyển hướng về trang đăng nhập
header("Location: ../Login/login.php");
exit();
?>