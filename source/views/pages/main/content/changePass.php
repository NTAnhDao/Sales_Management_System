<?php 
    include_once "../database/db.php";
    $error = "";
    $succ = "";
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $conn = connectDB();
        $newPass = $_POST['newPass'];
        $confirmPass = $_POST['confirmPass'];
        $flag = $_SESSION['pass_changed'] == 0 ? 1 : 0;

        if ($_SESSION['pass_changed'] == 1) {
            $oldPass = $_POST['oldPass'];
            
            $stmt = $conn->prepare("SELECT password FROM taikhoan WHERE id_nhanvien=?");
            $stmt->bind_param("i", $_SESSION['id']);
            $stmt->execute();
            $stmt->bind_result($password);
            $stmt->fetch();
            $stmt->close();
            if ($password !== $oldPass) {
                $error = "Mật khẩu hiện tại không đúng!";
            } else if ($password == $newPass) {
                $error = "Mật khẩu mới không được giống mật khẩu hiện tại!";
            }
        }
    
        if ($newPass !== $confirmPass) {
            $error = "Mật khẩu không khớp!";
        } 
        if ($error == "") {
            $stmt = $conn->prepare("UPDATE taikhoan SET password=?, pass_changed=1 WHERE id_nhanvien=?");
            $stmt->bind_param("si", $newPass, $_SESSION['id']);
            if ($stmt->execute()) {
                $_SESSION['pass_changed'] = 1;
                if ($flag == 1) {
                    echo "<script>window.location.href='index.php';</script>";
                }
                $succ = "Cập nhật mật khẩu thành công!";
            } else {
                $error = "Lỗi khi cập nhật mật khẩu!";
            }
            $stmt->close();
        }
    }
?>
<div class="main-content" id="main">
    <main>
        <div class="display-content">
            <section class="form-container">
                <div class="text-center mb-4" id="form-title">
                    <?php 
                        if ($_SESSION['pass_changed'] == 1) {
                        ?>
                            <h3>Đổi mật khẩu</h3>
                        <?php
                        } else {
                        ?>
                            <h3>Vui lòng đổi mật khẩu để sử dụng hệ thống</h3>
                            <script>
                                main.style.marginLeft= "0";
                            </script>
                        <?php
                        }
                    ?>
                </div>
                <form action="" method="POST">
                    <?php 
                        if ($_SESSION['pass_changed'] == 1) {
                        ?>
                            <div class="form-group">
                                <div class="input-box">
                                    <label for="oldPass">Mật khẩu hiện tại:</label>
                                    <input type="password" id="oldPass" name="oldPass" required>
                                </div>
                            </div>
                        <?php
                        } 
                    ?>
                    <div class="form-group">
                        <div class="input-box">
                            <label for="newPass">Mật khẩu mới:</label>
                            <input type="password" id="newPass" name="newPass" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-box">
                            <label for="confirmNewPass">Xác nhận mật khẩu mới:</label>
                            <input type="password" id="confirmPass" name="confirmPass" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <?php if($error != "") {
                            ?><div class="alert alert-danger p-2"><?=$error?></div>
                            <?php
                        } if ($succ != "") {
                            ?><div class="alert alert-success p-2"><?=$succ?></div>
                            <?php
                        } ?>
                    </div>
                    <div class="right-actions">
                        <button class="btn btn btn-primary m-2" type="submit" name="changePass">Lưu</button>
                        <?php 
                            if ($_SESSION['pass_changed'] == 0) {
                                ?>
                                    <button class="btn btn btn-secondary m-2" type="button" onclick="window.location.href='../Login/logout.php'">Đăng xuất</button>
                                <?php
                            } 
                        ?>
                    </div>
                    
                </form>
            </section>
    </main>
</div>