<?php
    header("Content-Type: application/json");
    require '../../database/db.php';
    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $sdt = $_POST["sdt"] ?? '';

        $conn = connectDB();

        $query = $conn->prepare("SELECT * FROM khachhang WHERE sdt = ? or hoten = ?");
        $query->bind_param("ss",$sdt,$sdt);
        $query->execute();
        $result = $query->get_result();


        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode(["id" => $row["id"], "name" => $row["hoten"], "address" => $row["diachi"],"sdt" => $row["sdt"],"ngaytao" => $row["ngaytao"]]);
        } else {
            echo json_encode(["error" => "Không tìm thấy khách hàng! Vui lòng nhập thông tin khách hàng mới."]);
        }
    }
    $conn->close();
?>