<div class="main-content" id="main">
        <main>
            <div class="page-header">
                <div>
                    <h3>Trang chủ</h3>
                </div>
                
            </div>

            <div class="stats-orders">
                <div class="stats-order-box products">
                    <h2>
                        <?php
                            echo isset($_COOKIE["totalProducts"]) ? number_format($_COOKIE["totalProducts"]) : 0;
                        ?>
                    </h2>
                    <p>Tổng số lượng sản phẩm</p>
                </div>
                <div class="stats-order-box categories">
                    <h2>
                        <?php
                            echo isset($_COOKIE["totalCategories"]) ? number_format($_COOKIE["totalCategories"]) : 0;
                        ?>
                    </h2>
                    <p>Tổng số danh mục</p>
                </div>
                <div class="stats-order-box employees">
                    <h2>
                        <?php
                            echo isset($_COOKIE["totalEmployees"]) ? number_format($_COOKIE["totalEmployees"]) : 0;
                        ?>
                    </h2>
                    <p>Tổng số nhân viên</p>
                </div>
            </div>
        </main>
    </div>

