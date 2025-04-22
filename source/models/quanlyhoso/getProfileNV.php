<?php
    require '../database/db.php';

    $conn = connectDB();

    if (isset($_GET["id_detail"])) {
        $id = $_GET["id_detail"];
    } else {
        $id = $_SESSION["id"];
    }
    if(empty($_SESSION["id"])) {
        header("Location: ../index.php");
        exit();
    }else{
        $sql = "SELECT nhanvien.*, taikhoan.trangthai_khoa, taikhoan.role FROM nhanvien, taikhoan WHERE nhanvien.id = taikhoan.id_nhanvien AND nhanvien.id =" . $id;
    }
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $profile_name =  $row["hoten"]??"";
        $profile_role = $row['role']??"";
        $profile_sdt = $row["sdt"]??"";
        $profile_email = $row["email"]??"";
        $profile_gender = $row["gioitinh"]??"";
        $profile_ngaysinh = $row["ngaysinh"]??"";
        $profile_diachi = $row["diachi"]??"";
        $profile_img = $row["img"]??"";
        $profile_trangthai = $row["trangthai_khoa"] == 0 ? 1 : 0;
    }
    $conn->close();
?>