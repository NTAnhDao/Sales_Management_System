<div class = "main">                
    <!-- sidebar -->
    <?php include("main/sidebar.php");?>
    <!-- nội dung chính-->
    <?php 
    if(isset($_GET['sidebar']))
    {
        $side = $_GET['sidebar'];
        if($side == 'quanlysanpham'){
            include("main/content/quanlysanpham.php");
        }else if($side == 'quanlydanhmuc'){
            include("main/content/quanlydanhmuc.php");
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
                header('Location: ../../../index.php');
                break;
        }
    } else {
        include("main/content/trangchu.php");
    } 
    ?>
</div> 