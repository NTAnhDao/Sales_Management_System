<?php
    require '../database/db.php';

    $conn = connectDB();
    $sql = "SELECT * FROM sanpham, danhmucsp WHERE sanpham.id_danhmuc = danhmucsp.id";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<tr data-mavach='{$row["mavach"]}'>";
                echo "<td>" . $row["mavach"]. "</td>";
                echo "<td><img src='image/" . htmlspecialchars($row["img"]) . "' width='80' height='80'></td>";
                echo "<td>" . $row["tensp"]. "</td>";
                echo "<td>" . $row["giagoc"]. "</td>";
                echo "<td>" . $row["giabanle"]. "</td>";
                echo "<td>" . $row["id_danhmuc"]. "</td>";
                echo "<td>" . $row["ngaytao"]. "</td>";
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