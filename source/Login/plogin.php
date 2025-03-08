<?php 
    session_start();
    $user = $_POST["username"];
    $password = $_POST["password"];
    if($user=="admin" && $password=="admin") {
        $_SESSION["username"] = $user;
        $_SESSION["password"] = $password;
        header("Location: index.php");
        exit();
    }   
    else {
        header("Location: login.php?e=1");
        exit();
    }
?>