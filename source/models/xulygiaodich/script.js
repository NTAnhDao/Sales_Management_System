var hoten = document.getElementById("hoten");
var diachi = document.getElementById("diachi");
var idkh = "";
var btnfindsdt = document.getElementById("search-sdt");
var isNew = false;
var tenv = "";
var diachiv = "";
var sdtv = "";
var tongsp = 0;
var total = 0;
var tienkhachdua = 0;
// Kiểm tra xem đã nhập thông tin khách hàng chưa
function checkData() {
    tenv = document.getElementById("enter-name").value;
    diachiv = document.getElementById("enter-diachi").value;
    sdtv = document.getElementById("customer-phone").value;
    document.getElementById("enter-name").focus();
    if (sdtv === "" || tenv === "" || diachiv === "" ){
        errormessage("Lỗi. Vui lòng nhập đầy đủ thông tin khách hàng!", 'err-addkh');
        return;
    }else{
        let nhapcustomer = bootstrap.Modal.getInstance(document.getElementById('nhapcustomer'));
        nhapcustomer.hide(); 
        isNew = true;
    } 
}

// Thêm thông tin khách hàng khi nhấn thanh toán
function insertData() { 
    tenv = document.getElementById("enter-name").value;
    diachiv = document.getElementById("enter-diachi").value;
    sdtv = document.getElementById("customer-phone").value;
    tienkhachdua = document.getElementById("tienkhachdua").value;
    
    if (tongsp == 0){
        alert("Vui lòng kiểm tra lại thông tin giỏ hàng trước khi thanh toán!");
        return false;
    }
    if (sdtv === ""){
        errormessage("Vui lòng nhập số điện thoại!", 'err-findsdt');
        return false;
    }
    if (tienkhachdua == 0){
        errormessage("Vui lòng nhập tiền khách đưa!", 'err-tien');
        return false;
    }
    if (tienkhachdua < total){
        errormessage("Lỗi. Số tiền khách đưa phải nhiều hơn tổng tiền!", 'err-tien');
        return false;
    }
    if(isNew){
        if (sdtv === "" || tenv === "" || diachiv === ""){
            alert("Vui lòng kiểm tra lại thông tin khách hàng trước khi thanh toán!");
            return false;
        }
        $.ajax({
            url: "../models/xulygiaodich/addcustomer.php",
            type: "POST",
            data: { ten: tenv, diachi: diachiv, sdt: sdtv},
            success: function() {
                errormessage(null,'err-addkh');
            }
        });
        isNew = false;
    }
    errormessage(null, 'err-tien');

    return true;
}


// Tìm số điện thoại
document.addEventListener("DOMContentLoaded", function () {
    btnfindsdt.addEventListener("click", function () {
        let sdt = $("#customer-phone").val().trim();

        if (sdt === "") {
            errormessage("Vui lòng nhập số điện thoại!",'err-findsdt')
            return;  
        }

        fetch("../models/xulygiaodich/findsdt.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `sdt=${sdt}`
        })
        .then(response => response.json()) // Chuyển đổi kết quả JSON
        .then(product => {
            if (product.error) {
                hoten.innerHTML = "";
                diachi.innerHTML = "";
                alert(product.error);
                let nhapcustomer = new bootstrap.Modal(document.getElementById("nhapcustomer"));
                nhapcustomer.show(); 
                return;
            }else{
                tenv = product.name;
                sdtv =  sdt;
                idkh = product.id;
                hoten.innerHTML = "Khách hàng: " + product.name + " - " + sdt;
                diachi.innerHTML = "Địa chỉ: " + product.address;
                errormessage(null,'err-findsdt');
                openLichSu(product.lichsu);
            }
        })
        .catch(error => console.error("Lỗi:", error));
    });
});

function openLichSu(lichsu) {
    let lichsuContainer = document.getElementById("lichsumua");
    lichsuContainer.innerHTML = ""; // clear cũ

    if (lichsu.length === 0) {
        lichsuContainer.innerHTML = "<tr><td colspan='7'>Không có lịch sử mua hàng</td></tr>";
        return;
    }

    lichsu.forEach((order, index) => {
        let html = `
            <tr>
                <td>${index + 1}</td>
                <td>${order.ngaymua}</td>
                <td>${formatMoney(order.tongtien)}</td>
                <td>${formatMoney(order.tienkhachdua)}</td>
                <td>${formatMoney(order.tientra)}</td>
                <td>${order.soluongsp}</td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick='xemChiTiet(${JSON.stringify(order.chitiet)})'>
                        Xem chi tiết
                    </button>
                </td>
            </tr>
        `;
        lichsuContainer.innerHTML += html;
    });
}


function xemChiTiet(chitiet) {
    let html = '<table class="table table-striped">';
    html += '<thead><tr><th>STT</th><th>Tên sản phẩm</th><th>Số lượng</th><th>Đơn giá</th></tr></thead><tbody>';

    chitiet.forEach((item, index) => {
        html += `
            <tr>
                <td>${index + 1}</td>
                <td>${item.tensp}</td>
                <td>${item.soluong}</td>
                <td>${formatMoney(item.giaban)}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    document.getElementById('chitiet-container').innerHTML = html;

    let myModal = new bootstrap.Modal(document.getElementById('modalChiTiet'));
    myModal.show();
}

// thêm vào giỏ hàng
function isAddProduct(button){
    let isIDbtn = button.id;  
    // Nếu thêm bằng cách chọn sản phẩm trong danh sách
    if (isIDbtn == "") {
        let row = button.closest("tr"); // Tìm thẻ <tr> chứa nút
        let giaban = parseInt(row.children[2].textContent); // Giá
        let tensp = row.children[1].textContent;
        updateCart(1, tensp, giaban);
    }else{ // Thêm bằng cách nhập mã vạch
        let name = $("#product_id").val().trim();
        let sl = $("#soluong").val().trim();

        if (name === "" || sl === "") {
            errormessage("Vui lòng nhập đủ thông tin!",'err-addsp');
            return;
        }

        fetch("../models/xulygiaodich/addsptocart.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${name}&sl=${sl}`
        })
        .then(response => response.json()) // Chuyển đổi kết quả JSON
        .then(product => {
            if (product.error) {
                errormessage(product.error,'err-addsp');
                return;
            }

            updateCart(sl, product.tensp, product.giabanle);
            errormessage(null,'err-addsp');
        })
        .catch(error => console.error("Lỗi:", error));
    }        
}

// Thêm sản phẩm vào giỏ
function updateCart(sl, tensp, giaban) {
    let tongtien = sl*giaban;
    tongsp += parseInt(sl);
    let existingRow = null;
    document.querySelectorAll("#tbody tr").forEach(row => {
        let productName = row.cells[0].innerText;
        if (productName === tensp) {
            existingRow = row;
        }
    });

    if (existingRow) {
        let quantityCell = existingRow.cells[2];
        let quantityInput = quantityCell.querySelector('input[name="quantity"]');
        let oldsl = parseInt(quantityInput.value);
        let newsl = oldsl + parseInt(sl);
        quantityInput.value = newsl;
        existingRow.cells[3].innerText = (newsl * giaban);
        total += (newsl - oldsl) * giaban;
    } else {
        total += tongtien;
        let tableContent = `<tr>
            <td>${tensp}</td>
            <td>${giaban}</td>
            <td>
                <div class="modify-sl">
                    <button class="btn-updown" type="button" onclick="decreaseQuantity(this)">-</button>
                    <input class="soluong-input" type="text" name="quantity" value="${sl}" readonly>
                    <button class="btn-updown" type="button" onclick="increaseQuantity(this)">+</button>
                </div>
            </td>
            <td>${tongtien}</td>
            <td><a onclick="removeProduct(this)"><i class="fa fa-trash" ></i></a></td>
        </tr>`;

        let tbody = document.getElementById("tbody");
        let newRow = document.createElement("tr");
        newRow.innerHTML = tableContent;
        tbody.appendChild(newRow);
    }

    // Cập nhật tổng tiền hiển thị
    document.querySelectorAll(".total").forEach(el => {
        el.innerHTML = total.toLocaleString('vi-VN');
    });
}

// Tăng số lượng sản phẩm
function increaseQuantity(button) {
    let input = button.parentElement.querySelector('input[name="quantity"]');
    let value = parseInt(input.value);
    value += 1;
    input.value = value;
    tongsp += 1;
    let row = button.closest("tr"); // Tìm thẻ <tr> chứa nút
    let td_giaban = parseInt(row.children[1].textContent); // Giá bán của dòng điều chỉnh sô lượng
    row.children[3].textContent = value*td_giaban;
    total += td_giaban;
    document.querySelectorAll(".total").forEach(el => {
        el.innerHTML = total.toLocaleString('vi-VN');
    });
}

// Giảm số lượng sản phẩm
function decreaseQuantity(button) {
    let input = button.parentElement.querySelector('input[name="quantity"]');
    let value = parseInt(input.value);
    if (value > 1) {
        value -= 1;
        input.value = value;
        tongsp -= 1;
        let row = button.closest("tr"); // Tìm thẻ <tr> chứa nút
        let td_giaban = parseInt(row.children[1].textContent); // Giá bán của dòng điều chỉnh sô lượng
        row.children[3].textContent = value*td_giaban;
        total -= td_giaban;
        document.querySelectorAll(".total").forEach(el => {
            el.innerHTML = total.toLocaleString('vi-VN');
        });
    }else{
        removeProduct(button);
    }
}

// Xóa sản phẩm
function removeProduct(element){
    let row = element.closest("tr"); // Tìm thẻ <tr> chứa nút
    let td_tongtien = parseInt(row.children[3].textContent); // Tổng tiền của dòng chọn xóa
    let quantityCell = row.children[2];
    let quantity = quantityCell.querySelector('input[name="quantity"]').value;        
    if (!isNaN(quantity)) {
        tongsp -= quantity;
    } // Số sản phẩm của dòng chọn xóa
    total -= td_tongtien;
    document.querySelectorAll(".total").forEach(el => {
        el.innerHTML = total.toLocaleString('vi-VN');
    });
    row.remove();
}


// Xóa tất cả sản phẩm trong giỏ hàng
function removeallProduct(){
    document.getElementById("tbody").innerHTML = "";
    total = tongsp = 0;
    document.querySelectorAll(".total").forEach(el => {
        el.innerHTML = total.toLocaleString('vi-VN');
    });
}

// Khi nhấn nút tiếp theo ở trang xử lý giao dịch sẽ hiển thị tổng sản phẩm đã chọn
document.getElementById("nextprocess").addEventListener("click", function () {
    document.getElementById("tongsp").innerHTML = tongsp;
});

// Khi nhấn nút thanh toán
document.getElementById("btnthanhtoan").addEventListener("click", function () {
    let sourceTable = document.getElementById("tbody");
    let destinationTable = document.getElementById("tbodyhd");
    document.getElementById("tenkh").innerHTML = tenv;
    document.getElementById("sdtkh").innerHTML = sdtv;

    destinationTable.innerHTML = ""; 

    let selectedCols = [0, 1, 2, 3]; 

    // Lấy dữ liệu từ giỏ hàng
    let cartData = [];
    for (let row of sourceTable.rows) {
        let newRow = destinationTable.insertRow();
        let quantity = 1;
        for (let i of selectedCols) {
            let newCell = newRow.insertCell();
            if(i==2){
                quantity = row.cells[2].querySelector('input[name="quantity"]').value;    
                newCell.innerHTML = "<td>"+quantity+"</td>";    
            }else{
                newCell.innerHTML = row.cells[i].innerHTML;
            }
        }
        let tensp = row.cells[0].innerHTML;
        let slsp = parseInt(quantity);

        cartData.push({
            tensp: tensp,
            slsp: slsp
        });
    }


    if(insertData()){
        // đưa dữ liệu vào taoHD.php để tạo hóa đơn(đơn hàng)
        fetch("../models/xulygiaodich/taoHD.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `tienkhachdua=${tienkhachdua}&slsp=${tongsp}&tong=${total}&sdt=${sdtv}`
        })
        .then(response => response.json()) 
        .then(product => {
            if (product.error) {
                alert(product.error);
                return;
            }
            // Gửi cartData tạo chi tiết hóa đơn
            fetch("../models/xulygiaodich/taoCTHD.php", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(cartData)
            })
            .then(response => response.text())
            .then(data => {
                // Thông báo hoàn tất
                alert(data);
            });
            const now = dayjs();
           

            // Hiển thị hóa đơn
            document.getElementById("day").innerHTML = now.format('DD/MM/YYYY HH:mm:ss');
            document.getElementById("sohd").innerHTML = product.idhd;
            document.getElementById("tiendua").innerHTML = parseInt(tienkhachdua).toLocaleString('vi-VN');
            document.getElementById("tienthoi").innerHTML = product.tienthoi.toLocaleString('vi-VN');

            // Chuyển giao diện 
            chuyengiaodien('purchase','bill');

        })
    }
});

