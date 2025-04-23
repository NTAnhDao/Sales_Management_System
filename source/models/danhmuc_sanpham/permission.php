<?php
// Kiểm tra quyền quản lý (dành cho Admin)
function isAdmin() {
    return isset($_SESSION["role"]) && $_SESSION["role"] == "Admin";
}

// Kiểm tra quyền quản lý danh mục (thêm/sửa/xóa)
function canManageCategories() {
    return isAdmin();
}

// Kiểm tra quyền quản lý sản phẩm (thêm/sửa/xóa)
function canManageProducts() {
    return isAdmin();
}

// Kiểm tra quyền xem giá nhập
function canViewImportPrice() {
    return isAdmin();
}

// Kiểm tra quyền quản lý nhân viên
function hasEmployeeManagementPermission() {
    return isAdmin();
}

function hasCategoryManagementPermission() {
    return isAdmin();
}
// Kiểm tra quyền và chuyển hướng nếu không có quyền
function redirectIfNoPermission() {
    // No redirection by default - all users can access the base interface
    // Specific page permissions will be checked within each page
    return true;
}

// Kiểm tra nếu người dùng có thể xem tính năng cụ thể
function canViewFeature($featureName) {
    switch($featureName) {
        case 'add_product':
        case 'edit_product':
        case 'delete_product':
            return canManageProducts();
            
        case 'add_category':
        case 'edit_category':
        case 'delete_category':
            return canManageCategories();
            
        case 'view_import_price':
            return canViewImportPrice();
            
        case 'manage_employees':
            return hasEmployeeManagementPermission();
            
        default:
            return true; // Default allow if feature isn't specifically restricted
    }
}
?>