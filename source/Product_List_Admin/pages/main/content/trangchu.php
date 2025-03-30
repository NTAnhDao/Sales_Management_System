<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <div>
                <h3>Trang chủ</h3>
            </div>
            </div>
                <button class = "button-actions"> <!--button phía dưới cài đặt-->
                    <i class="fa-regular fa-calendar"></i> Today <i class="fa-solid fa-chevron-down"></i>
                </button>
            </div>
        </div>


            <div class="cards">
                <!-- Purple Card (Biểu đồ đường) -->
                <div class="card purple">
                    <div class="card-content">
                        <div class="card-icon"><i class="las la-shopping-bag"></i></div>
                        <div>
                            <h4>NEW ORDERS</h4>
                            <h2>35,673</h2>
                            <p>2.00% (30 days)</p>
                        </div>
                    </div>
                    <div class = "chart-container">
                        <canvas id="lineChartPurple"></canvas>
                    </div>
                    
                </div>
            
                <!-- Green Card (Biểu đồ cột) -->
                <div class="card green">
                    <div class="card-content">
                        <div class="card-icon"><i class="las la-dollar-sign"></i></div>
                        <div>
                            <h4>TOTAL INCOME</h4>
                            <h2>$14,966</h2>
                            <p>Increased by 7.35%</p>
                        </div>
                    </div>
                    <div class = "chart-container"> 
                        <canvas id="barChartGreen"></canvas>
                    </div>
                   
                </div>
            
                <!-- Blue Card (Biểu đồ đường) -->
                <div class="card blue">
                    <div class="card-content">
                        <div class="card-icon"><i class="las la-file-invoice-dollar"></i></div>
                        <div>
                            <h4>TOTAL EXPENSE</h4>
                            <h2>26,526</h2>
                            <p>Increased by 7.35%</p>
                        </div>
                    </div>
                    <div class = "chart-container"> 
                        <canvas id="lineChartBlue"></canvas>
                    </div>
                   
                </div>
            
                <!-- Yellow Card (Biểu đồ cột) -->
                <div class="card yellow">
                    <div class="card-content">
                        <div class="card-icon"><i class="las la-user"></i></div>
                        <div>
                            <h4>NEW USERS</h4>
                            <h2>32,566</h2>
                            <p>54.1% less earnings</p>
                        </div>
                    </div>
                    <div class = "chart-container">
                        <canvas id="barChartYellow"></canvas>
                    </div>
                    
                </div>
            </div>
        
    </main>
</div> 