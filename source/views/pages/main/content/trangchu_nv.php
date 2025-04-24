<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Trang chủ</h3>
            </div>

        </div>
        <div class="orders">
            <div class="order-box">
                <h2>
                    <?php
                        echo isset($_COOKIE["newcustomer"]) ? $_COOKIE["newcustomer"] : 0;
                    ?>
                </h2>
                <p>Khách hàng mới</p>
            </div>
            <div class="order-box">
                <h2>
                    <?php
                        echo isset($_COOKIE["donhangtoday"]) ? $_COOKIE["donhangtoday"] : 0;
                    ?>
                </h2>
                <p>Đơn hàng </p>
            </div>
            <div class="order-box">
                <h2>
                    <?php
                        echo isset($_COOKIE["doanhthutoday"]) ? number_format($_COOKIE["doanhthutoday"]) : 0;
                    ?>
                </h2>
                <p>Doanh thu </p>
            </div>
        </div>
    </main>
</div> 