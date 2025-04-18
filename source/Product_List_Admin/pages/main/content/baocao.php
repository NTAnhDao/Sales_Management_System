<div class = "main-content " id="main">
    <main>
        <div class ="page-header">
            <h3>Báo cáo</h3>
        </div>
        <div class="content">
            <session class="report">
                <div class="date-time">
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
            </session>
        </div>
        
    </main>
</div> 
<script> src="report/func.js" </script>