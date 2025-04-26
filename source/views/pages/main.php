<?php
include '../models/danhmuc_sanpham/forDashboard.php';
include_once("../models/danhmuc_sanpham/permission.php"); // Đường dẫn tới file kiểm tra quyền

// Lưu quyền vào các thuộc tính data để sử dụng phía client
$isAdminValue = isAdmin() ? "1" : "0";
$canManageProductsValue = canManageProducts() ? "1" : "0";
$canManageCategoriesValue = canManageCategories() ? "1" : "0";
$canViewImportPriceValue = canViewImportPrice() ? "1" : "0";
?>
<div class="main">
    <!-- Hidden element to store user permissions for client-side access -->
    <div id="user-permissions" 
        data-is-admin="<?= $isAdminValue ?>" 
        data-can-manage-products="<?= $canManageProductsValue ?>" 
        data-can-manage-categories="<?= $canManageCategoriesValue ?>" 
        data-can-view-import-price="<?= $canViewImportPriceValue ?>" 
        style="display: none;"></div>

    <!-- sidebar -->

    <?php 
    include("main/sidebar.php");
    // Kiểm tra quyền truy cập
    redirectIfNoPermission();
    ?>


    <!-- nội dung chính -->
    <?php 
    if (isset($_GET['sidebar'])) {
        $side = $_GET['sidebar'];

        if ($side == 'quanlysanpham') {
            include("main/content/quanlysanpham.php");
        } else if ($side == 'quanlydanhmuc') {
            include("main/content/quanlydanhmuc.php");
        } else if ($side == 'quanlynhanvien') {
            if (isset($_GET['id_view'])) {
                include("main/content/thongtinbanhang.php");
            } else if (hasEmployeeManagementPermission()) {
                include("main/content/quanlynhanvien.php");
            } else {
                include("main/content/trangchu_nv.php");
            }
        } else if ($side == 'khachhang') {
            if (isset($_GET['idlskh'])) {
                if (isset($_GET['idctdh'])) {
                    include("main/content/chitietdh.php");
                } else {
                    include("main/content/lichsumua.php");
                }
            } else {
                include("main/content/khachhang.php");
            }
        } else if ($side == 'giaodich') {
            include("main/content/giaodich.php");
        } else if ($side == 'donhang') {
            if (isset($_GET['idctdh'])) {
                include("main/content/ctdh_donhang.php");
            } else {
                include("main/content/donhang.php");
            }
        } else if ($side == 'baocao') {
            if (isset($_GET["idctdh"])) {
                include("main/content/ctdh_donhang.php");
            } else {
                include("main/content/baocao.php");
            }
        }
    } else if (isset($_GET['nav'])) {
        switch ($_GET['nav']) {
            case 'hoso':
                if (isset($_GET['query'])) {
                    include("main/content/suahoso.php");
                } else {
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
        if (isset($_SESSION['role']) && $_SESSION['role'] == 'Admin' || $_SESSION['role'] == 'Nhân viên') {
            include("main/content/trangchu.php");
        }
    }
    ?>
</div>