
        

    </style>
</head>
<body>
    <div class="main-content" id="main">
        <main>
            <div class="page-header">
                <div>
                    <h3>Trang chủ</h3>
                </div>
                <div>
                    <input type="date" id="date-picker" class="date-picker" value="<?php echo date('Y-m-d'); ?>">
                </div>
            </div>

            <!-- Cards -->
            <div class="cards">
                <!-- Card: Tổng số lượng sản phẩm -->
                <div class="card products">
                    <div class="card-content">
                        <div class="card-icon"><i class="fas fa-box"></i></div>
                        <div>
                            <h4>TỔNG SỐ LƯỢNG SẢN PHẨM</h4>
                            <h2 id="total-products">0</h2>
                            <p id="products-change">Tăng 0% (so với ngày trước)</p>
                        </div>
                    </div>
                </div>

                <!-- Card: Tổng số danh mục -->
                <div class="card categories">
                    <div class="card-content">
                        <div class="card-icon"><i class="fas fa-tags"></i></div>
                        <div>
                            <h4>TỔNG SỐ DANH MỤC</h4>
                            <h2 id="total-categories">0</h2>
                            <p id="categories-change">Tăng 0% (so với ngày trước)</p>
                        </div>
                    </div>
                </div>

                <!-- Card: Tổng số nhân viên -->
                <div class="card employees">
                    <div class="card-content">
                        <div class="card-icon"><i class="fas fa-users"></i></div>
                        <div>
                            <h4>TỔNG SỐ NHÂN VIÊN</h4>
                            <h2 id="total-employees">0</h2>
                            <p id="employees-change">Tăng 0% (so với ngày trước)</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Charts -->
            <div class="charts">
                <!-- Biểu đồ đường: Doanh thu -->
                <div class="chart-container">
                    <h4>Doanh thu theo ngày</h4>
                    <canvas id="lineChartRevenue"></canvas>
                </div>

                <!-- Biểu đồ cột: Số lượng sản phẩm bán ra -->
                <div class="chart-container">
                    <h4>Số lượng sản phẩm bán ra</h4>
                    <canvas id="barChartSales"></canvas>
                </div>
            </div>
        </main>
    </div>
    <script>src = "functionQLDM_SP/functionForDashBoard.js"</script>
</body>
</html>