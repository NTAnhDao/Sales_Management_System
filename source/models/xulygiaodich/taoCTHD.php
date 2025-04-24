<?php
    session_start();
    
    require '../../database/db.php';
    $data = json_decode(file_get_contents("php://input"), true);

    if ($data) {
        $conn = connectDB();
        // Tìm id, giabanle của sản phẩm trong hóa đơnđơn
        $sql = $conn->prepare("SELECT mavach, giabanle FROM sanpham WHERE tensp = ?");

        // Thêm vào chi tiết hóa đơn 
        $query = $conn->prepare("INSERT INTO `chitietdonhang` (`id_donhang`, `id_sanpham`, `soluong`, `tongtien`, `giaban`) VALUES (?, ?, ?, ?, ?)");

        foreach ($data as $item) {
            $tensp =  $item['tensp'];
            $sl = $item['slsp'];

            $sql->bind_param("s", $tensp);
            $sql->execute();
            $result = $sql->get_result();

            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $idsp = $row["mavach"];
                $giaban = $row["giabanle"];
            } else {
                echo "Lỗi: Không tìm thấy sản phẩm để thêm vào đơn hàng.";
                $result->free();
                $sql->close();
                $conn->close();
                exit;
            }

            $tongtiensp = $giaban * $sl;
            $query->bind_param("sssss", $_SESSION["idhd"] , $idsp, $sl, $tongtiensp, $giaban);

            if (!$query->execute()) {
                echo "Lỗi: Không thể thêm chi tiết vào hóa đơn.";
                $sql->close();
                $conn->close();
                exit;
            }
        }
        echo "Đơn hàng đã được tạo thành công!";
        $result->free();
        $sql->close();
        $conn->close();
    } else {
        echo "Lỗi: Không có dữ liệu!";
    }
?>