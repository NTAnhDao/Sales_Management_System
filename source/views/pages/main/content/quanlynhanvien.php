<?php 
    include "../models/nhanvien/actions.php";
?>
<div class = "main-content " id="main">
    <main>
        <div class="page-header">
            <div class="page-name"><h3>Quản lý nhân viên</h3></div>
        </div>
        <div class="content">
            <section class="listAccount">
                <div class="actions">
                    <button type="button" class="btn btn-success" style="position: absolute; right: 30px;" onclick="chuyengiaodien('listAccount','addAccount')"><i class="fa fa-plus"></i> Tạo tài khoản</button>
                </div>
                <br>
                <div class="employee">
                    <h5>Danh sách nhân viên</h5>
                    <table class="cart-table">
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
                        <?php 
                            include "../models/nhanvien/DsNV.php";
                        ?>
                        </tbody>
                        
                    </table>
                </div>
            </section>
            <section class="addAccount d-none">
                <div class="form-container">
                    <div class="text-center mb-4" id="form-title">
                        <h3>Tạo tài khoản</h3>
                    </div>
                    <form action="" method="POST">
                        <div class="form-group">
                            <div class="input-box">
                                <label for="fullname">Họ tên:</label>
                                <input type="text" id="fullname" name="fullname" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-box">
                                <label for="email">Gmail:</label>
                                <input type="email" id="emailNV" name="email" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-box">
                                <label for="gender">Giới tính:</label>
                                <div class="radio-group">
                                    <div class="radio-option"><input type="radio" id="male" name="gender" value="male"> Nam</div>
                                    <div class="radio-option"><input type="radio" id="female" name="gender" value="female"> Nữ</div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-box">
                                <label for="SDT">Số điện thoại:</label>
                                <input type="tel" id="SDT" name="SDT" pattern="[0-9]{10}">
                            </div>
                        </div>
                        <div class="right-actions">
                            <button type="submit" class="btn btn-primary edit-btn" id="add-account">Tạo</button>
                            <button type="button" class="btn btn-secondary edit-btn" onclick="chuyengiaodien('addAccount','listAccount')">Quay lại</button>
                        </div>
                        
                    </form>
                    
                </div>

            </section>
        </div>
    </main>
</div>
<script src="../models/nhanvien/func.js" ></script> 