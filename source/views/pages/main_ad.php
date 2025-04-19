<?php include '../models/danhmuc_sanpham/forDashboard.php' ?>
<div class = "main">                
    <!-- sidebar -->
    <?php include("main/sidebar_ad.php");?>
    <!-- nội dung chính-->
    <?php 
    if(isset($_GET['sidebar']))
    {
        $side = $_GET['sidebar'];
        if($side == 'quanlysanpham'){
            include("main/content/quanlysanpham.php");
        }else if($side == 'quanlydanhmuc'){
            include("main/content/quanlydanhmuc.php");
        }else if($side == 'quanlynhanvien'){
            if(isset($_GET['id_view'])) {
                include("main/content/thongtinbanhang.php");
            } else {
                include("main/content/quanlynhanvien.php");
            }
        }else if ($side == 'baocao'){
            if(isset($_GET["idctdh"])) {
                include("../TrangNhanVien/pages/main/content/ctdh_donhang.php");
            } else {
                include("main/content/baocao.php");
            }
            
        }
    }else if(isset($_GET['nav'])){
        switch($_GET['nav']){
            case 'hoso':
                if (isset($_GET['query'])) {
                    include("main/content/suahoso.php");
                }else{
                    include("main/content/hoso.php");
                }
                break;
            case 'changepass':
                include("main/content/changePass.php");
                break;
            case 'Dangxuat':
                if (isset($_SESSION['role'])) {
                    unset($_SESSION['role']);
                }
                header('Location: ../../../index.php');
                break;
        }
    } else {
        include("main/content/trangchu_ad.php");
    } 
    ?>
</div> 