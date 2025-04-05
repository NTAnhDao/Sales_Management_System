<?php 
    header("Content-Type: application/json");
    include "../../database/db.php";
    include "sendEmail.php";

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $fullname = $_POST["fullname"];
        $email = $_POST["email"];
        $gender = $_POST["gender"];
        $sdt = $_POST["SDT"];

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
                
                //tạo data
                $usename = strstr($email, '@', true); //Tách phần đầu của email
                $token = bin2hex(random_bytes(4));
                $time = time() + 60; //Hiệu lực trong 1 phút
                //Lưu data vào database bảng tài khoản
                $query = $conn->prepare("INSERT INTO taikhoan (id_nhanvien, username, password, token, role, Thoigianhieuluc, pass_changed) VALUE (?, ?, ?, ?, 'Nhân viên', ?, '1')");
                $query->bind_param("sssss", $id, $usename, $temp_pass, $token, $time);
                if($query->execute()) {
                    echo json_encode(["success" => true]);
                    echo "<script>console.log('hello')</script>";
                    //Gửi mail
                    $mess = send_email($email, $token);
                    // echo json_encode(["mail" => $mess]);
                } else {
                    echo json_encode(["error" => "Lỗi khi thêm tài khoản vào database!"]);
                }
            } else {
                echo json_encode(["error" => "Lỗi khi thêm nhân viên vào database!"]);
            }
            
        }
    }
    $conn->close();
?>