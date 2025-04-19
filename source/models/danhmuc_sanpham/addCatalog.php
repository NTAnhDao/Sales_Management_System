<?php
session_start();
require '../../database/db.php';

header("Content-Type: application/json");

if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'Admin') {
    echo json_encode(["success" => false, "message" => "Bạn không có quyền thực hiện thao tác này!"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $tendm = $_POST["tendm"] ?? '';
    $mota = $_POST["mota"] ?? '';
    $id_nguoitao = $_SESSION['id'] ?? 0; // Lấy id_nguoitao từ session

    // Kiểm tra dữ liệu đầu vào
    if (empty($tendm)) {
        echo json_encode(["success" => false, "message" => "Vui lòng nhập tên danh mục!"]);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(["success" => false, "message" => "Lỗi kết nối cơ sở dữ liệu!"]);
        exit;
    }

    $query = $conn->prepare("INSERT INTO danhmucsp (id, tendm, mota, ngaytao, id_nguoitao) 
                             VALUES (NULL, ?, ?, CURRENT_TIMESTAMP(), ?)");
    if (!$query) {
        echo json_encode(["success" => false, "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssi", $tendm, $mota, $id_nguoitao);
    $query->execute();

    if ($query->affected_rows > 0) {
        $new_id = $conn->insert_id;
        $selectQuery = "SELECT danhmucsp.id, danhmucsp.tendm, danhmucsp.mota, danhmucsp.ngaytao, taikhoan.username 
                        FROM danhmucsp 
                        LEFT JOIN taikhoan ON danhmucsp.id_nguoitao = taikhoan.id 
                        WHERE danhmucsp.id = ?";
        $selectStmt = $conn->prepare($selectQuery);
        $selectStmt->bind_param("i", $new_id);
        $selectStmt->execute();
        $result = $selectStmt->get_result();
        $catalog = $result->fetch_assoc();

        echo json_encode([
            "success" => true,
            "message" => "Thêm danh mục thành công!",
            "id" => $catalog['id'],
            "tendm" => $catalog['tendm'],
            "mota" => $catalog['mota'],
            "ngaytao" => $catalog['ngaytao'],
            "id_nguoitao" => $catalog['username'] ?? 'Chưa xác định'
        ]);
        $selectStmt->close();
    } else {
        echo json_encode(["success" => false, "message" => "Thêm danh mục thất bại: " . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>