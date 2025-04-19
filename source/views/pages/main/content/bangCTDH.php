<div class="card-customer">
    <h5>Chi tiết hóa đơn</h5>
    <table class="cart-table">
        <tr>
            <th>STT</th>
            <th>Tên sản phẩm</th>
            <th>Đơn giá</th>
            <th>Số lượng</th>
            <th>Tổng tiền</th>
            <th>Chi tiết</th>
        </tr>
        <tbody id = "ctdh">
        <?php
        require '../database/db.php';

            $id = $_GET['idctdh'];

            $conn = connectDB();

            $sql = "SELECT * FROM sanpham,chitietdonhang WHERE mavach = id_sanpham and id_donhang =" .$id;

            $result = $conn->query($sql);

            if ($result->num_rows > 0) {
                $stt = 1;
                while($row = $result->fetch_assoc()) {
                    echo "<tr>";
                    echo "<td>" . $stt++. "</td>";
                    echo "<td>" . $row["tensp"]. "</td>";
                    echo "<td>" . $row["giaban"]. "</td>";
                    echo "<td>" . $row["soluong"]. "</td>";
                    echo "<td>" . $row["tongtien"]. "</td>";
                    echo "<td>
                            <button class='btn-search' onclick='searchFunction()'>
                                <i class='fa fa-search btn-search'> </i>
                            </button>
                        </td>";
                    echo "</tr>";
                }
            }
        $conn->close();
        ?>

        </tbody>
    </table>
</div>