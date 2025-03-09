<?php
$server_name = "localhost";
$username = "username";
$password = "password";
$database = "quanlytrangweb";

// Tạo kết nối với databasedatabase
$conn = new mysqli($servername, $username, $password, $database);

// Kiểm tra kết nốinối
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully";

// Đóng kết nốinối
// $conn->close();
?>