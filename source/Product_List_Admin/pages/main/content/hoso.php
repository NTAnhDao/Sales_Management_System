<?php 
    include 'quanlyhoso/editprofile.php';
    include 'quanlyhoso/getProfileNV.php';
?>

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
                    if (isset($_GET["id_detail"])) {
                        ?>
                        <div class="right-actions">
                            <a href="index.php?sidebar=quanlynhanvien"><i class='fa fa-long-arrow-left'></i></a>
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
                        <img src="../images/<?=$profile_img?>" alt="Avatar" class="avatar">
                        <h2 class="name"><?=$profile_name?></h2>
                        <p class="position"><?=$profile_role?></p>
                        <span class="status"><?=$profile_status?></span>
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