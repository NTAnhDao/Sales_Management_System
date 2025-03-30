<?php 
    include "../../database/db.php";

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $fullname = $_POST["fullname"];
        $email = $_POST["email"];
        $gender = $_POST["gender"] ?? "";
        $sdt = $_POST["SDT"] ?? "";

        if(!empty($gender)) {
            if ($gender == "female") {
                $gender = "Nữ";
            } else {
                $gender = "Nam";
            }
        }


        if (!empty($fullname) && filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $temp_pass = "52300048"; // Mật khẩu tạm thời

            $conn = connectDB();
            //Lưu data vào database bảng nhân viên
            $query = $conn->prepare("INSERT INTO nhanvien (hoten, email, gioitinh, sdt) VALUE (?, ?, ?, ?)");
            $query->bind_param("ssss",$fullname,$email, $gender, $sdt);
            if($query->execute()) {
                //Lấy mã nhân viên vừa thêm vào
                $query = $conn->prepare("SELECT * FROM nhanvien WHERE email = ?");
                $query->bind_param("s",$email);
                $query->execute();
                $result = $query->get_result();
                $row = $result->fetch_assoc();
                $id = $row["id"];

                //Tách phần đầu của email
                $usename = strstr($email, '@', true);
                //Lưu data vào database bảng tài khoản
                $query = $conn->prepare("INSERT INTO taikhoan (id_nhanvien, username, password, role) VALUE (?, ?, ?, 'Nhân viên')");
                $query->bind_param("sss", $id, $usename, $temp_pass);
                if($query->execute()) {
                    echo "<script>alert('Tạo tài khoản thành công!');</script>";
                } else {
                    echo "<script>alert('Tạo tài khoản thất bại!');</script>";
                }
            } else {
                echo "<script>alert('Lỗi khi thêm nhân viên vào database!');</script>";
            }
            
        }
    }
?>