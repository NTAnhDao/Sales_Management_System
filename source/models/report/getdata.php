<?php
    session_start();
    header("Content-Type: application/json"); 
    include "../../database/db.php";

    if($_SERVER["REQUEST_METHOD"] === "POST") {
        $fromDate = $_POST["fromDate"] ?? date('Y-m-01');
        $toDate = $_POST["toDate"] ?? date('Y-m-t');
        $isAdmin = $_SESSION["role"] === "Admin";
        $id = $_SESSION["id"] ?? null;

        $conn = connectDB();

        //khởi tạo giá trị ban đầu
        $response = [
            'success' => true,
            'isAdmin' => $isAdmin,
            'totalAmount' => 0,
            'totalOrders' => 0,
            'totalProducts' => 0,
            'totalProfit' => 0,
            'statistic' => [],
            'orders' => []
        ];

        //tính tổng tiền, đơn hàng, sản phẩm
        $sql = "SELECT 
                    SUM(tongtien) AS totalAmount,
                    COUNT(id) AS totalOrders, 
                    SUM(soluongsp) AS totalProducts
                FROM donhang
                WHERE ngaymua BETWEEN ? AND ?". ($isAdmin ? "" : " AND id_nhanvien = ?");

        $stmt = $conn->prepare($sql);
        if ($isAdmin) {
            $stmt->bind_param("ss", $fromDate, $toDate);
        } else {
            $stmt->bind_param("ssi", $fromDate, $toDate, $id);
        }
        if(!$stmt->execute()) {
            echo json_encode(["success" => false, "mess" => "Lỗi truy vấn, vui lòng thử lại sau"]);
            $stmt->close();
            $conn->close();
            exit();
        } 
        $result = $stmt->get_result();
        if($result->num_rows === 0) {
            echo json_encode(["success" => false, "mess" => "Không có dữ liệu trong khoảng thời gian này"]);
            $stmt->close();
            $conn->close();
            exit();
        }
        if($row = $result->fetch_assoc()) {
            $response['totalAmount'] = (float)($row['totalAmount'] ?? 0);
            $response['totalOrders'] = (int)($row['totalOrders'] ?? 0);
            $response['totalProducts'] = (int)($row['totalProducts'] ?? 0);
        }

        //thống kê theo thời gian để vẽ biểu đồ
        $sql = "SELECT 
                    DATE(ngaymua) AS ngaymua,
                    SUM(tongtien) AS tongtien,
                    COUNT(id) AS sodon,
                    SUM(soluongsp) AS soluongsp
                FROM donhang
                WHERE ngaymua BETWEEN ? AND ?". ($isAdmin ? "" : " AND id_nhanvien = ?")
                . " GROUP BY DATE(ngaymua) ORDER BY DATE(ngaymua) ASC";

        $stmt = $conn->prepare($sql);
        if ($isAdmin) {
            $stmt->bind_param("ss", $fromDate, $toDate);
        } else {
            $stmt->bind_param("ssi", $fromDate, $toDate, $id);
        }

        if(!$stmt->execute()) {
            echo json_encode(["success" => false, "mess" => "Lỗi truy vấn, vui lòng thử lại sau"]);
            $stmt->close();
            $conn->close();
            exit();
        } 
        $result = $stmt->get_result();
        if($result->num_rows === 0) {
            echo json_encode(["success" => false, "mess" => "Không có dữ liệu trong khoảng thời gian này"]);
            $stmt->close();
            $conn->close();
            exit();
        }

        while ($row = $result->fetch_assoc()) {
            $response["statistic"][] = [
                "ngaymua" => $row["ngaymua"],
                "tongtien" => (float) $row["tongtien"],
                "soluongsp" => (int) $row["soluongsp"],
                "sodon" => (int) $row["sodon"],
                "loinhuan" => 0
            ];
        }

        //tính lợi nhuận dành cho admin
        $totalProfit = null;
        if($isAdmin) {
            foreach ($response["statistic"] as &$stat) {
                $sql = "SELECT SUM(ct.soluong * (ct.giaban - sp.giagoc)) AS loinhuan
                        FROM donhang dh, chitietdonhang ct, sanpham sp 
                        WHERE dh.id = ct.id_donhang AND ct.id_sanpham = sp.mavach AND DATE(dh.ngaymua) = ?";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("s", $stat["ngaymua"]);
                $stmt->execute();
                $result = $stmt->get_result();
                if ($row = $result->fetch_assoc()) {
                    $stat["loinhuan"] = (int) $row["loinhuan"];
                    $response["totalProfit"] += (int) $row["loinhuan"];
                }
            }
        }

        //lấy danh sách đơn hàng
        $sql = "SELECT id, ngaymua, tongtien, tienkhachdua, tientra, soluongsp
                FROM donhang
                WHERE ngaymua BETWEEN ? AND ?". ($isAdmin ? "" : " AND id_nhanvien = ?")
                . " ORDER BY ngaymua ASC";
        $stmt = $conn->prepare($sql);
        if ($isAdmin) {
            $stmt->bind_param("ss", $fromDate, $toDate);
        } else {
            $stmt->bind_param("ssi", $fromDate, $toDate, $id);
        }

        if(!$stmt->execute()) {
            echo json_encode(["success" => false, "mess" => "Lỗi truy vấn, vui lòng thử lại sau"]);
            $stmt->close();
            $conn->close();
            exit();
        } 

        $result = $stmt->get_result();
        if($result->num_rows === 0) {
            echo json_encode(["success" => false, "mess" => "Không có dữ liệu trong khoảng thời gian này"]);
            exit();
        }

        
        while ($row = $result->fetch_assoc()) {
            $response["orders"][] = [
                "id" => $row["id"],
                "ngaymua" => $row["ngaymua"],
                "tongtien" => (float) $row["tongtien"],
                "tienkhachdua" => (float) $row["tienkhachdua"],
                "tientra" => (float) $row["tientra"],
                "soluongsp" => (int) $row["soluongsp"]
            ];
        }
    
        echo json_encode($response);
    }
    $conn->close();
?>