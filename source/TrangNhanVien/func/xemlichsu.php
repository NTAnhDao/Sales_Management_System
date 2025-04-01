<?php
require '../database/db.php';

    $id = $_GET['idlskh'];

    $conn = connectDB();

    $sql = "SELECT * FROM donhang WHERE id_khachhang =" .$id;

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $row["id"]. "</td>";
            echo "<td>" . $row["ngaymua"]. "</td>";
            echo "<td>" . $row["tongtien"]. "</td>";
            echo "<td>" . $row["tienkhachdua"]. "</td>";
            echo "<td>" . $row["tientra"]. "</td>";
            echo "<td>" . $row["soluongsp"]. "</td>";
            echo "<td>
                    <button class='btn-search' onclick='searchFunction()'>
                        <i class='fa fa-search btn-search'> </i>
                    </button>
                </td>";
            echo "</tr>";
        }
    }
$conn->close();
?>