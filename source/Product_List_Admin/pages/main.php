<div class = "main">                
    <!-- sidebar -->
    <?php include("main/sidebar.php");?>
    <!-- nội dung chính-->
    <?php 
    if(isset($_GET['sidebar']))
    {
        $side = $_GET['sidebar'];
        if($side == 'quanlynhanvien'){
            include("main/content/quanlynhanvien.php");
        }else if($side == 'quanlykhachhang'){
            include("main/content/quanlykhachhang.php");
        }else if($side == 'quanlyspdanhmuc'){
            include("main/content/quanlyspdanhmuc.php");
        }else if($side == 'baocaophantich'){
            include("main/content/baocaophantich.php");
        }
    }else if(isset($_GET['nav'])){
        switch($_GET['nav']){
            case 'hoso':
                include("main/content/hoso.php");
                break;
            case 'Dangxuat':
                if (isset($_SESSION['role'])) {
                    unset($_SESSION['role']);
                }
                header('Location: ../Login/login.php');
                break;
        }
    } else {
        include("main/content/trangchu.php");
    } 
    ?>
</div> 