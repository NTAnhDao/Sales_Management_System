<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <h3>Đơn hàng </h3>
        </div>
        <div class="content">
            <div class="d-flex justify-content-center">
                <label class = "search-text" for="searchBox" > Tìm kiếm đơn hàng: </label>
                <div  class="searchBox">
                    <i class="fa fa-search btn-search"> </i>
                    <input onkeyup="filterTable(this,'donhang')" type="text" placeholder="Search" name="searchBox">
                </div>
            </div>               
            
            <section class="detail-bill">
            <div class="card-customer">
                    <h5>Đơn hàng đã tạo</h5>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Họ tên</th>
                                <th>Tổng tiền</th>
                                <th>Tiền khách đưa</th>
                                <th>Tiền trả</th>
                                <th>Số lượng</th>
                                <th>Ngày mua</th>
                                <th>Chi tiết</th>
                            </tr>
                        </thead>
                        <tbody id = "donhang">
                            <?php include("modules/donhang/donhangdatao.php"); ?>
                        </tbody>
                    </table>
                </div>
            </section>

            
        </div>
       
    </main>
</div> 