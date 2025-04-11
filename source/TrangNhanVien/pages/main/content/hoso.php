<?php include 'func/showprofile.php'; ?>
<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Hồ sơ nhân viên </h3>
            </div>
        </div>
        <section class="profife-view">
            <div class="profile-container">
                <div class="profile-header">
                    <div class="right-actions">
                        <a  onclick = "chuyengiaodien('profife-view','profile-edit')"><i class='fa fa-edit'></i></a>
                    </div>
                    <div>
                        
                        <img src="images/<?=$profile_img?>" alt="Avatar" class="avatar">
                        <h2 class="name"><?=$profile_name?></h2>
                        <p class="position"><?=$_SESSION['role']?></p>
                        <span class="status">Đang làm việc</span>
                    </div>
                </div>

                <div class="profile-info">
                <h3>Thông tin nhân sự</h3>
                <div class="info-item">
                    <span class="fa fa-envelope icon"></span>
                    <span class="label">Email:</span>
                    <span class="value"><?=$profile_email?></span>
                </div>
                <div class="info-item">
                    <span class="fa fa-phone icon"></span>
                    <span class="label">Điện thoại:</span>
                    <span class="value"><?=$profile_sdt?></span>
                </div>
                <div class="info-item">
                    <span class="fa fa-venus-mars icon"></span>
                    <span class="label">Giới tính:</span>
                    <span class="value"><?=$profile_gender?></span>
                </div>
                </div>
                            
                </div>
        </section>
            <section class="profile-edit d-none">
                <div class="profile-container">
                    <form action="func/editprofile.php" method="post">
                        <div class="form-group avatar-group">
                            <label>Ảnh đại diện:</label>
                            <input type="file" id="avatarInput" accept="image/*">
                            <img id="avatarPreview" src="" alt="Ảnh đại diện" />
                        </div>

                        <div class="form-group">
                            <label for="name">Họ tên:</label>
                            <input type="text" id="name" name="name">
                        </div>

                        <div class="form-group">
                            <label for="phone">Số điện thoại:</label>
                            <input type="text" id="phone" name="phone">
                        </div>

                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email">
                        </div>

                        <div class="form-group">
                            <label for="gender">Giới tính:</label>
                            <select id="gender" name="gender">
                            <option value="">-- Chọn --</option>
                            <option value="nam">Nam</option>
                            <option value="nu">Nữ</option>
                            <option value="khac">Khác</option>
                            </select>
                        </div>

                        <div class="right-actions">
                            <button type="submit" class="btn btn-primary edit-btn" id="edit-profile">Sửa</button>
                            <button type="button" class="btn btn-secondary edit-btn" onclick="chuyengiaodien('profile-edit','profife-view')">Hủy</button>
                        </div>
                    </form>

                </div>
            </section>

    </main>
</div> 