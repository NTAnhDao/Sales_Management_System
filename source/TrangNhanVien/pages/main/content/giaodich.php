<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Xử lý giao dịch</h3>
            </div>
        </div>
        <div class="content">
                <section class="cart">
                    <div class="row">
                        <div class="cart-product col-lg-6 col-sm-12">
                            <div class="card-cart list-product">
                                <div class="cart-header">
                                    <div class="cart-name"><h5>Danh sách sản phẩm</h5></div>
                                </div>
                                <table class="cart-table">
                                    <tr>
                                        <th>Tên sản phẩm</th>
                                        <th>Đơn giá</th>
                                        <th>Số lượng</th>
                                        <th>Hành động</th>
                                    </tr>
                                </table>
                            </div>
                        </div>


                        <div class="col-lg-6 col-sm-12">
                            <div class="card-cart ">
                                <h5>Thêm/quét sản phẩm</h5>
                                <div class="form-product">
                                    <input id="product_id" type="text" name="product_id" placeholder="Nhập tên hoặc mã sản phẩm">
                                    <input id="soluong" type="number" name="soluong" placeholder="Số lượng" min="1" >
                                    <button class="btn-custom btn btn-success" id="add_product"  class="btn btn-default">Thêm</button>
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
                            <h5 >Tổng tiền: <span id = "total">0</span> VNĐ</h5>
                        </div>
                        <button onclick = " chuyengiaodien('cart','purchase')" type="button" class="btn-custom btn btn-secondary" class="btn btn-default">
                            <span class = "fa fa-arrow-right"> </span> Tiếp tục
                        </button>
                    </div> 

                </section>

                <section class="purchase d-none" >
                    <div class="card-cart">
                        <h5>Thông tin khách hàng</h5>
                        <div class="purchase-item">
                            <div class="form-product row">
                                    <label for="customer-phone" style="margin:10px 0;" > Số điện thoại: </label>
                                    <input class="sdt" type="text" id="customer-phone" name="customer-phone" placeholder="Nhập số điện thoại">
                                    <button id="search-sdt"  class="btn-custom btn btn-secondary" >Tìm kiếm</button>
                            </div>
                            <div class="customer-infor" id="hoten"> </div>
                            <div class="customer-infor" id="diachi"> </div>
                            <div class="d-none errorMessage" id="err-findsdt"></div>
                            <div class="d-none successMessage" id="suc-addkh"></div>

                        </div>

                        <!-- The Modal -->
                        <div class="modal" id="nhapcustomer">
                            <div class="modal-dialog">
                                <div class="modal-content">

                                    <!-- Modal Header -->
                                    <div class="modal-header">
                                        <h4 class="modal-title">Nhập thông tin khách hàng</h4>
                                    </div>

                                    <!-- Modal body -->
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

                                    <!-- Modal footer -->
                                    <div class="modal-footer">
                                        <button onclick="checkData()" class="btn btn-success">Hoàn tất</button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-cart">
                        <h5>Thông tin giao dịch </h5>
                        <div class="purchase-item">
                            <form method="POST" class="search-form">
                                <label for="customer_phone" style="margin:10px 0;" > Tiền khách đưa: </label>
                                <input class="sdt" type="text" name="customer_phone" placeholder="Nhập số tiền" required>
                            </form>
                            <div class="customer-infor">Tổng tiền :</div>
                            <div class="customer-infor">Tiền thối : </div>
                        </div>
                    </div>
                    
                    <div class="cart-submit">
                        <button onclick = "chuyengiaodien('cart','purchase')" type="button" class="btn-custom btn btn-secondary" class="btn btn-default">
                            <span class = "	fa fa-arrow-left"></span> Quay lại 
                        </button>
                        <button onclick="insertData()" type="button" id = "btnthanhtoan" class="btn-custom btn btn-success" >
                            <span class = "fa fa-shopping-cart"></span> Thanh toán
                        </button>
                    </div> 
                </section>
                         
                
                <section class="bill d-none" >
                    <div class="HoaDon">
                        <div style="text-align: center;">
                            <b> HÓA ĐƠN THANH TOÁN </b>
                            <div> Số HD: </div>
                            <div class = "hd-inf">
                                <div>Thu ngân: </div>
                                <div>Ngày: </div>
                            </div>
                        </div>
                        <div class = "kh-inf">
                               
                            <div class=" d-flex">
                            <div>Tên khách hàng: </div>
                            <p id ="tenkh"> </p>
                            </div>
                            <div class=" d-flex">
                            <div>Số điện thoại: </div>
                            <p id ="sdtkh"> </p>
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
                            <div style="display: flex; justify-content: space-between;">
                            <b>Thành tiền: </b>
                            <p id ="tong">0</p>
                            </div>
                            <div class = "hd-inf d-none">
                            <div>Thuế VAT(0%)</div>
                            <div>Thanh toán(chuyển khoản):</div>
                            </div>
                            <div class = "hd-inf">
                            <div>Thuế VAT(0%)</div>
                            <div>Thanh toán(tiền mặt):</div>
                            <div>Tiền khách đưa: </div>
                            <div>Tiền thối:</div>
                            </div>
                        </div>
                    </div>
                    
                    
              
                    <div class="cart-submit">
                        <button onclick = " chuyengiaodien('cart','bill')" id = "btninbill" type="button"  class="btn-custom btn btn-success" >
                            <span class = "fa fa-print"></span> In hóa đơn
                        </button>
                    </div> 
                </section>
        </div>
    </main>
</div> 
<script src="func/script.js" ></script> 
