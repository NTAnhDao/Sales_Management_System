<?php 
    include "../database/db.php";
    $conn = connectDB();
    
    
    $token = $_GET["token_lock"];
    $sql = "UPDATE taikhoan SET trangthai_khoa = 1 WHERE activate_token = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $token);
    if($stmt->execute()) {
        echo "<script>alert('Khóa tài khoản thành công!');</script>";
    }
    echo "<script>window.location.href = 'index.php?sidebar=quanlynhanvien';
    </script>";
    
    $conn->close();
    exit();
        
?>