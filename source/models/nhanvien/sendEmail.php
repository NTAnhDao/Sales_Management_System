<?php 
    use PHPMailer\PHPMailer\PHPMailer;
    use PHPMailer\PHPMailer\Exception;
    include_once __DIR__ . "/../../vendor/autoload.php";

    function send_Email($email, $token) {
        $mail = new PHPMailer(true);

        try {
            //cấu hình SMTP
            $mail->isSMTP();
            $mail->Host = 'smtp.gmail.com'; //SMTP Server
            $mail->SMTPAuth = true;
            $mail->Username = 'itnotformehihi@gmail.com';
            $mail->Password = 'hwcrpxkjpsgvnhlc';
            $mail->SMTPSecure = 'tls';
            $mail->Port = 587;

            // Thiết lập charset cho tiêu đề và nội dung email
            $mail->CharSet = 'UTF-8';

            //cấu hình email
            $mail->setFrom('itnotformehihi@gmail.com', 'Admin của hệ thống cửa hàng MyPOS'); //Người gửi
            $mail->addAddress($email); // Người nhận

            $mail->isHTML(true); // Cho phép sử dụng HTML
            $mail->Subject = 'Thông tin đăng nhập hệ thống POS'; //Tiêu đề

            $link = 'http://localhost/d-an-cu-i-k/source/index.php?token='.$token; //link để đăng nhập
            $mail->Body = "Xin chào,<br>
                        Chúng tôi đã tạo tài khoản cho bạn tại hệ thống cửa hàng MyPOS.<br>
                        Tên đăng nhập của bạn là phần đầu email. Mật khẩu tạm thời là 52300048.<br>
                        Vui lòng nhấn vào liên kết sau để đăng nhập vào hệ thống lần đầu tiên và đổi mật khẩu của bạn.<br>
                        Liên kết: <a href='$link'>$link</a><br>
                        Liên kết chỉ có hiệu lực trong 1 phút. Nếu qua thời gian này, vui lòng liên hệ với admin để nhận lại liên kết khác.<br>
                        Trân trọng,<br>
                        Admin của hệ thống cửa hàng MyPOS"; //Nội dung email
            
            // Gửi email
            $mail->send();
            return true;
        } catch (Exception $e) {
            return "Gửi email không thành công";
        }
    } 
    
?>