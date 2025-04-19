<div class = "main-content " id="main">
    <main>
        <div class ="page-header">
            <h3>Báo cáo</h3>
        </div>
        <div class="content">
            <session class="report">
                <div class="date-time mb-3">
                    <input type="date" id="fromDate" class="date-type">
                    <span> - </span>
                    <input type="date" id="toDate" class="date-type">

                    <select class="date-type" onchange="rangeChange(this.value)">
                        <option disabled selected>Chọn khoảng</option>
                        <option value="today">Hôm nay</option>
                        <option value="yesterday">Hôm qua</option>
                        <option value="week">7 ngày qua</option>
                        <option value="thismonth">Tháng này</option>
                    </select>
                </div>
                <div class="cards mb-3">
                    <div class="card pink">
                        <div class="card-content">
                            <div class="card-icon"><i class="fa fa-money"></i></div>
                            <div class="card-text">
                                <h5>Tổng số tiền nhận</h5>
                                <h2 id="totalAmount">0</h2>
                            </div>
                        </div>
                    </div>
                    <div class="card blue">
                        <div class="card-content">
                            <div class="card-icon"><i class="fa fa-list-alt"></i></div>
                            <div class="card-text">
                                <h5>Số lượng đơn hàng</h5>
                                <h2 id="totalOrders">0</h2>
                            </div>
                        </div>
                    </div>
                    <div class="card green">
                        <div class="card-content">
                            <div class="card-icon"><i class="fa fa-cubes"></i></div>
                            <div class="card-text">
                                <h5>Số lượng sản phẩm</h5>
                                <h2 id="totalProducts">0</h2>
                            </div>
                            
                        </div>
                    </div>
                    <div class="card yellow d-none" id="profitCard">
                        <div class="card-content">
                            <div class="card-icon"><i class="fa fa-line-chart"></i></div>
                            <div class="card-text">
                                <h5>Tổng lợi nhuận</h5>
                                <h2 id="totalProfit">0</h2>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="report-chart">
                    <canvas id="myChart" height="100px"></canvas>
                </div>
                <div class="card-catalog">
                    <h5>Danh sách đơn hàng</h5>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Ngày mua</th>
                                <th>Tổng tiền</th>
                                <th>Tiền khách đưa</th>
                                <th>Tiền trả lại</th>
                                <th>Số lượng sản phẩm</th>
                                <th>Chi tiết</th>
                            </tr>
                        </thead>
                        <tbody id="reporttbody"></tbody>
                    </table>
                </div>
            </session>
        </div>
        
    </main>
</div> 
<script src="report/func.js"></script>