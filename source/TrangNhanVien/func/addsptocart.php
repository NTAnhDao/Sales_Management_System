<?php
header("Content-Type: application/json");
require '../../database/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $name = $_POST["name"] ?? "";
    $soluong = intval($_POST["sl"] ?? 1);

    $conn = connectDB();

    $query = $conn->prepare("SELECT * FROM sanpham WHERE tensp = ? or mavach= ?");
    $query->bind_param("ss",$name,$name);
    $query->execute();
    $result = $query->get_result();


    if ($row = $result->fetch_assoc()) {
    $response = [
        "tensp" => $row["tensp"],
        "giabanle" => $row["giabanle"],
        "soluong" => $soluong,
        "tongtien" => $soluong * $row["giabanle"]
    ];
    echo json_encode($response);
    } else {
        echo json_encode(["error" => "Sản phẩm không tồn tại"]);
    }
}
$conn->close();
?>