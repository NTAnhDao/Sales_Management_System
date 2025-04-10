<?php
require '../database/db.php';

    $id = $_GET['idlskh'];

    $conn = connectDB();

    $sql = "SELECT * FROM donhang WHERE id_khachhang =" .$id;

    $result = $conn->query($sql);
    $stt = 1;
    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $stt++ . "</td>";
            echo "<td>" . $row["ngaymua"]. "</td>";
            echo "<td>" . $row["tongtien"]. "</td>";
            echo "<td>" . $row["tienkhachdua"]. "</td>";
            echo "<td>" . $row["tientra"]. "</td>";
            echo "<td>" . $row["soluongsp"]. "</td>";
            echo "<td>
            <a class='btn-custom btn btn-secondary' href='index.php?sidebar=khachhang&idlskh=".$id."&idctdh=" . $row['id'] . "'>
                Xem
            </a>
            </td>";
            echo "</tr>";
        }
    }
$conn->close();
?>