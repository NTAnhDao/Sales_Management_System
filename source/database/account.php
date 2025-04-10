<?php
    include "db.php";
    
    function login($username, $pass) {
        $conn = connectDB();
        $sql = "SELECT taikhoan.*, nhanvien.email, nhanvien.id, nhanvien.hoten FROM taikhoan, nhanvien WHERE id_nhanvien=nhanvien.id and username = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $username);
        
        if(!$stmt->execute()) return 'Không thể login, vui lòng liên hệ với admin';

        $res = $stmt->get_result();
        if($res->num_rows !== 1) return 'Tên đăng nhập không chính xác';
        $kq = $res->fetch_assoc();
        $hashed = $kq['password'];

        if($pass !== $hashed) return 'Mật khẩu không chính xác';

        $activated = $kq['trangthai_hd'];
        if($activated === 0) return 'Vui lòng đăng nhập bằng cách nhấp vào liên kết trong email của bạn';

        $status_lock = $kq['trangthai_khoa'];
        if($status_lock === 1) return 'Tài khoản của bạn đã bị khóa';
        
        $conn->close();
        return $kq;
    }

    

?>

