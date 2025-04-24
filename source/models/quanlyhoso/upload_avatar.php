<?php
    session_start();
    require '../../database/db.php';

    $avatarPath = '';
    $avatarTmp = '';
    $avatarName = '';
    $uploadOk = false;
    
    if (isset($_FILES['avatar']) && $_FILES['avatar']['error'] === UPLOAD_ERR_OK) {
        $uploadDir = "imagesNV/"; 
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0755, true);
        }
    
        $avatarTmp = $_FILES['avatar']['tmp_name'];
        $avatarName = $_FILES['avatar']['name'];
        $avatarName = time().'_'.$avatarName;
        $avatarPath = $uploadDir . $avatarName;
    
        // Kiểm tra loại file
        $allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];
        $fileType = mime_content_type($avatarTmp);
        $fileExt = strtolower(pathinfo($avatarName, PATHINFO_EXTENSION));
    
        if (in_array($fileType, $allowedTypes) && in_array($fileExt, ['jpg', 'jpeg', 'png', 'webp'])) {
            if (move_uploaded_file($avatarTmp, $avatarPath)) {
                $uploadOk = true;
            }
        }
    
        if ($uploadOk) {
            $conn = connectDB();
            $stmt = $conn->prepare("UPDATE nhanvien SET img = ? WHERE id = ?");
            $stmt->bind_param("ss", $avatarName, $_SESSION["id"]);
            $stmt->execute();
            $stmt->close();
            $conn->close();
        }   
    } 
    header("Location: ../../views/index.php?nav=hoso&query=sua");
    exit();
?>