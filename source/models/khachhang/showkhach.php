<?php
    require '../database/db.php';


    $conn = connectDB();
    $sql = "SELECT * FROM khachhang";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $row["id"]. "</td>";
            echo "<td>" . $row["hoten"]. "</td>";
            echo "<td>" . $row["diachi"]. "</td>";
            echo "<td>" . $row["sdt"]. "</td>";
            echo "<td>" . $row["ngaytao"]. "</td>";
            echo "<td>
            <a class='btn-custom btn btn-secondary' href='index.php?sidebar=khachhang&idlskh=" . $row['id'] . "'>
               Xem
            </a>
          </td>";
            echo "</tr>";
        }
    }

$conn->close();
?>