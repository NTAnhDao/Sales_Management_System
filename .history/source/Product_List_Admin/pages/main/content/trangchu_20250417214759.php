<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ</title>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 0;
        }
        .main-content {
            padding: 20px;
        }
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .page-header h3 {
            margin: 0;
            color: #333;
        }
        .date-picker {
            padding: 8px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
        }
        .cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        .card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .card-content {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .card-icon {
            font-size: 30px;
            color: #fff;
            background: #007bff;
            padding: 10px;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card h4 {
            margin: 0;
            font-size: 14px;
            color: #666;
        }
        .card h2 {
            margin: 5px 0;
            font-size: 24px;
            color: #333;
        }
        .card p {
            margin: 0;
            font-size: 12px;
            color: #999;
        }
        .card.products .card-icon { background: #ff6f61; }
        .card.categories .card-icon { background: #28a745; }
        .card.employees .card-icon { background: #ffca28; }
        .charts {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 20px;
        }
        .chart-container {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .chart-container h4 {
            margin: 0 0 10px 0;
            font-size: 16px;
            color: #333;
        }
        @media (max-width: 768px) {
            .charts {
                grid-template-columns: 1fr;
            }
        }
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
    <script>'functionQLDM_SP/functionForDashBoard'</script>
</body>
</html>