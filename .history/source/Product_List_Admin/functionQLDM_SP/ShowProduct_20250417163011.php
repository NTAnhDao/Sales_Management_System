<?php
require '../database/db.php';

$conn = connectDB();
$sql = "SELECT sanpham.*, danhmucsp.tendm FROM sanpham 
        LEFT JOIN danhmucsp ON sanpham.id_danhmuc = danhmucsp.id";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-mavach='{$row["mavach"]}'>";
        echo "<td>" . htmlspecialchars($row["mavach"]) . "</td>";
        $imgPath = $row["img"] ? "image/" . htmlspecialchars($row["img"]) : '';
        $imgSrc = $imgPath && file_exists($imgPath) ? $imgPath : ''; // Kiểm tra xem file hình ảnh có tồn tại không
        echo "<td>" . ($imgSrc ? "<img src='{$imgSrc}' width='80' height='80'>" : "Không có hình ảnh") . "</td>";
        
        echo "<td>" . htmlspecialchars($row["tensp"]) . "</td>";
        echo "<td>" . number_format($row["giagoc"], 0, ',', '.') . "</td>";
        echo "<td>" . number_format($row["giabanle"], 0, ',', '.') . "</td>";
        echo "<td>" . htmlspecialchars($row["tendm"] ?? 'Không có danh mục') . "</td>";
        echo "<td>" . $row["ngaytao"] . "</td>";
        echo "<td>
                <a href='#' class='btn btn-sm btn-primary mr-1 edit-btn' 
                   data-mavach='" . htmlspecialchars($row["mavach"], ENT_QUOTES) . "' 
                   data-img='" . htmlspecialchars($row["img"], ENT_QUOTES) . "' 
                   data-tensp='" . htmlspecialchars($row["tensp"], ENT_QUOTES) . "' 
                   data-giagoc='" . $row["giagoc"] . "' 
                   data-giabanle='" . $row["giabanle"] . "' 
                   data-id_danhmuc='" . $row["id_danhmuc"] . "' 
                   data-ngaytao='" . $row["ngaytao"] . "'>Chỉnh sửa</a>
                <a href='#' class='btn btn-sm btn-danger delete-btn' 
                   data-mavach='" . htmlspecialchars($row["mavach"], ENT_QUOTES) . "'>Xóa</a>
              </td>";
        echo "</tr>";
    }
} else {
    echo "<tr><td colspan='8'>Không có sản phẩm nào.</td></tr>";
}

$conn->close();
?>