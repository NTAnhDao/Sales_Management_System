<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Khách hàng </h3>
            </div>
        </div>
        <div class="content">
            <div class="d-flex justify-content-center">
                <label class = "search-text" for="searchBox" > Tìm kiếm khách hàng: </label>
                <div  class="searchBox">
                    <input type="text" placeholder="Search" name="searchBox">
                    <button class=" btn-search" onclick="searchFunction()">
                        <i class="fa fa-search btn-search"> </i>
                    </button>
                </div>
            </div>
            
            <section class="list-customer">
                <div class="card-customer">
                    <h5>Danh sách khách hàng</h5>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Họ tên</th>
                                <th>Địa chỉ</th>
                                <th>Số điện thoại</th>
                                <th>Ngày tạo</th>
                                <th>Lịch sử mua hàng</th>
                            </tr>
                        </thead>
                        <tbody id = "customers-info">
        
                           <?php include 'func/showkhach.php'; ?>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- <section class="detail-bill d-none">
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
            
                <button onclick = " chuyengiaodien('history-customer','detail-bill')" type="button" class="btn-custom btn btn-secondary" class="btn btn-default">
                    <span class = "	fa fa-arrow-left"></span> Quay lại 
                </button>
            </section> -->

            
        </div>
    </main>
</div> 