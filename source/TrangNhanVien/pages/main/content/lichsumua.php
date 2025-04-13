<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <a  href="index.php?sidebar=khachhang">
                <h3>Khách hàng </h3>
            </a>
            <h3> \ Lịch sử mua hàng </h3>
        </div>
        <div class="content">
            <div class="d-flex justify-content-center">
                <label class = "search-text" for="searchBox" > Tìm kiếm đơn hàng: </label>
                <div  class="searchBox">
                    <i class="fa fa-search btn-search"> </i>
                    <input onkeyup="filterTable(this,'lichsumua')" type="text" placeholder="Nhập ngày mua" name="searchBox">
                </div>
            </div>
            <section class="history-customer">
                <div class="customer">
                    <?php include("modules/khachhang/hienthikh.php"); ?>
                </div>
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
                        <tbody id = "lichsumua">
            
                            <?php include 'modules/khachhang/xemlichsu.php'; ?>

                        </tbody>
                    </table>
                </div>
                <a class='btn-custom btn btn-secondary' href='index.php?sidebar=khachhang'>
                    <span class = "	fa fa-arrow-left"></span> Quay lại 
                </a>
            </section>

            
        </div>
       
    </main>
</div> 