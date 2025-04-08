<?php 
    include "database/db.php";

    $conn = connectDB();

    if(isset($_GET["token"])) {
        $token = $_GET["token"];
        $time = time();
        
        $sql = "SELECT * FROM taikhoan WHERE activate_token = ? and expire_time > ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("si", $token, $time);
        $stmt->execute();
        $result = $stmt->get_result();
        if($result->num_rows === 1) {
            $data = $result->fetch_assoc();
            $id = $data["id"];

            //cập nhật tài khoản thành đã được kích hoạt
            $sql = "UPDATE taikhoan SET trangthai_hd = 1 WHERE id = ?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $stmt->close();

            //chuyển đến trang đăng nhập
            header("Location: index.php");
            exit();
        
        } else {
            echo "<script>alert('Liên kết đăng nhập đã hết hạn. Vui lòng liên hệ admin của bạn.');
                window.location.href = 'index.php';</script>";
            exit();
        }
        
        
    }
?>