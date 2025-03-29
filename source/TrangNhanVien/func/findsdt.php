<?php
header("Content-Type: application/json");
require '../../database/db.php';
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $sdt = $_POST["sdt"] ?? '';

    if (empty($sdt)) {
        echo json_encode(["error" => "Vui lòng nhập số điện thoại!"]);
        exit();
    }
    $conn = connectDB();

    $query = $conn->prepare("SELECT * FROM khachhang WHERE sdt = ?");
    $query->bind_param("s",$sdt);
    $query->execute();
    $result = $query->get_result();


    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo json_encode(["name" => $row["hoten"], "address" => $row["diachi"]]);
    } else {
        echo json_encode(["error" => "Không tìm thấy khách hàng! Vui lòng thêm thông tin khách hàng mới."]);
    }
}
$conn->close();
?>