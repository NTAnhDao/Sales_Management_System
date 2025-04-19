<?php 
    if(isset($_GET["token_send"])){
        include_once "resendEmail.php";
    } else if(isset($_GET["token_lock"])){
        include_once "lock.php";
    } else if(isset($_GET["token_unlock"])){
        include_once "unlock.php";
    } 
?>