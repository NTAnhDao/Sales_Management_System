// Biểu đồ đường: Doanh thu
const lineChartRevenue = new Chart(document.getElementById('lineChartRevenue'), {
    type: 'line',
    data: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [{
            label: 'Doanh thu ($)',
            data: [],
            borderColor: '#ff6f61',
            backgroundColor: 'rgba(255, 111, 97, 0.2)',
            fill: true,
            tension: 0.4
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Biểu đồ cột: Số lượng sản phẩm bán ra
const barChartSales = new Chart(document.getElementById('barChartSales'), {
    type: 'bar',
    data: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [{
            label: 'Số lượng bán ra',
            data: [],
            backgroundColor: '#28a745',
            borderColor: '#28a745',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Hàm cập nhật dữ liệu khi chọn tuần
function updateDashboard(week) {
    $.ajax({
        url: 'getDashboardData.php',
        type: 'GET',
        data: { week: week },
        dataType: 'json',
        success: function(data) {
            if (data.error) {
                alert(data.error);
                return;
            }

            // Cập nhật các box
            document.getElementById('total-products').textContent = data.totalProducts.toLocaleString();
            document.getElementById('total-categories').textContent = data.totalCategories.toLocaleString();
            document.getElementById('total-employees').textContent = data.totalEmployees.toLocaleString();
            document.getElementById('products-change').textContent = `Tăng ${data.productsChange}% (so với tuần trước)`;
            document.getElementById('categories-change').textContent = `Tăng ${data.categoriesChange}% (so với tuần trước)`;
            document.getElementById('employees-change').textContent = `Tăng ${data.employeesChange}% (so với tuần trước)`;

            // Cập nhật biểu đồ
            lineChartRevenue.data.datasets[0].data = data.revenueData;
            barChartSales.data.datasets[0].data = data.salesData;
            lineChartRevenue.update();
            barChartSales.update();
        },
        error: function(xhr, status, error) {
            console.log('Lỗi AJAX:', status, error);
            alert('Lỗi khi lấy dữ liệu từ server');
        }
    });
}

// Hàm lấy tuần hiện tại theo định dạng YYYY-Www
function getCurrentWeek() {
    const today = new Date();
    const firstDayOfYear = new Date(today.getFullYear(), 0, 1);
    const pastDaysOfYear = (today - firstDayOfYear) / 86400000;
    const weekNumber = Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7);
    const weekString = weekNumber < 10 ? `0${weekNumber}` : weekNumber;
    return `${today.getFullYear()}-W${weekString}`;
}

// Cập nhật dữ liệu ban đầu khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    const weekPicker = document.getElementById('week-picker');
    weekPicker.value = getCurrentWeek();
    updateDashboard(weekPicker.value);

    // Sự kiện khi thay đổi tuần
    weekPicker.addEventListener('change', function() {
        updateDashboard(this.value);
    });
});