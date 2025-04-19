<?php
require '../database/db.php';

$conn = connectDB();
if (!$conn) {
    echo "<tr><td colspan='6'>Lỗi kết nối cơ sở dữ liệu!</td></tr>";
    exit;
}

$sql = "SELECT * FROM danhmucsp";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo "<tr data-id='{$row["id"]}'>"; // Thêm data-id vào <tr>
        echo "<td>" . htmlspecialchars($row["id"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["mota"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["ngaytao"], ENT_QUOTES) . "</td>";
        echo "<td>" . htmlspecialchars($row["id_nguoitao"], ENT_QUOTES) . "</td>";
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