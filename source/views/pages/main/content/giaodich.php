<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Xử lý giao dịch</h3>
            </div>
            <div>
                <a href="" class="btn btn-success" style="position: absolute; right: 30px; color : white;" ><i class="fa fa-plus"></i> Giao dịch</a>
            </div>
        </div>
        <div class="content">
            <section class="cart">
                <div class="d-flex justify-content-center">
                    <label class = "search-text" for="searchBox" > Tìm kiếm sản phẩm: </label>
                    <div  class="searchBox">
                        <i class="fa fa-search btn-search"> </i>
                        <input onkeyup="filterTable(this,'danhsachSP')" type="text" placeholder="Nhập tên sản phẩm" name="searchBox">
                    </div>
                </div>
                <div class="row">
                    <div class="cart-product col-lg-6 col-sm-12">
                        <div class="card-cart list-product">
                            <div class="cart-header">
                                <div class="cart-name"><h5>Danh sách sản phẩm</h5></div>
                            </div>
                            <table class="cart-table">
                                <tr>
                                    <th>Mã vạch</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Hành động</th>
                                </tr>
                                <tbody id ="danhsachSP">
                                    <?php include '../models/xulygiaodich/showdssp.php'; ?>
                                </tbody>
                            </table>
                        </div>
                    </div>


                    <div class="col-lg-6 col-sm-12">
                        <div class="card-cart ">
                            <h5>Thêm/quét sản phẩm</h5>
                            <div class="form-product">
                                <input id="product_id" type="text" name="product_id" placeholder="Nhập mã sản phẩm">
                                <input id="soluong" type="number" name="soluong" placeholder="Số lượng" min="1" >
                                <button class="btn-custom btn btn-success" id="addbyID" onclick="isAddProduct(this)"  class="btn btn-default">Thêm</button>
                            </div> 
                            
                            <div class="d-none errorMessage" id="err-addsp"></div>
                        </div>
                    
                        <div class="card-cart list-cart">
                            <div class="cart-header">
                                <div class="cart-name"><h5>Giỏ hàng</h5></div>
                                <div class="actions">
                                    <button  onclick="removeallProduct()" type="button" class="btn-custom btn btn-danger">Xoá tất cả</button>
                                </div>
                            </div>
                            <table class="cart-table">
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Tổng tiền</th>
                                    <th>Hành động</th>
                                </tr>
                                <tbody id="tbody"> </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="cart-submit">
                    <div>
                        <h5 >Tổng tiền:  <span class="total">0</span> VNĐ</h5>
                    </div>
                    <button id = "nextprocess" onclick = "chuyengiaodien('cart','purchase')" type="button" class="btn-custom btn btn-secondary" class="btn btn-default">
                        <span class = "fa fa-arrow-right"> </span> Tiếp tục
                    </button>
                </div> 
            </section>

            <section class="purchase d-none">
                <div class="row">
                    <div class="col-lg-6 col-sm-12">
                        <div class="card-cart">
                            <h5>Thông tin khách hàng</h5>
                            <div class="purchase-item">
                                <div class="form-product row">
                                    <label for="customer-phone" style="margin:10px 0;" > Số điện thoại: </label>
                                    <input class="sdt" oninput="this.value = this.value.replace(/[^0-9]/g, '')" type="text" id="customer-phone" name="customer-phone" placeholder="Nhập số điện thoại">
                                    <button id="search-sdt"  class="btn-custom btn btn-secondary" >Tìm kiếm</button>
                                </div>
                                <div class="d-none errorMessage" id="err-findsdt"></div>
                            </div>
                        </div>
                        
                        <!-- Nếu là khách mới -->
                        <div class="modal" id="nhapcustomer">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Nhập thông tin khách hàng</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="annouce">
                                            Nhập đầy đủ và chính xác thông tin của khách hàng trước khi thoát.
                                        </div>
                                        <div>
                                            <label for="name" style="margin:10px 0;" > Tên khách hàng: </label>
                                            <input id="enter-name" type="text" name="name" placeholder="Nhập tên khách hàng">
                                        </div>
                                        <div>
                                            <label for="name" style="margin:10px 0;" > Địa chỉ: </label>
                                            <input id="enter-diachi" type="text" name="diachi" placeholder="Nhập địa chỉ"  >
                                        </div>
                                        <div class="d-none errorMessage" id="err-addkh"></div>
                                    </div>
                                    <div class="modal-footer">
                                        <button onclick="checkData()" class="btn btn-success">Hoàn tất</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card-cart">
                            <h5>Thông tin giao dịch </h5>
                            <div class="purchase-item">
                                <label for="tienkhachdua" style="margin:10px 0;" > Tiền khách đưa: </label>
                                <input id="tienkhachdua" oninput="this.value = this.value.replace(/[^0-9]/g, '')"  type="text" name="tienkhachdua" placeholder="Nhập số tiền" required>
                                <div class="customer-infor">Tổng sản phẩm :
                                    <span id="tongsp"></span>
                                </div>
                                <div class="customer-infor">Tổng tiền :
                                    <span class="total"></span>
                                </div>
                                <div class="d-none errorMessage" id="err-tien"></div>
                            </div>
                        </div>
                        
                        <div class="cart-submit">
                            <button onclick = "chuyengiaodien('cart','purchase')" type="button" class="btn-custom btn btn-secondary" class="btn btn-default">
                                <span class = "	fa fa-arrow-left"></span> Quay lại 
                            </button>
                            <button type="button" id = "btnthanhtoan" class="btn-custom btn btn-success" >
                                <span class = "fa fa-shopping-cart"></span> Thanh toán
                            </button>
                        </div> 
                    </div>

                    <div class="col-lg-6 col-sm-12">
                        <div class = "card-history">
                            <!-- Nếu là khách cũ -->
                            <h5>Lịch sử mua hàng</h5>
                            <div class="customer-infor" id="hoten"> </div>
                            <div class="customer-infor" id="diachi"> </div>
                            <table class="cart-table">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Ngày mua</th>
                                        <th>Tổng tiền</th>
                                        <th>Số tiền khách đưa</th>
                                        <th>Số tiền trả lại</th>
                                        <th>Số lượng sản phẩm</th>
                                        <th>Chi tiết</th>
                                    </tr>
                                </thead>
                                <tbody id="lichsumua">
                                </tbody>
                            </table>
                        </div>
                        <div class="modal fade" id="modalChiTiet" tabindex="-1" aria-labelledby="modalChiTietLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="modalChiTietLabel">Chi tiết đơn hàng</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                </div>
                                <div class="modal-body" id="chitiet-container"> </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
                        
            
            <section class="bill d-none">
                <div id="HoaDon">
                    <div>
                        <div class="text-center">
                            <b> HÓA ĐƠN THANH TOÁN </b>
                            <div> Số HD: <span id="sohd"></span></div>
                            <div class = "hd-inf">
                                <div>Thu ngân: <?=$_SESSION["tennv"] ?> </div>
                                <div>Ngày: <span id = "day"></span></div>
                            </div>
                        </div>
                        <div class = "kh-inf">
                            <div>Tên khách hàng: 
                                <span id ="tenkh"> </span>
                            </div>
                            <div>Số điện thoại: 
                                <span id ="sdtkh"> </span>
                            </div>
                        </div>
                        <table class="cart-table">
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Tổng tiền</th>
                            </tr>
                            <tbody id="tbodyhd"> </tbody>
                        </table>
                        <div>
                            <div class="cal-parts" >
                            <b>Thành tiền: </b>
                            <p class ="total">0</p>
                            </div>

                            <div class = "hd-inf">
                                <div class="cal-parts">
                                    <div>Thanh toán(tiền mặt): </div>
                                    <div class="total"></div>
                                </div>
                                <div class="cal-parts">
                                    <div>Tiền khách đưa: </div>
                                    <div id="tiendua"></div>
                                </div>
                                <div class="cal-parts">
                                    <div>Tiền thối:</div>
                                    <div id="tienthoi"></div>
                                </div>
                            </div>
                        </div>
                        <div class="thanks">
                            Cảm ơn và hẹn gặp lại
                        </div>
                    </div>
                </div>
                
                
            
                <div class="cart-submit right-actions">
                    <button onclick="printDiv('HoaDon')" id = "btninbill" type="button"  class="btn-custom btn btn-success" >
                        <span class = "fa fa-print"></span> In hóa đơn
                    </button>
                </div> 
            </section>
        </div>
    </main>
</div> 
<script>
    function printDiv(divId) {
        window.print(); // Kết hợp với CSS @media print để chỉ in phần được chọn
    }
</script>
<script src="../models/xulygiaodich/script.js" ></script> 
