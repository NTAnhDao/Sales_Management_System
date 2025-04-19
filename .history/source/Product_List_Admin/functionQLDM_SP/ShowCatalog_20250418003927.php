<?php
require '../database/db.php';

$conn = connectDB();
// Sửa truy vấn để lấy đúng cột hoten từ bảng taikhoan
$sql = "SELECT danhmucsp.*, taikhoan.username AS ten_nguoitao 
        FROM danhmucsp 
        LEFT JOIN taikhoan ON danhmucsp.id_nguoitao = taikhoan.id_nhanvien";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $stt = 1; // Thêm STT
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-id='{$row["id"]}'>";
        echo "<td>" . $stt++ . "</td>";
        echo "<td>" . htmlspecialchars($row["tendm"]) . "</td>";
        echo "<td>" . htmlspecialchars($row["mota"]) . "</td>";
        echo "<td>" . htmlspecialchars($row["ngaytao"]) . "</td>";
        echo "<td>" . (isset($row["ten_nguoitao"]) ? htmlspecialchars($row["ten_nguoitao"]) : 'Không xác định') . "</td>";
        echo "<td>
                <a href='#' class='btn btn-sm btn-primary mr-1 edit-btn' 
                   data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "' 
                   data-tendm='" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "' 
                   data-mota='" . htmlspecialchars($row["mota"], ENT_QUOTES) . "' 
                   data-ngaytao='" . htmlspecialchars($row["ngaytao"], ENT_QUOTES) . "' 
                   data-idnguoitao='" . htmlspecialchars($row["id_nguoitao"], ENT_QUOTES) . "'>Chỉnh sửa</a>
                <a href='#' class='btn btn-sm btn-danger delete-btn' 
                   data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "'>Xóa</a>
              </td>";
        echo "</tr>";
    }
} else {
    echo "<tr><td colspan='6'>Không có danh mục nào.</td></tr>";
}

$conn->close();
?>