<?php
    include "../database/db.php";
    
    function getInfoUser($username) {
        $conn = connectDB();
        $sql = "SELECT * FROM taikhoan WHERE username = '".$username."'";
        $stmt = $conn->prepare($sql);
        $stmt->execute();
        $stmt->execute();
        $res = $stmt->get_result();
        $kq = $res->fetch_all(MYSQLI_ASSOC);
        return $kq;
    }
?>
