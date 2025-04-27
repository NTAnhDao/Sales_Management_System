<?php
require '../database/db.php';
include_once('permission.php');

if (!isset($_SESSION)) {
    session_start();
}

$conn = connectDB();
$sql = "SELECT sanpham.*, danhmucsp.tendm FROM sanpham 
        LEFT JOIN danhmucsp ON sanpham.id_danhmuc = danhmucsp.id";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-mavach='{$row["mavach"]}' data-giagoc='{$row["giagoc"]}' data-giabanle='{$row["giabanle"]}' data-id_danhmuc='{$row["id_danhmuc"]}' data-img='{$row["img"]}'>";
        echo "<td>" . htmlspecialchars($row["mavach"]) . "</td>";
        
        // Kiểm tra đường dẫn hình ảnh ở nhiều thư mục có thể
        $imgFile = $row["img"];
        $imgSrc = "";

        $possiblePaths = [
            "../models/danhmuc_sanpham/images/{$imgFile}",
             
            
        ];
        
        foreach ($possiblePaths as $path) {
            if (file_exists($path)) {
                $imgSrc = $path;
                break;
            }
        }
        
        echo "<td>" . ($imgSrc ? "<img src='{$imgSrc}' width='80' height='80' alt='Sản phẩm'>" : "Không có hình ảnh") . "</td>";
        
        echo "<td>" . htmlspecialchars($row["tensp"]) . "</td>";
        
        // Chỉ hiển thị giá nhập nếu có quyền xem giá nhập
        if(canViewImportPrice()) {
            echo "<td>" . number_format($row["giagoc"], 0, ',', '.') . "</td>";
        }
        
        echo "<td>" . number_format($row["giabanle"], 0, ',', '.') . "</td>";
        echo "<td>" . htmlspecialchars($row["tendm"] ?? 'Không có danh mục') . "</td>";
        echo "<td>" . $row["ngaytao"] . "</td>";
        
        // Chỉ hiển thị nút hành động nếu có quyền quản lý sản phẩm
        if(canManageProducts()) {
            echo "<td>
              <div class='action-buttons' style ='display: flex; gap: 5px; align-items: center; justify-content: center;'>
                  <a href='#' class='btn btn-sm btn-primary edit-btn' 
                     data-mavach='" . htmlspecialchars($row["mavach"], ENT_QUOTES) . "' 
                     data-img='" . htmlspecialchars($row["img"], ENT_QUOTES) . "' 
                     data-tensp='" . htmlspecialchars($row["tensp"], ENT_QUOTES) . "' 
                     data-giagoc='" . $row["giagoc"] . "' 
                     data-giabanle='" . $row["giabanle"] . "' 
                     data-id_danhmuc='" . $row["id_danhmuc"] . "' 
                     data-ngaytao='" . $row["ngaytao"] . "'>Chỉnh sửa</a>
                  <a href='#' class='btn btn-sm btn-danger delete-btn' 
                     data-mavach='" . htmlspecialchars($row["mavach"], ENT_QUOTES) . "'>Xóa</a>
              </div>
          </td>";
        }
        echo "</tr>";
    }
} else {
    // Tính số cột dựa trên quyền
    $colspan = 7; // Mặc định là 7 cột
    if(canViewImportPrice()) $colspan++; // Thêm 1 cột cho giá nhập
    if(canManageProducts()) $colspan++; // Thêm 1 cột cho hành động
    
    echo "<tr><td colspan='$colspan'>Không có sản phẩm nào.</td></tr>";
}

$conn->close();
?>