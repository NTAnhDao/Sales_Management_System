<div class="sidebar"  id="mySidebar" >
    <div class="sidebar-user">
        <img src="<?=$avtPath?>" height = "70px"  width = "70px" alt = "">
            <div>
                <h3><?=$_SESSION['tennv']?></h3>
                <span><?=$_SESSION['role']?></span>
        </div>
    </div>
    <div class="sidebar-menu">
        <ul >
            <li class="item">
                <a class="link" href="index.php">
                    <span class = "fa fa-home"></span>
                    <p class="item-sidebar">Trang chủ</p>
                </a>
            </li>
            <?php
            if ($_SESSION['role'] == 'Admin') {
                ?>
                <li class="item">
                    <a class="link" href="index.php?sidebar=quanlynhanvien">
                        <span class = "fa fa-users"></span>
                        <p class="item-sidebar">Quản lý nhân viên</p> 
                    </a>
                </li>
            <?php 
        }?>

                <li class="item">
                    <a class="link" href="index.php?sidebar=quanlydanhmuc">
                        <span class = "fa fa-tags"></span>
                        <p class="item-sidebar">Quản lý danh mục</p>
                    </a>
                </li>
            
            <li class="item">
                <a class="link" href="index.php?sidebar=quanlysanpham">
                    <span class = "fa fa-list"></span>
                    <p class="item-sidebar">Quản lý sản phẩm</p> 
                </a>
            </li>
           
                <li class="item">
                    <a class="link" href="index.php?sidebar=giaodich">
                        <span class = "fa fa-shopping-cart"></span>
                        <p class="item-sidebar">Xử lý giao dịch</p>
                    </a>
                </li>
                <li class="item">
                    <a class="link" href="index.php?sidebar=khachhang">
                        <span class = "fa fa-users"></span>
                        <p class="item-sidebar">Khách hàng</p>
                    </a>
                </li>
                <li class="item">
                    <a class="link" href="index.php?sidebar=donhang">
                        <span class = "fa fa-credit-card"></span>
                        <p class="item-sidebar">Đơn hàng</p>
                    </a>
                </li>
             
            <li class="item">
                <a class="link" href="index.php?sidebar=baocao">
                    <span class = "fa fa-bar-chart"></span>
                    <p class="item-sidebar">Báo cáo</p> 
                </a>
            </li>
            
        
        </ul>
    </div>
    <div class="logout">
        <a class="link" href="../../Login/logout.php">
            <span class = "fa fa-sign-out"></span>
        </a>
    </div>
</div> 