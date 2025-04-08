<?php 
    if(isset($_GET["token_send"])){
        include_once "resendEmail.php";
    } else if(isset($_GET["token_lock"])){
        include_once "lock.php";
    } else if(isset($_GET["token_unlock"])){
        include_once "unlock.php";
    }
    // } else if(isset($_GET["iddetail"])){
    //     include "detailAcc.php";
    // } else if(isset($_GET["idview"])){
    //     include "viewSale.php";
    // } else if(isset($_POST["fullname"])){
    //     include "addAccount.php";
    // }
?>