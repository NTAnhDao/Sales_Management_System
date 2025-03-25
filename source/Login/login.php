<?php session_start(); ?>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Point of Sale</title>
        <link rel="icon" type="image/x-icon" href="../logo.ico">
        <link rel="stylesheet" href="style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="col col-1">
                <h2>Cửa hàng điện thoại</h2>
                <h2>Points of sale</h2>
            </div>
            <div class="col col-2">
                <div class="login-form">
                    <form method="POST" action="plogin.php">
                        <h2 class="title-form">Login</h2>
                        <div class="input-box">
                            <input name="username" type="text" placeholder="Username" required>
                            <i class='fa fa-user'></i>
                        </div>
                        <div class="input-box">
                            <input name="password" type="password" placeholder="Password" required>
                            <div id="eye">
                                <i class='fa fa-eye'></i>
                            </div>
                            
                        </div>
                        <input name="login" type="submit" class="button" value="Login">
                        <?php
                            if(isset($_GET["e"])) {
                                $e = $_GET["e"];
                                if($e=="1"){ ?>
                                <p style="color:red; margin: 10px 10px;" ><?php echo "Tên đăng nhập hoặc mật khẩu không chính xác"; ?></p>
                                <?php } else if($e== "2"){ ?>
                                <p style="color:red; margin: 10px 10px;" ><?php echo "Vui lòng đăng nhập bằng cách nhấp vào liên kết trong email của bạn"; ?></p>
                                <?php }
                            } ?>
                    </form>
                </div>
                
            </div>
            
        </div>
    </body>

<script src="main.js"></script>
</html>