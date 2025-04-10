<?php 
    header("Content-Type: application/json");
   
    include "../../database/db.php";
    include_once "sendEmail.php";

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $fullname = $_POST["fullname"];
        $email = $_POST["email"];
        $gender = $_POST["gender"] !== "null" ? $_POST["gender"] : null;
        $sdt = $_POST["SDT"] !== "" ? $_POST["SDT"] : null;
        
        if($gender !== null) {
            if ($gender == "female") {
                $gender = "Nữ";
            } else {
                $gender = "Nam";
            }
        }

        if (!empty($fullname) && filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $conn = connectDB();
            $sql = 'SELECT * FROM nhanvien WHERE email = ?';
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("s", $email); 
            $stmt->execute();

            $res = $stmt->get_result();
            if($res->num_rows >= 1) {
                echo json_encode(["error" => "Tài khoản có email này đã tồn tại!"]);
                exit();
            }

            //Lưu data vào database bảng nhân viên
            $sql = 'INSERT INTO nhanvien (hoten, email, gioitinh, sdt) VALUE (?, ?, ?, ?)';
            $stmt->prepare($sql);
            $stmt->bind_param("ssss", $fullname, $email, $gender, $sdt);
            
            if($stmt->execute()) {
                //Lấy mã nhân viên vừa thêm vào
                $stmt = $conn->prepare("SELECT id FROM nhanvien WHERE email = ?");
                $stmt->bind_param("s",$email);
                $stmt->execute();
                $result = $stmt->get_result();

                $row = $result->fetch_assoc(); 
                $id = $row['id'];

                $temp_pass = "52300048"; // Mật khẩu tạm thời

                //tạo data
                $usename = strstr($email, '@', true); //Tách phần đầu của email
                $token = bin2hex(random_bytes(4));
                $time = time() + 60; //Hiệu lực trong 1 phút

                //Lưu data vào database bảng tài khoản
                $stmt = $conn->prepare("INSERT INTO taikhoan (id_nhanvien, username, password, role, trangthai_hd, activate_token, expire_time, pass_changed) VALUE (?, ?, ?, 'Nhân viên', 0, ?, ?, 0)");
                $stmt->bind_param("ssssi", $id, $usename, $temp_pass, $token, $time);
                if($stmt->execute()) {
                    //Gửi mail
                    $mess = send_email($email, $token);
                    if (is_bool($mess)) {
                        $mess = "Email đã được gửi thành công";
                    }
                    echo json_encode([
                        "success" => true,
                        "mail" => $mess
                    ]);
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