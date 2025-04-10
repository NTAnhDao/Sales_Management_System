var hoten = document.getElementById("hoten");
var diachi = document.getElementById("diachi");
var idkh = "";
var btnfindsdt = document.getElementById("search-sdt");
// Tìm số điện thoại
document.addEventListener("DOMContentLoaded", function () {
    btnfindsdt.addEventListener("click", function () {
        let sdt = $("#customer-phone").val().trim();

        if (sdt === "") {
            annoucement("Vui lòng nhập số điện thoại!",'err-findsdt')
            return;  
        }

        fetch("func/findsdt.php", {
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
                annoucement(null,'err-findsdt');
                annoucement(null ,'suc-addkh');
            }

        })
        .catch(error => console.error("Lỗi:", error));
    });
});
// thêm vào hóa đơn
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("add_product").addEventListener("click", function () {
        let name = $("#product_id").val().trim();
        let sl = $("#soluong").val().trim();

        if (name === "" || sl === "") {
            annoucement("Vui lòng nhập đủ thông tin!",'err-addsp');
            return;
        }

        // Gửi yêu cầu AJAX đến add.php
        fetch("func/addsptocart.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${name}&sl=${sl}`
        })
        .then(response => response.json()) // Chuyển đổi kết quả JSON
        .then(product => {
            if (product.error) {
                annoucement(product.error,'err-addsp');
                return;
            }
            tongsp += parseInt(sl);

            let existingRow = null;
            document.querySelectorAll("#tbody tr").forEach(row => {
                let productName = row.cells[0].innerText; // Cột Tên sản phẩm
                if (productName === product.tensp) {
                    existingRow = row;
                }
            });

            if (existingRow) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng và tổng tiền
                let quantityCell = existingRow.cells[2];
                let quantity = quantityCell.querySelector('input[name="quantity"]').value;
                let oldsl= parseInt(quantity); 
                let newsl = oldsl + product.soluong;
                quantityCell.querySelector('input[name="quantity"]').value = newsl; // Cập nhật số lượng mới
                existingRow.cells[3].innerText = newsl * product.giabanle; // Cập nhật tổng tiền
                total = total + (newsl-oldsl)*product.giabanle;
            } else {
                total += product.tongtien;
            // Thêm dữ liệu vào bảng
                let tableContent = `<tr>
                                    <td>${product.tensp}</td>
                                    <td>${product.giabanle}</td>
                                    <td>
                                    <div class="modify-sl">
                                        <button class="btn-updown" type="button" onclick="decreaseQuantity(this)">-</button>
                                        <input class = "soluong-input" type="text" name="quantity" value="${product.soluong}" readonly>
                                        <button class="btn-updown" type="button" onclick="increaseQuantity(this)">+</button>
                                    </div>
                                   
                                    </td>
                                    <td>${product.tongtien}</td>
                                    <td><a onclick="removeProduct(this)""><i class="fa fa-trash" style="font-size:20px;color:#ffa551"></i></a></td>
                                    </tr>`;
                document.getElementById("tbody").innerHTML += tableContent;
            }
            document.getElementById("product_id").focus();
            document.querySelectorAll(".total").forEach(el => {
                el.innerHTML = total.toLocaleString('vi-VN');
            });
            annoucement(null,'err-addsp');
        })
        .catch(error => console.error("Lỗi:", error));
    });
});


function increaseQuantity(button) {
    let input = button.parentElement.querySelector('input[name="quantity"]');
    let value = parseInt(input.value) || 1;
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

function decreaseQuantity(button) {
    let input = button.parentElement.querySelector('input[name="quantity"]');
    let value = parseInt(input.value) || 1;
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



function removeallProduct(){
    document.getElementById("tbody").innerHTML = "";
    total = tongsp = 0;
    document.querySelectorAll(".total").forEach(el => {
        el.innerHTML = total.toLocaleString('vi-VN');
    });

}


// Khi nhan thanh toan
document.getElementById("btnthanhtoan").addEventListener("click", function () {
    let sourceTable = document.getElementById("tbody");
    let destinationTable = document.getElementById("tbodyhd");
    document.getElementById("tenkh").innerHTML = tenv;
    document.getElementById("sdtkh").innerHTML = sdtv;

    destinationTable.innerHTML = ""; 

    let selectedCols = [0, 1, 2, 3]; 

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
        fetch("func/taoHD.php", {
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
            fetch("func/taoCTHD.php", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(cartData)
            })
            .then(response => response.text())
            .then(data => {
                alert(data);
                // Chuyển giao diện hoặc thông báo hoàn tất
            });
            const now = dayjs();
           

            // Hiển thị hóa đơn
            document.getElementById("day").innerHTML = now.format('DD/MM/YYYY HH:mm:ss');
            document.getElementById("sohd").innerHTML = product.idhd;
            document.getElementById("tiendua").innerHTML = parseInt(tienkhachdua).toLocaleString('vi-VN');
            document.getElementById("tienthoi").innerHTML = product.tienthoi.toLocaleString('vi-VN');
            chuyengiaodien('purchase','bill');

        })

        
    
    }

});


