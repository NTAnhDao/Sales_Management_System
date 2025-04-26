<?php
header("Content-Type: application/json");
require '../../database/db.php';

try {
    if ($_SERVER["REQUEST_METHOD"] === "POST") {
        $sdt = $_POST["sdt"] ?? '';

        $conn = connectDB();

        $query = $conn->prepare("SELECT * FROM khachhang WHERE sdt = ? OR hoten = ?");
        $query->bind_param("ss", $sdt, $sdt);
        $query->execute();
        $result = $query->get_result();

        if ($result->num_rows > 0) {
            $khachhang = $result->fetch_assoc();
            $idkh = $khachhang['id'];

            $stmt_ls = $conn->prepare("SELECT * FROM donhang WHERE id_khachhang = ?");
            $stmt_ls->bind_param("i", $idkh);
            $stmt_ls->execute();
            $result_ls = $stmt_ls->get_result();

            $lichsu = [];
            while ($order = $result_ls->fetch_assoc()) {
                $stmt_chitiet = $conn->prepare("
                    SELECT sp.tensp, ctdh.soluong, ctdh.giaban 
                    FROM chitietdonhang ctdh
                    INNER JOIN sanpham sp ON ctdh.id_sanpham = sp.mavach
                    WHERE ctdh.id_donhang = ?
                ");
                $stmt_chitiet->bind_param("i", $order['id']);
                $stmt_chitiet->execute();
                $result_chitiet = $stmt_chitiet->get_result();

                $chitiet = [];
                while ($row = $result_chitiet->fetch_assoc()) {
                    $chitiet[] = $row;
                }

                $order['order_id'] = $order['id'];
                $order['chitiet'] = $chitiet;
                $lichsu[] = $order;
            }

            echo json_encode([
                'id' => $khachhang['id'],
                'name' => $khachhang['hoten'],
                'address' => $khachhang['diachi'],
                'lichsu' => $lichsu
            ]);
            exit();
        } else {
            echo json_encode(["error" => "Không tìm thấy khách hàng! Vui lòng nhập thông tin khách mới."]);
            exit();
        }
    }
} catch (Exception $e) {
    echo json_encode(["error" => "Lỗi server: " . $e->getMessage()]);
    exit();
}
?>
