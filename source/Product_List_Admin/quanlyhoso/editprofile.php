<?php

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $fullname = trim($_POST['fullname'] ?? "");
    $email = trim($_POST['email'] ?? "");
    $address = trim($_POST['address'] ?? "");
    $phone = trim($_POST['phone'] ?? "");
    $gender = trim($_POST['gender'] ?? "");
    $dob = trim($_POST['dob'] ?? "");
    if (empty($dob)) {
        $dob = null; 
    }
    if (trim($fullname) === "" || trim($email) === "") {
    }else{
        $conn = connectDB();

        $stmt = $conn->prepare("UPDATE nhanvien SET hoten = ?, email = ?, diachi = ?, sdt = ?, gioitinh = ?, ngaysinh = ? WHERE id = ?");
        $stmt->bind_param("ssssssi", $fullname, $email, $address, $phone, $gender, $dob, $_SESSION["id"]);
        $stmt->execute();

        $stmt->close();
        $conn->close();
    }
}

?>
