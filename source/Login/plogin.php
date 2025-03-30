<?php 
    session_start();
    ob_start();
    include "../database/db.php";
    include "user.php";

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $username = $_POST["username"];
        $password = $_POST["password"];
        $info = getInfoUser($username);

        if ($info != null) {
            $pass = $info[0]["password"];
            $role = $info[0]["role"];

            if ($password == $pass) {
                $_SESSION["role"]= $role;

                if($role == "Admin") {
                    header("Location: ../Product_List_Admin/index.php");
                    exit();
                } else if($role == "Nhân viên") {
                    header("Location: ../TrangNhanVien/index.php");
                    exit();
                }
                
            } else {
                header("Location: ../Login/login.php?e=1");
                exit();
            }
            
        } else {
            header("Location: ../Login/login.php?e=2");
            exit();
        }
    } 
    
?>