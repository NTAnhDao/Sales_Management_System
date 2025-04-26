<?php
    if(isset($_GET["token"])) {
        include "Login/activate.php";
    } else {
        include "Login/login.php";
    }
?>