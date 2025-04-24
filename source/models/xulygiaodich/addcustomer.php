<?php
    require '../../database/db.php';
    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $ten = $_POST["ten"] ?? '';
        $diachi = $_POST["diachi"] ?? '';
        $sdt = $_POST["sdt"] ?? '';

        $conn = connectDB();

        $query = $conn->prepare("INSERT INTO `khachhang` (`id`, `hoten`, `diachi`, `sdt`, `ngaytao`) VALUES (NULL, ?, ?, ?, current_timestamp());");
        $query->bind_param("sss", $ten, $diachi, $sdt);
        $query->execute();
    }
    
    $query->close();
    $conn->close();
?>