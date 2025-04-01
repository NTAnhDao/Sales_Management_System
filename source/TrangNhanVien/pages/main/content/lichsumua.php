<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Khách hàng </h3>
            </div>
        </div>
        <div class="content">
            <div class="d-flex justify-content-center">
                <label class = "search-text" for="searchBox" > Tìm kiếm đơn hàng: </label>
                <div  class="searchBox">
                    <input type="text" placeholder="Search" name="searchBox">
                    <button class=" btn-search" onclick="searchFunction()">
                        <i class="fa fa-search btn-search"> </i>
                    </button>
                </div>
            </div>
            <section class="history-customer">
                <div class="card-customer">
                    <h5>Lịch sử mua hàng</h5>
                    <table class="cart-table" >
                        <tr>
                            <th>STT</th>
                            <th>Ngày mua</th>
                            <th>Tổng tiền</th>
                            <th>Số tiền khách đưa</th>
                            <th>Số tiền trả lại</th>
                            <th>Số lượng sản phẩm</th>
                            <th>Chi tiết</th>
                        </tr>
                        <tbody id = "customers-info">
            
                            <?php include 'func/xemlichsu.php'; ?>

                        </tbody>
                    </table>
                </div>
                <a class='btn-custom btn btn-secondary' href='index.php?sidebar=khachhang'>
                    <span class = "	fa fa-arrow-left"></span> Quay lại 
                </a>
            </section>

            <section class="detail-bill d-none">
                <div class="card-customer">
                    <h5>Chi tiết hóa đơn</h5>
                    <table class="cart-table" border="1">
                        <tr>
                            <th>Tên sản phẩm</th>
                            <th>Đơn giá</th>
                            <th>Số lượng</th>
                            <th>Tổng tiền</th>
                            <th>Chi tiết</th>
                        </tr>
                        <tr>
                            <td>2024-03-20</td>
                            <td>500,000 VND</td>
                            <td>600,000 VND</td>
                            <td>100,000 VND</td>
                            <td>5</td>
                        </tr>
                    </table>
                </div>
            
                <a class='btn-custom btn btn-secondary' href='index.php?sidebar=khachhang'>
                    <span class = "	fa fa-arrow-left"></span> Quay lại 
                </a>
            </section>

            
        </div>
       
    </main>
</div> 