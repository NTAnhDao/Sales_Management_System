<?php 
    session_start();
    ob_start();
    
    include "../database/account.php";

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $username = $_POST["username"];
        $password = $_POST["password"];
        $result = login($username, $password);

        if (is_array($result)) {
            $role = $result["role"];
            $_SESSION["role"] = $role;
            $_SESSION["id"] = $result["id_nhanvien"];
            $_SESSION["email"] = $result["email"];
            $_SESSION["username"]= $username;
            $_SESSION["tennv"] = $result["hoten"];
            $_SESSION["pass_changed"] = $result["pass_changed"];
            

            if($role == "Admin") {
                header("Location: ../Product_List_Admin/index.php");
                exit();
            } else if($role == "Nhân viên") {
                $_SESSION["pass_changed"] = $result["pass_changed"];
                header("Location: ../TrangNhanVien/index.php");
                exit();
            }
        
        } else {
            $_SESSION["error"] = $result;
            header("Location: ../index.php?e=1");
            exit();
        }
    }
          
?>