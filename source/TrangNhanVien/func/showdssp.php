<?php
    require '../database/db.php';


    $conn = connectDB();
    $sql = "SELECT tensp, giabanle, mavach FROM sanpham";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $row["mavach"]. "</td>";
            echo "<td>" . $row["tensp"]. "</td>";
            echo "<td>" . $row["giabanle"]. "</td>";
            echo "<td>
                <a onclick='isAddProduct(this)'><i class='fa fa-shopping-cart addToCart'></i></a>
                </td>";
            echo "</tr>";
        }
    }

$conn->close();
?>