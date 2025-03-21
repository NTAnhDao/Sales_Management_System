<?php 
    session_start();
    ob_start();
    include "../database/db.php";
    include "user.php";

    if (isset($_POST["login"]) && ($_POST["login"])) {
        $username = $_POST["username"];
        $password = $_POST["password"];
        $info = getInfoUser($username);
        $pass = $info[0]["password"];
        $role = $info[0]["role"];

        if ($info != null) {
            if ($password == $pass) {
                if($role=="Admin") {
                    $_SESSION["role"]= $role;
                    header("Location: ../Product_List_Admin/E_CommerceWebsite.php");
                    exit();
                } else if($role=="Nhân viên") {
                    $_SESSION["role"]= $role;
                    header("Location: ../TrangNhanVien/index.php");
                    exit();
                }
                
            } else {
                header("Location: login.php?e=1");
                exit();
            }
            
        } else {
            header("Location: login.php?e=2");
            exit();
        }
    }
    
?>