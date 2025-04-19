<?php 
    include_once "../database/db.php";
    $error = "";
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $newPass = $_POST['newPass'];
        $confirmPass = $_POST['confirmPass'];
        
    
        if ($newPass !== $confirmPass) {
            $error = "Mật khẩu không khớp!";
        } else {
            $conn = connectDB();
            $stmt = $conn->prepare("UPDATE taikhoan SET password=?, pass_changed=1 WHERE id_nhanvien=?");
            $stmt->bind_param("si", $newPass, $_SESSION['id']);
            if ($stmt->execute()) {
                $_SESSION['pass_changed'] = 1;
                echo "<script>alert('Mật khẩu đã được cập nhật!');
                window.location.href = 'index.php';
                </script>";
                
            } else {
                $error = "Lỗi khi cập nhật mật khẩu!";
            }
            $stmt->close();
        }
    }
?>
<div class = "main-pass" id="main">
    <main>
        <div class="header-pass">
            <div class="header-name"><h3>Vui lòng đổi mật khẩu để sử dụng hệ thống</h3></div>
        </div>
        <div class="content">
            <section class="changePass">
                <form action="" method="POST">
                    <div class="group">
                        <div class="input-box">
                            <label for="newPass">Mật khẩu mới:</label>
                            <input type="password" id="newPass" name="newPass" required>
                        </div>
                    </div>
                    <div class="group">
                        <div class="input-box">
                            <label for="confirmNewPass">Xác nhận mật khẩu mới:</label>
                            <input type="password" id="confirmPass" name="confirmPass" required>
                        </div>
                    </div>
                    <div class="group">
                        <?php if($error != "") {
                            ?><p style="color:#FA8A5A; margin: 10px 10px;"><?= $error ?></p>
                            <?php
                        }?>
                        
                    </div>
                    <button class="btn btn btn-primary" type="submit" name="changePass">Lưu</button>
                    <button class="btn btn btn-secondary" type="button" onclick="window.location.href='logout.php'">Đăng xuất</button>
                </form>
            </section>
    </main>
</div>