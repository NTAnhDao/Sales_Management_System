<div class = "main-content " id="main">
    <main>
        <div class="page-header">
            <div class="page-name"><h3>Quản lý nhân viên</h3></div>
        </div>
        <div class="content">
            <section class="listAccount">
                <div class="actions">
                    <button type="button" class="btn btn-success" style="position: absolute; right: 30px;" onclick="chuyengiaodien('listAccount','addAccount')">Tạo tài khoản</button>
                </div>
                <br>
                <div class="employee">
                    <h5>Danh sách nhân viên</h5>
                    <table class="cart-table" border="1">
                        <thread>
                            <tr>
                                <th>Ảnh đại diện</th>
                                <th>Họ tên</th>
                                <th>Trạng thái hoạt động</th>
                                <th>Trạng thái khóa</th>
                                <th>Hành động</th>
                            </tr>
                        </thread>
                        <tbody>
                            <tr>
                                <td><img src="image/user.jpg" alt="Avatar" id="avatar"></td>
                                <td>500,000 VND</td>
                                <td>600,000 VND</td>
                                <td>100,000 VND</td>
                                <td class="action">
                                    <button class="btn-sendEmail">Gửi email</button>
                                    <button class="btn-lock">Khóa</button>
                                    <button class="btn-unlock">Mở khóa</button>
                                    <button class="btn-detail">chi tiết</button>
                                    <button class="btn-viewSale">Xem bán hàng</button>
                                </td>
                            </tr>
                        </tbody>
                        
                    </table>
                </div>
            </section>
            
            <section class="addAccount d-none">
                <div class="container">
                    <h4>Tạo tài khoản</h4>
                    <div class="form">
                        <div class="group">
                            <div class="input-box">
                                <label for="fullname">Họ tên:</label>
                                <input type="text" id="fullname" name="fullname" required>
                            </div>
                            <div class="input-box">
                                <label for="email">Gmail:</label>
                                <input type="email" id="email" name="email" required>
                            </div>
                        </div>
                        <div class="group">
                            <div class="input-box">
                                <label for="gender">Giới tính:</label>
                                <div class="group" style="gap: 10px;">
                                    <input type="radio" id="male" name="gender" value="male">Nam
                                    <input type="radio" id="female" name="gender" value="female">Nữ
                                </div>
                            </div>
                            <div class="input-box">
                                <label for="SDT">Số điện thoại:</label>
                                <input type="tel" id="SDT" name="SDT" pattern="[0-9]{10}">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Tạo</button>
                        <button type="button" class="btn btn-secondary" onclick="chuyengiaodien('addAccount','listAccount')">Quay lại</button>
                    </div>
                    
                </div>

            </section>
        </div>
    </main>
</div>