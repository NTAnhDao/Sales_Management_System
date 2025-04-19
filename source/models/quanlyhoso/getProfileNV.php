<?php
    require '../database/db.php';

    $conn = connectDB();

    if(empty($_SESSION["id"])) {
        header("Location: ../index.php");
        exit();
    }else{
        $sql = "SELECT * FROM nhanvien WHERE id =" . $_SESSION["id"];
    }
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $profile_name =  $row["hoten"]??"";
        $profile_sdt = $row["sdt"]??"";
        $profile_email = $row["email"]??"";
        $profile_gender = $row["gioitinh"]??"";
        $profile_ngaysinh = $row["ngaysinh"]??"";
        $profile_diachi = $row["diachi"]??"";
        $profile_img = $row["img"]??"";
    }
    $conn->close();
?>