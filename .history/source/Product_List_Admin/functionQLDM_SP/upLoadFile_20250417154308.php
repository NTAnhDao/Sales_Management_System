<?php
header("Content-Type: application/json");

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    if (isset($_FILES['file-upload']) && $_FILES['file-upload']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['file-upload']['tmp_name'];
        $fileName = $_FILES['file-upload']['name'];
        $fileExt = pathinfo($fileName, PATHINFO_EXTENSION);
        $newFileName = uniqid() . '.' . $fileExt;
        $uploadDir = 'image/';

        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0777, true);
        }

        $destPath = $uploadDir . $newFileName;

        if (move_uploaded_file($fileTmpPath, $destPath)) {
            echo json_encode([
                "success" => true,
                "message" => "Upload file thành công!",
                "fileName" => $newFileName
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "Lỗi khi lưu file hình ảnh!"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Vui lòng chọn một file để upload!"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Yêu cầu không hợp lệ!"]);
}
?>