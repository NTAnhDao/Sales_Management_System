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
                    <i class="fa fa-search btn-search"> </i>
                    <input onkeyup="filterTable(this,'customers-info')" type="text" placeholder="Search" name="searchBox">
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
                           <?php include 'modules/khachhang/showkhach.php'; ?>
                        </tbody>
                    </table>
                </div>
            </section>

            
        </div>
    </main>
</div> 
