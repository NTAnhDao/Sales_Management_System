$(document).ready(function(){
    //Đặt giá trị mặc định là từ đầu tháng đến hiện tại
    const today = new Date();
    const startDay = new Date(today.getFullYear(), today.getMonth(), 1);
    document.getElementById("fromDate").value = formatDate(startDay);
    document.getElementById("toDate").value = formatDate(today);
    
    loadReport(); //Gọi khi load trang
    
    //Khi người dùng chọn lại ngày
    $("#fromDate, #toDate").on("change", loadReport);
})
    
function formatDate(date) {
    return date.toLocaleDateString('sv-SE');
}

function rangeChange(value) {
    const today = new Date();
    let from, to;

    switch (value) {
        case "today":
            from = to = new Date(today);
            break;
        case "yesterday":
            from = new Date(today);
            from.setDate(from.getDate() - 1);
            to = new Date(from);
            break;
        case "week":
            from = new Date(today);
            from.setDate(from.getDate() - 6)
            to = new Date(today);
            break;
        case "thismonth":
            from = new Date(today.getFullYear(), today.getMonth(), 1);
            to = new Date(today.getFullYear(), today.getMonth() + 1, 0);
            break;
        default:
            return;
    }

    document.getElementById("fromDate").value = formatDate(from);
    document.getElementById("toDate").value = formatDate(to);

    loadReport(); //Gọi lại hàm load báo cáo
}

function loadReport() {
    const fromDate = $("#fromDate").val();
    const toDate = $("#toDate").val();

    //Kiểm tra ngày đầu phải nhỏ hơn ngày cuối
    if (new Date(fromDate) > new Date(toDate)) {
        alert("Ngày đầu phải nhỏ hơn ngày cuối");
        location.reload();
        return;
    }

    $.ajax({
        url: "../models/report/getdata.php",
        method: "POST",
        data: {fromDate: fromDate, toDate: toDate},
        dataType: "json",
        success: function(response) {
            if(response.success) {
                //Cập nhật số liệu
                $("#totalAmount").text(response.totalAmount);
                $("#totalOrders").text(response.totalOrders);
                $("#totalProducts").text(response.totalProducts);
                if (response.isAdmin) {
                    $('#totalProfit').text(response.totalProfit);
                    $("#profitCard").removeClass("d-none");
                } else {
                    $("#profitCard").addClass("d-none");
                }

                // Vẽ biểu đồ
                drawChart(response);

                //Danh sách các đơn hàng
                let tbody = document.getElementById("reporttbody");
                tbody.innerHTML = ""; //Xóa dữ liệu cũ
                response.orders.forEach((order, index) => {
                    let tableContent = `<tr>
                                            <td>${order.ngaymua}</td>
                                            <td>${order.tongtien}</td>
                                            <td>${order.tienkhachdua}</td>
                                            <td>${order.tientra}</td>
                                            <td>${order.soluongsp}</td>
                                            <td>
                                            <a class='btn-custom btn btn-secondary' href='index.php?sidebar=baocao&idctdh=${order.id}'>
                                            Xem
                                            </a>
                                            </td>
                                        </tr>`;
                    let newRow = document.createElement("tr");
                    newRow.innerHTML = tableContent;
                    tbody.appendChild(newRow);
                });
            } else {
                alert(response.mess);
                location.reload();
            }
        },
        error: function () {
            alert("Không thể tải dữ liệu báo cáo, vui lòng thử lại sau");
        }
    });
}

function drawChart(data) {
    const ctx = document.getElementById('myChart').getContext('2d');

    //Xóa chart cũ nếu có
    if (window.reportChartInstance) {
        window.reportChartInstance.destroy();
    }

    //Lấy dữ liệu đã được nhóm theo ngày
    const labels = data.statistic.map(stat => stat.ngaymua);
    const tongtien = data.statistic.map(stat => stat.tongtien);
    const soluongsp = data.statistic.map(stat => stat.soluongsp);
    const sodon = data.statistic.map(stat => stat.sodon);
    const loinhuan = data.statistic.map(stat => stat.loinhuan || 0); //Chỉ dành cho admin

    const datasets = [];
    //Nếu là admin thì thêm lợi nhuận
    if (data.isAdmin && loinhuan.some(p => p > 0)) {
        datasets.push(
        {
        type: 'line',
        label: 'Tổng lợi nhuận',
        data: loinhuan,
        borderColor: '#f8b500',
        fill: false,
        yAxisID: 'y1',
        borderWidth: 2,
        tension: 0.3,
        pointStyle: 'line'
        });
    }
    datasets.push(
        {
            type: 'line',
            label: 'Tổng số tiền nhận',
            data: tongtien,
            borderColor: '#f857a6',
            fill: false,
            yAxisID: 'y1',
            borderWidth: 2,
            tension: 0.3,
            pointStyle: 'line'
        },
        {
            type: 'bar',
            label: 'Số lượng sản phẩm',
            data: soluongsp,
            backgroundColor: '#42e695',
            barThickness: 40, 
            maxBarThickness: 50,
            yAxisID: 'y',
            pointStyle: 'rect'
        },
        {
            type: 'bar',
            label: 'Số lượng đơn hàng',
            data: sodon,
            backgroundColor: '#5b86e5',
            barThickness: 40,
            maxBarThickness: 50,
            yAxisID: 'y',
            pointStyle: 'rect'
        }
    );

    window.reportChartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "top",
                    labels: {
                        usePointStyle: true,
                        font: {
                            size: 15
                        },
                    }
                },
                tooltip: {
                    usePointStyle: true,
                },
                title: {
                    display: true,
                    text: "Báo cáo bán hàng",
                    font: {
                        size: 20
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Ngày'
                    }
                },
                y: {
                    type: 'linear',
                    position: 'left',
                    title: {
                        display: true,
                        text: "Số lượng"
                    }
                },
                y1: {
                    type: 'linear',
                    position: 'right',
                    title: {
                        display: true,
                        text: "VND"
                    }
                }
            }
        }
    });
}
