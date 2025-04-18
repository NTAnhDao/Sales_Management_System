<?php 
    include_once "../database/db.php";
    $error = "";
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $conn = connectDB();
        $newPass = $_POST['newPass'];
        $confirmPass = $_POST['confirmPass'];

        if ($_SESSION['pass_changed'] == 1) {
            $oldPass = $_POST['oldPass'];
            
            $stmt = $conn->prepare("SELECT password FROM taikhoan WHERE id_nhanvien=?");
            $stmt->bind_param("i", $_SESSION['id']);
            $stmt->execute();
            $stmt->bind_result($password);
            $stmt->fetch();
            if ($password !== $oldPass) {
                $error = "Mật khẩu cũ không đúng!";
                exit();
            }
            
        }
    
        if ($newPass !== $confirmPass) {
            $error = "Mật khẩu không khớp!";
        } else {
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
<div class = "main-content" id="main">
    <main>
        <div class = "page-header">
        <?php 
            if ($_SESSION['pass_changed'] == 1) {
            ?>
                <h3>Đổi mật khẩu</h3>
            <?php
            } else {
            ?>
                <h3>Vui lòng đổi mật khẩu để sử dụng hệ thống</h3>
            <?php
            }
        ?>
        </div>
        <div class="content">
            <section class="changePass">
                <form action="" method="POST">
                <?php 
                    if ($_SESSION['pass_changed'] == 1) {
                        ?>
                        <div class="group">
                            <div class="input-box">
                                <label for="oldPass">Mật khẩu cũ:</label>
                                <input type="password" id="oldPass" name="oldPass" required>
                            </div>
                        </div>
                        <?php
                    } 
                ?>
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
                    <?php 
                    if ($_SESSION['pass_changed'] == 0) {
                        ?>
                            <button class="btn btn btn-secondary" type="button" onclick="window.location.href='logout.php'">Đăng xuất</button>
                        <?php
                    } 
                ?>
                </form>
            </section>
    </main>
</div>