<?php 
    include "../database/db.php";
    include_once "sendEmail.php";

    
    $token = $_GET["token_send"];
    $conn = connectDB();

    $sql = "SELECT expire_time, email FROM taikhoan, nhanvien WHERE id_nhanvien=nhanvien.id AND activate_token = ?" ;
    $stmt = $conn->prepare( $sql );
    $stmt->bind_param("s", $token);

    $stmt->execute();
    $result = $stmt->get_result();
    if($result->num_rows !== 1) {
        echo "<script>alert('Không tìm thấy tài khoản này!');
        window.location.href = 'index.php?sidebar=quanlynhanvien';
        </script>";
        exit();
    }
    $data = $result->fetch_assoc();
    $expire_time = $data["expire_time"];
    if($expire_time >= time()) {
        echo "<script>alert('Link đăng nhập chưa hết hạn.');
        window.location.href = 'index.php?sidebar=quanlynhanvien';
        </script>";
        exit();
    }
    
    
    $email = $data["email"];

    
    //Gửi lại mail
    $mess = send_Email($email, $token);
    if (is_bool( $mess )) {
        $new_time = time() + 60; //Thay đổi thời gian hết hạn
        $sql = "UPDATE taikhoan SET expire_time = ? WHERE activate_token = ?";
        $stmt = $conn->prepare( $sql );
        $stmt->bind_param("ss", $new_time, $token);
        $stmt->execute();
        $mess = "Email đã được gửi thành công";
    }
    
    $conn->close();
    echo "<script>alert('" .$mess. "!');
    window.location.href = 'index.php?sidebar=quanlynhanvien';</script>";
    exit();
    
    
    
    
?>