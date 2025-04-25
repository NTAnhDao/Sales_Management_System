<?php 
    session_start(); 
    if(isset($_SESSION["error"])) {
        $error = $_SESSION["error"];
        unset($_SESSION["error"]);
    }
?>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Point of Sale</title>
        <link rel="icon" type="image/x-icon" href="../images/logo.ico">
        <link rel="stylesheet" href="Login/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    </head>
    <body>
    <div class="header">
        <h1>MyPOS</h1>
    </div>
        <div class="wrapper">
       
            <div class="container">
                <div class="col col-1">
                    <img src="Login/pos.png" style="width: fit-content;">
                    <h3>Chào mừng bạn đến với Quản lý bán hàng <br> Point of Sale</h3>
                </div>
                <div class="col col-2">
                    <div class="login-form">
                            <form method="POST" action="Login/plogin.php">
                                <h2 class="title-form">LOGIN</h2>
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
                                        <p style="color:#FA8A5A; margin: 10px 10px;"><?= $error ?></p>
                                        <?php }
                                    } ?>
                            </form>
                        </div>
                        
                    </div>

                </div>                
            </div>
        </div>
        
    </body>

<script src="Login/main.js"></script>
</html>