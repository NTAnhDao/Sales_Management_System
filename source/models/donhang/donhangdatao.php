<?php
require '../database/db.php';

    $idnv =  $_SESSION["id"];

    $conn = connectDB();

    $sql = "SELECT donhang.*, khachhang.hoten FROM donhang, khachhang WHERE id_khachhang = khachhang.id and id_nhanvien =" .$idnv;

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $stt = 1;
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" .$stt++. "</td>";
            echo "<td>" . $row["hoten"]. "</td>";
            echo "<td>" . $row["tongtien"]. "</td>";
            echo "<td>" . $row["tienkhachdua"]. "</td>";
            echo "<td>" . $row["tientra"]. "</td>";
            echo "<td>" . $row["soluongsp"]. "</td>";
            echo "<td>" . $row["ngaymua"]. "</td>";
            echo "<td>
                    <a class='btn-custom btn btn-secondary' href='index.php?sidebar=donhang&idctdh=" . trim($row['id']) ."'>
                    Xem
                    </a>
                </td>";
            echo "</tr>";
        }
    }
$conn->close();
?>