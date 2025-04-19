<?php
require '../database/db.php';

$conn = connectDB();
$sql = "SELECT danhmucsp.id, danhmucsp.tendm, danhmucsp.mota, danhmucsp.ngaytao, taikhoan.username 
        FROM danhmucsp 
        LEFT JOIN taikhoan ON danhmucsp.id_nguoitao = taikhoan.id";
$result = $conn->query($sql);

$userRole = $_SESSION['role'] ?? 'Nhân viên';

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "'>";
        echo "<td>" . htmlspecialchars($row["id"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "</td>";
        echo "<td>" . (empty($row["mota"]) ? 'Không có mô tả' : htmlspecialchars($row["mota"], ENT_QUOTES)) . "</td>";
        echo "<td>" . htmlspecialchars($row["ngaytao"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["username"] ?? 'Chưa xác định', ENT_QUOTES) . "</td>";
        if ($userRole === 'Admin') {
            echo "<td>";
            echo "<a href='#' class='btn btn-sm btn-primary mr-1 edit-btn' data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "' data-tendm='" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "' data-mota='" . htmlspecialchars($row["mota"], ENT_QUOTES) . "'>Chỉnh sửa</a>";
            echo "<a href='#' class='btn btn-sm btn-danger delete-btn' data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "'>Xóa</a>";
            echo "</td>";
        }
        echo "</tr>";
    }
} else {
    $colspan = ($userRole === 'Admin') ? 6 : 5;
    echo "<tr><td colspan='$colspan'>Không có danh mục nào.</td></tr>";
}

$conn->close();
?>