<?php
require '../database/db.php';

$conn = connectDB();
$sql = "SELECT * FROM danhmucsp";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo "<tr data-id='{$row["id"]}'>"; // Thêm data-id vào <tr>
            echo "<td>" . $row["id"] . "</td>";
            echo "<td>" . $row["tendm"] . "</td>";
            echo "<td>" . $row["mota"] . "</td>";
            echo "<td>" . $row["ngaytao"] . "</td>";
            echo "<td>" . $row["id_nguoitao"] . "</td>";
            echo "<td>
                    <a href='#' class='btn btn-sm btn-primary mr-1 edit-btn' 
                        data-id='" . htmlspecialchars($row["id"], ENT_QUOTES) . "' 
                       data-tendm='" . htmlspecialchars($row["tendm"], ENT_QUOTES) . "' 
                       data-mota='" . htmlspecialchars($row["mota"], ENT_QUOTES) . "' 
                       data-ngaytao='" . $row["ngaytao"] . "' 
                       data-idnguoitao='" . $row["id_nguoitao"] . "'>Chỉnh sửa</a>

                    <a href='#' class='btn btn-sm btn-danger delete-btn' 
                       data-mavach='" . htmlspecialchars($row["mavach"], ENT_QUOTES) . "'>Xóa</a>
                </td>";
        echo "</tr>";
    }
} else {
    echo "<tr><td colspan='6'>Không có danh mục nào.</td></tr>";
}

$conn->close();
?>