<?php
    include "../database/db.php";
    
    function getInfoUser($username) {
        $conn = connectDB();
        $sql = "SELECT taikhoan.*, nhanvien.email, nhanvien.id, nhanvien.hoten FROM taikhoan, nhanvien WHERE id_nhanvien=nhanvien.id and username = '".$username."'";
        $stmt = $conn->prepare($sql);
        $stmt->execute();
        $res = $stmt->get_result();
        $kq = $res->fetch_all(MYSQLI_ASSOC);
        $conn->close();
        return $kq;
    }
?>
