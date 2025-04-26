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
                    <?php
                        if(isset($_GET['id_detail'])) {
                            ?> 
                                <div class="right-actions">
                                    <a href="index.php?sidebar=quanlynhanvien"><i class="fa-solid fa-left-long" style="color: black;"></i></a>
                                </div> 
                            <?php
                        } else {
                            ?>
                                <div class="right-actions">
                                    <a href="index.php?nav=hoso&query=sua"><i class='fa fa-edit'></i></a>
                                </div>
                            <?php
                        }
                    ?>
                    <div>
                        <img src="../models/quanlyhoso/imagesNV/<?=$profile_img?>" alt="Avatar" class="avatar">
                        <h2 class="name"><?=$profile_name?></h2>
                        <p class="position"><?=$profile_role?></p>
                        <?php
                            if($profile_trangthai == 1) {
                                ?><span class="status">Đang làm việc</span><?php
                            } else {
                                ?><span class="status" style="background-color:rgb(251, 189, 189); color:rgb(216, 86, 86);">Tài khoản đã bị khóa</span><?php
                            }
                        ?>
                        
                    </div>
                </div>

                <div class="profile-info">
                <h3>Thông tin nhân sự</h3>
                <div class="info-item">
                    <i class="fa fa-envelope icon"></i>
                    <span class="label">Email:</span>
                    <span class="value"><?=$profile_email?></span>
                </div>
                <div class="info-item">
                    <i class="fa fa-phone icon"></i>
                    <span class="label">Điện thoại:</span>
                    <span class="value"><?= $profile_sdt ?: "Chưa có dữ liệu" ?></span>
                </div>
                <div class="info-item">
                    <i class="fa fa-venus-mars icon"></i>
                    <span class="label">Giới tính:</span>
                    <span class="value"><?=$profile_gender ?: "Chưa có dữ liệu" ?></span>
                </div>
                <div class="info-item">
                    <i class="fa fa-birthday-cake icon"></i>
                    <span class="label">Ngày sinh:</span>
                    <span class="value"><?= $profile_ngaysinh ?: "Chưa có dữ liệu" ?></span>
                </div>
                <div class="info-item">
                    <i class="fa fa-calendar icon"></i>
                    <span class="label">Địa chỉ:</span>
                    <span class="value"><?= $profile_diachi ?: "Chưa có dữ liệu" ?></span>
                </div>
                </div>
            </div>
        </section>
    </main>
</div> 