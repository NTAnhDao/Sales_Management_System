<?php
    require '../database/db.php';

    $id = $_GET['idctdh'];

    $conn = connectDB();

    $sql = "SELECT * FROM sanpham,chitietdonhang WHERE mavach = id_sanpham and id_donhang =" .$id;

    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $stt = 1;
        while($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $stt++. "</td>";
            echo "<td>" . $row["tensp"]. "</td>";
            echo "<td>" . $row["giaban"]. "</td>";
            echo "<td>" . $row["soluong"]. "</td>";
            echo "<td>" . $row["tongtien"]. "</td>";
            echo "</tr>";
        }
    }
    $conn->close();
?>
