<?php
session_start();
require '../../database/db.php';

header('Content-Type: application/json');

if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'Admin') {
    echo json_encode(['success' => false, 'message' => 'Bạn không có quyền thực hiện thao tác này!']);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['id'])) {
    $id = intval($_POST['id']);
    $tendm = $_POST['tendm'] ?? '';
    $mota = $_POST['mota'] ?? '';
    $id_nguoitao = $_SESSION['id'] ?? 0; // Lấy id_nguoitao từ session

    // Kiểm tra dữ liệu đầu vào
    if (empty($tendm)) {
        echo json_encode(['success' => false, 'message' => 'Vui lòng nhập tên danh mục!']);
        exit;
    }

    $conn = connectDB();
    if (!$conn) {
        echo json_encode(['success' => false, 'message' => 'Lỗi kết nối cơ sở dữ liệu!']);
        exit;
    }

    $query = $conn->prepare("UPDATE danhmucsp SET tendm = ?, mota = ?, id_nguoitao = ? WHERE id = ?");
    if (!$query) {
        echo json_encode(['success' => false, 'message' => 'Lỗi chuẩn bị truy vấn: ' . $conn->error]);
        $conn->close();
        exit;
    }

    $query->bind_param("ssii", $tendm, $mota, $id_nguoitao, $id);
    $query->execute();

    if ($query->affected_rows >= 0) {
        $selectQuery = "SELECT danhmucsp.id, danhmucsp.tendm, danhmucsp.mota, danhmucsp.ngaytao, taikhoan.username 
                        FROM danhmucsp 
                        LEFT JOIN taikhoan ON danhmucsp.id_nguoitao = taikhoan.id 
                        WHERE danhmucsp.id = ?";
        $selectStmt = $conn->prepare($selectQuery);
        $selectStmt->bind_param("i", $id);
        $selectStmt->execute();
        $result = $selectStmt->get_result();
        $catalog = $result->fetch_assoc();

        echo json_encode([
            'success' => true,
            'message' => 'Cập nhật danh mục thành công!',
            'id' => $catalog['id'],
            'tendm' => $catalog['tendm'],
            'mota' => $catalog['mota'],
            'ngaytao' => $catalog['ngaytao'],
            'id_nguoitao' => $catalog['username'] ?? 'Chưa xác định'
        ]);
        $selectStmt->close();
    } else {
        echo json_encode(['success' => false, 'message' => 'Cập nhật danh mục thất bại: ' . $query->error]);
    }

    $query->close();
    $conn->close();
} else {
    echo json_encode(['success' => false, 'message' => 'Yêu cầu không hợp lệ!']);
}
?>