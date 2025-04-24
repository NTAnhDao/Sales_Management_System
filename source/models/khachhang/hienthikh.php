<?php
    require '../database/db.php';

    $id = $_GET['idlskh'];

    $conn = connectDB();

    $sql = "SELECT * FROM khachhang WHERE id =" .$id;
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo "<b>Khách hàng: </b>" . $row["hoten"]. " - ".$row["sdt"]. " - ".$row["diachi"];
    }
    $conn->close(); 
?>