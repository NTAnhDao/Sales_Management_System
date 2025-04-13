<?php
    session_start();
    header("Content-Type: application/json");
    require '../../../database/db.php';

    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $tienkhachdua = intval($_POST["tienkhachdua"] ?? 0);
        $tong = intval($_POST["tong"] ?? 0);
        $slsp = intval($_POST["slsp"] ?? 0);

        $tientra =  $tienkhachdua-$tong;
        $sdt = $_POST["sdt"] ?? '';

        $conn = connectDB();

        // Tìm id khách hàng qua số điện thoại
        $sql = $conn->prepare("SELECT id FROM khachhang WHERE sdt = ?");
        $sql->bind_param("s", $sdt);
        $sql->execute();
        $result = $sql->get_result();
        $idkh = null;
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $idkh = $row["id"];
        } else {
            echo json_encode(["error" => "Không tìm thấy khách hàng với số điện thoại này"]);
            $result->free();
            $sql->close();
            $conn->close();
            exit;
        }
        $result->free();
        $sql->close();

        $query = $conn->prepare("INSERT INTO `donhang` (`id`, `id_khachhang`, `id_nhanvien`, `tongtien`, `tienkhachdua`,`tientra`, `ngaymua`, `soluongsp`) VALUES (NULL, ?, ?, ?, ?, ?, current_timestamp(),?);");
        $query->bind_param("ssssss", $idkh, $_SESSION["id"], $tong, $tienkhachdua, $tientra, $slsp);

        if ($query->execute()) {
            $last_id = $conn->insert_id;
            $_SESSION["idhd"] = $last_id;
        
            echo json_encode([
                "idhd" => $last_id,
                "tienthoi" => $tientra
            ]);
        } else {
            echo json_encode([
                "error" => "Không thể thêm đơn hàng"
            ]);
        }
        
        $query->close(); 
        $conn->close();
    } 
    $donhang = isset($_COOKIE["donhangtoday"]) ? $_COOKIE["donhangtoday"] + 1 : 1;
    setcookie("donhangtoday", $donhang, $_SESSION["1day"], "/");

    $doanhthu = isset($_COOKIE["doanhthutoday"]) ? $_COOKIE["doanhthutoday"] + $tong : $tong;
    setcookie("doanhthutoday", $doanhthu, $_SESSION["1day"], "/");


?>