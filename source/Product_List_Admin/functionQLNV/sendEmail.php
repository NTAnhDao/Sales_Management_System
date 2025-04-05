<?php 
    use PHPMailer\PHPMailer\PHPMailer;
    use PHPMailer\PHPMailer\Exception;
    include "../../vendor/autoload.php";

    function send_Email($email, $token) {
        $mail = new PHPMailer(true);

        try {
            //cấu hình SMTP
            $mail->isSMTP();
            $mail->Host = 'smtp.gmai.com'; //SMTP Server
            $mail->SMTPAuth = true;
            $mail->Username = 'itnotformehihi@gmail.com';
            $mail->Password = 'NzV2XDKr$kXX2@T';
            $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
            $mail->Port = 587;

            //cấu hình email
            $mail->setFrom('itnotformehihi@gmail.com', 'Admin');
            $mail->addAddress($email); // Người nhận

            $mail->isHTML(true); // Cho phép sử dụng HTML
            $mail->Subject = 'Tài khoản đăng nhập hệ thống'; //Tiêu đề

            $link = ""; //link để đăng nhập
            $mail->Body = "Tài khoản đã được tạo. Vui lòng nhấn vào <a href='$link'>liên kết này</a> để đăng nhập vào hệ thống.
                            Liên kết chỉ có hiệu lực trong 1 phút. Nếu qua thời gian này, vui lòng liên hệ với admin để nhận lại liên kết khác.";
            
            // Gửi email
            $mail->send();
            var_dump("Email đã được gửi thành công");
        } catch (Exception $e) {
            var_dump("Lỗi gửi email: {$mail->ErrorInfo}");
        }
    } 

    send_Email("trannuu2005@gmail.com","");
?>