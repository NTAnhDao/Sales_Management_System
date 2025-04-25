<?php
if (!function_exists("connectDB")) {
  function connectDB() {
    $servername = "localhost";
    $username = "root";
    $password = "";
    $database = "quanlytrangweb";
    
    // Tạo kết nối với databasedatabase
    $conn = new mysqli($servername, $username, $password, $database);

    // Kiểm tra kết nốinối
    if ($conn->connect_error) {
      die("Connection failed: " . $conn->connect_error);
    }

    return $conn;
  }
}
?>