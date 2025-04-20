<?php 
    include "../database/db.php";

    $conn = connectDB();
    $sql = "SELECT * FROM taikhoan, nhanvien WHERE taikhoan.id_nhanvien=nhanvien.id AND taikhoan.role = 'Nhân viên'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $img = $row["img"];
            $name = $row["hoten"];
            $activated = $row["trangthai_hd"];
            $status_lock = $row["trangthai_khoa"];
    
            if ($activated == 1) {
                $activated = "Đã kích hoạt";
            } else if ($activated == 0) {
                $activated = "Chưa kích hoạt";
            }
    
            if ($status_lock == 0) {
                $status_lock = "Không";
            } else if ($status_lock == 1) {
                $status_lock = "Đã khóa";
            }
            
            echo "<tr>";
            echo "<td><img src='../models/quanlyhoso/imagesNV/" .$img. "' alt='Avatar' id='avatar'></td>";
            echo "<td>" .$name. "</td>";
            echo "<td>" .$activated. "</td>";
            echo "<td>" .$status_lock. "</td>";
            echo "<td class='action'>
                <a class='btn btn-sendEmail' href='index.php?sidebar=quanlynhanvien&token_send=" .$row['activate_token']. "'><i class='fa fa-paper-plane-o'></i></a>
                <a class='btn btn-lock' href='index.php?sidebar=quanlynhanvien&token_lock=" .$row['activate_token']. "'><i class='fa fa-lock'></i></a>
                <a class='btn btn-unlock' href='index.php?sidebar=quanlynhanvien&token_unlock=" .$row['activate_token']. "'><i class='fa fa-unlock'></i></a>
                <a class='btn btn-detail' href='index.php?nav=hoso&id_detail=" .$row['id_nhanvien']. "'><i class='fa fa-info'></i></a>
                <a class='btn btn-viewSale' href='index.php?sidebar=quanlynhanvien&id_view=" .$row['id_nhanvien']. "'><i class='fa fa-vcard-o'></i></a>
            </td>
            </tr>";
        }
    }
    $conn->close();
?>