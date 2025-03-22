<div class = "main">              
    <!-- sidebar -->
    <?php include("main/sidebar.php");?>
    <!-- nội dung chính-->
    <?php 
    if(isset($_GET['sidebar']))
    {
        $side = $_GET['sidebar'];
        if($side == 'khachhang'){
            include("main/content/khachhang.php");
        }else if($side == 'giaodich'){
            include("main/content/giaodich.php");
        }else if($side == 'donhang'){
            include("main/content/donhang.php");
        }else if($side == 'baocao'){
            include("main/content/baocao.php");
        }
    }else if(isset($_GET['nav'])){
        if($_GET['nav']== 'hoso'){
            include("main/content/hoso.php");
        }
    } else {
        include("main/content/trangchu.php");
    } 
    ?>
</div> 