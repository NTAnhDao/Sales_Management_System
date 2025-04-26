<?php 
include '../models/quanlyhoso/editprofile.php';
include '../models/quanlyhoso/getProfileNV.php';
?>

<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Hồ sơ nhân viên </h3>
            </div>
        </div>
        <div id="err-edit-mes" class="alert alert-danger d-none"></div>
        <section class="profile-edit">
            <div class="profile-container">
                <form action="../models/quanlyhoso/upload_avatar.php" method="POST" enctype="multipart/form-data">
                    <h5> Chỉnh sửa hồ sơ</h5>
                    <div class="drop-zone avatar-edit" id="drop-avatar">
                        <img id ="preview" src="../models/quanlyhoso/imagesNV/<?=$profile_img?>" alt="Ảnh đại diện" class="avatar">
                        <br>
                        <p>Kéo và thả ảnh ở đây</p>
                        <p>--hoặc--</p>
                        <label for="avatarInput" class="btn btn-outline-primary">Chọn ảnh</label>
                        <input type="file" name="avatar" id="avatarInput" class="d-none" accept="image/*" />
                    </div>
                    <div class="file-actions right-actions">
                        <button type="submit" class="btn btn-primary" id="save-avatar-btn">Lưu</button>
                    </div>
                </form>
                <form action="index.php?nav=hoso&query=sua" method="post">
                    <div class="form-group">
                    <label><i class="fa fa-user"></i> Họ và tên</label>
                    <input value="<?=$profile_name?>" type="text" name="fullname" placeholder="Nhập họ và tên" id="name-edit">
                    </div>

                    <div class="form-group">
                    <label><i class="fa fa-envelope"></i> Email</label>
                    <input value="<?=$profile_email?>" type="email" name="email" placeholder="Nhập email" readonly>
                    </div>

                    <div class="form-group">
                    <label><i class="fa fa-map-marker"></i> Địa chỉ</label>
                    <input value="<?=$profile_diachi?>" type="text" name="address" placeholder="Nhập địa chỉ">
                    </div>

                    <div class="form-group">
                    <label><i class="fa fa-phone"></i> Số điện thoại</label>
                    <input value="<?=$profile_sdt?>" type="text" name="phone" placeholder="Nhập số điện thoại"  oninput="this.value = this.value.replace(/[^0-9]/g, '')" pattern="[0-9]{10}">
                    </div>

                    <div class="form-group">
                    <label><i class="fa fa-venus-mars"></i> Giới tính</label>
                    <select name="gender">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="Nam" <?= ($profile_gender == "Nam") ? "selected" : "" ?>>Nam</option>
                        <option value="Nữ" <?= ($profile_gender == "Nữ") ? "selected" : "" ?>>Nữ</option>
                    </select>
                    </div>

                    <div class="form-group">
                    <label><i class="fa fa-calendar"></i> Ngày sinh</label>
                    <input value="<?=$profile_ngaysinh?>" type="date" name="dob" >
                    </div>
                    <div class="right-actions">
                        <button type="submit" class="btn btn-primary edit-btn" id="edit-profile">Sửa</button>
                        <a href="index.php?nav=hoso" class="btn btn-secondary edit-btn">Hủy</a>
                    </div>
                </form>
            </div>
        </section>
    </main>
</div> 
<script src="../models/quanlyhoso/script.js" ></script>
