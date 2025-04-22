<div class="card-customer">
    <h5>Chi tiết hóa đơn</h5>
    <table class="cart-table">
        <tr>
            <th>STT</th>
            <th>Tên sản phẩm</th>
            <th>Đơn giá</th>
            <th>Số lượng</th>
            <th>Tổng tiền</th>
        </tr>
        <tbody id = "ctdh">
        <?php
        include("../models/donhang/xemCTDH.php");
        ?>
        </tbody>
    </table>
</div>