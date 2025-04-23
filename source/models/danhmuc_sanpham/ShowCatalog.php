<?php


require '../database/db.php';
include_once('permission.php');

if (!isset($_SESSION)) {
    session_start();
}

$conn = connectDB();

// Sử dụng JOIN để lấy username từ bảng `taikhoan`
$sql = "SELECT danhmucsp.id, danhmucsp.tendm, danhmucsp.mota, danhmucsp.ngaytao, taikhoan.username 
        FROM danhmucsp 
        LEFT JOIN taikhoan ON danhmucsp.id_nguoitao = taikhoan.id";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-id='{$row["id"]}'>"; // Thêm data-id vào <tr>
        echo "<td>" . $row["id"] . "</td>";
        echo "<td>" . $row["tendm"] . "</td>";
        echo "<td>" . $row["mota"] . "</td>";
        echo "<td>" . $row["ngaytao"] . "</td>";
        echo "<td>" . $row["username"] . "</td>"; // Hiển thị username thay vì id_nguoitao

        // Chỉ hiển thị cột hành động nếu có quyền quản lý danh mục
        if (canManageCategories()) {
            echo "<td>
                    <a href='#' class='btn btn-sm btn-primary edit-btn' 
                        data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "' 
                        data-tendm='" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "' 
                        data-mota='" . htmlspecialchars($row["mota"], ENT_QUOTES) . "' 
                        data-ngaytao='" . $row["ngaytao"] . "'>Chỉnh sửa</a>

                    <a href='#' class='btn btn-sm btn-danger delete-btn' 
                        data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "'>Xóa</a>
                </td>";
        }
        echo "</tr>";
    }
} else {
    $colspan = canManageCategories() ? 6 : 5;
    echo "<tr><td colspan='$colspan'>Không có danh mục nào.</td></tr>";
}

$conn->close();
?>