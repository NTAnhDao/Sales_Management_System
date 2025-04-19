<?php
    require '../database/db.php';

    $idnv =  $_GET["id_view"];

    $conn = connectDB();

    $sql = "SELECT donhang.*, khachhang.hoten FROM donhang, khachhang WHERE id_khachhang = khachhang.id AND id_nhanvien =" .$idnv;

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $stt = 1;
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" .$stt++. "</td>";
            echo "<td>" . $row["hoten"]. "</td>";
            echo "<td>" . $row["ngaymua"]. "</td>";
            echo "<td>" . $row["tongtien"]. "</td>";
            echo "<td>" . $row["soluongsp"]. "</td>";
            echo "</tr>";
        }
    }
$conn->close();
?>