
<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Xử lý giao dịch</h3>
            </div>
        </div>
        <div class="content">
            <div>
                <section>
                    <div class="card-tt">
                        <h5>Thêm/quét sản phẩm</h5>
                        <form method="POST" class="form-product">
                            <input id="product_id" type="text" name="product_id" placeholder="Nhập tên hoặc mã sản phẩm">
                            <input id="soluong" type="number" name="quantity" placeholder="Số lượng" min="1" >
                            <button onclick = "return addProduct()" class="btn-custom btn btn-success" name="add_product" type="button" class="btn btn-default">Thêm</button>
                        </form>
                        <div class="d-none errorMessage"></div>
                    </div>
                    
                    <div class="card-tt">
                        <div class="cart-header">
                            <div class="cart-name"><h5>Giỏ hàng</h5></div>
                            <div class="actions">
                                <button type="button" class="btn-custom btn btn-danger">Xoá tất cả</button>
                            </div>
                        </div>
                        <table class="cart-table" border="1">
                            <tr>
                                <th>Tên sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Tổng tiền</th>
                                <th>Hành động</th>
                            </tr>
                            <tbody> </tbody>
                        </table>
                    </div>
                    
                </section>
                <section>
                <div class="card-tt">    
                    <h5>Thông tin khách hàng</h5>
                    <div class="customer">
                        <form method="POST" class="search-form">
                            <label for="customer_phone" style="margin:10px 0;" > Số điện thoại: </label>
                            <input class="sdt" type="text" name="customer_phone" placeholder="Nhập số điện thoại khách hàng" required>
                            <button type="submit" name="search_customer" class="btn-custom btn btn-secondary">Tìm kiếm</button>
                        </form>
                        <div class="customer-infor">Họ tên :</div>
                        <div class="customer-infor">Địa chỉ : </div>
                    </div>
                    
                    <h5>Lịch sử mua hàng</h5>
                    <table class="cart-table" border="1">
                        <tr>
                            <th>Ngày mua</th>
                            <th>Tổng tiền</th>
                            <th>Số tiền khách đưa</th>
                            <th>Số tiền trả lại</th>
                            <th>Số lượng sản phẩm</th>
                            <th>Chi tiết</th>
                        </tr>
                        <tr>
                            <td>2024-03-20</td>
                            <td>500,000 VND</td>
                            <td>600,000 VND</td>
                            <td>100,000 VND</td>
                            <td>5</td>
                            <td><button class="btn-custom btn btn-secondary">Xem</button></td>
                        </tr>
                    </table>
                    </div>
                </section>
                
            </div>
            
    
        </div>
        
    </main>
</div> 