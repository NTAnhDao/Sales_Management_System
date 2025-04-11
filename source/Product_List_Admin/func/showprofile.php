<?php
    require '../database/db.php';

    $conn = connectDB();

    $sql = "SELECT * FROM nhanvien WHERE id =" . $_SESSION["id"];

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $profile_name =  $row["hoten"];
        $profile_sdt = $row["sdt"]?? "Chưa có dữ liệu";
        $profile_email = $row["email"];
        $profile_gender = $row["gioitinh"]?? "Chưa có dữ liệu";
        $profile_img = $row["img"];
    }
    $conn->close();
?>