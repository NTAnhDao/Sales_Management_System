<?php
    require '../database/db.php';

    $conn = connectDB();

    if ($_SESSION['role'] == "Admin" && isset($_GET["id_detail"])) {
        // Admin xem thông tin nhân viên
        $sql = "SELECT * FROM nhanvien, taikhoan WHERE taikhoan.id_nhanvien=nhanvien.id AND nhanvien.id =" . $_GET["id_detail"];
    } else {
        $sql = "SELECT * FROM nhanvien, taikhoan WHERE taikhoan.id_nhanvien=nhanvien.id AND nhanvien.id =" . $_SESSION["id"];
    }

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $profile_name =  $row["hoten"];
        $profile_sdt = $row["sdt"];
        $profile_email = $row["email"];
        $profile_gender = $row["gioitinh"];
        $profile_ngaysinh = $row["ngaysinh"];
        $profile_diachi = $row["diachi"];
        $profile_img = $row["img"];
        $profile_role = $row["role"];
        if ($row["trangthai_khoa"] == 0) {
            $profile_status = "Đang làm việc";
        } else if ($row["trangthai_khoa"] == 1) {
            $profile_status = "Đã bị khóa";
        }
    }
    $conn->close();
?>