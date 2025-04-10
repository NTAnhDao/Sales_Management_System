<?php
 session_start();
 $expire = strtotime('tomorrow');
 $_SESSION["1day"]=$expire;
 $sokhach = isset($_COOKIE["newcustomer"]) ? $_COOKIE["newcustomer"]  : 0;
 $donhang = isset($_COOKIE["donhangtoday"]) ? $_COOKIE["donhangtoday"]  : 0;
 $doanhthu = isset($_COOKIE["doanhthutoday"]) ? $_COOKIE["doanhthutoday"]  : 0;

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <link rel="icon" type="image/x-icon" href="../logo.ico">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script defer src="script.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/dayjs@1/dayjs.min.js"></script>
</head>

<body>
<?php
    if (isset($_SESSION["role"]) && ($_SESSION["role"]=="Nhân viên")) {

        //kiểm tra tài khoản đã đổi mật khẩu chưa
        if (isset($_SESSION["pass_changed"]) && $_SESSION["pass_changed"] === 0) {
            include("pages/changePass.php");
        } else {
             // NAVIGATION
            include("pages/nav.php");

            // Main
            include("pages/main.php");
        }
        
    } 
    else {
        header("Location: ../index.php");
        exit();
    }
?>
    
</body>
</html>
