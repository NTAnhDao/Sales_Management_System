<div class = "main-content " id="main">
    <main>
        <div class ="page-header">
            <h3>Lịch sử bán hàng của nhân viên</h3>
        </div>
        <div class="content">
            <session class="history">
                <div class="history-sale">
                    <table class="cart-table" border="1">
                        <thread>
                            <tr>
                                <th>STT</th>
                                <th>Khách hàng</th>
                                <th>Ngày mua</th>
                                <th>Tổng tiền</th>
                                <th>Số lượng sản phẩm</th>
                            </tr>
                        </thread>
                        <tbody>
                            <?php include 'functionQLNV/ttbanhang.php'; ?>
                        </tbody>
                    </table>
                </div>
                <a class='btn-custom btn btn-secondary' href='index.php?sidebar=quanlynhanvien'>
                    <span class = "fa fa-arrow-left"></span> Quay lại 
                </a>
            </session>
        </div>
        
    </main>
</div> 