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
var total = 0;
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("add_product").addEventListener("click", function () {
        let name = $("#product_id").val().trim();
        let sl = $("#soluong").val().trim();

        if (name === "" || sl === "") {
            annoucement("Vui lòng nhập đủ thông tin!",'err-addsp');
            return;
        }

        // Gửi yêu cầu AJAX đến add.php
        fetch("func/add.php", {
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

            let existingRow = null;
            document.querySelectorAll("#tbody tr").forEach(row => {
                let productName = row.cells[0].innerText; // Cột Tên sản phẩm
                if (productName === product.tensp) {
                    existingRow = row;
                }
            });

            if (existingRow) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng và tổng tiền
                let oldsl= parseInt(existingRow.cells[2].innerText); 
                let newsl = oldsl + product.soluong;
                existingRow.cells[2].innerText = newsl; // Cập nhật số lượng mới
                existingRow.cells[3].innerText = newsl * product.giabanle; // Cập nhật tổng tiền
                total = total + (newsl-oldsl)*product.giabanle;
            } else {
                total += product.tongtien;
            // Thêm dữ liệu vào bảng
                let tableContent = `<tr>
                                    <td>${product.tensp}</td>
                                    <td>${product.giabanle}</td>
                                    <td>${product.soluong}</td>
                                    <td>${product.tongtien}</td>
                                    <td><a onclick="removeProduct(this)""><i class="fa fa-trash" style="font-size:20px;color:#ffa551"></i></a></td>
                                    </tr>`;
                document.getElementById("tbody").innerHTML += tableContent;
            }
            document.getElementById("product_id").focus();
            document.getElementById("total").innerHTML = total;
            annoucement(null,'err-addsp');
        })
        .catch(error => console.error("Lỗi:", error));
    });
});

function removeProduct(element){
    let row = element.closest("tr"); // Tìm thẻ <tr> chứa nút
    let td_tongtien = parseInt(row.children[3].textContent); // Tổng tiền của dòng chọn xóa
    total -= td_tongtien;
    document.getElementById("total").innerHTML = total;
    row.remove();
}



function removeallProduct(){
    document.getElementById("tbody").innerHTML = "";
    total = 0;
    document.getElementById("total").innerHTML = total;

}


// Khi nhan thanh toan
document.getElementById("btnthanhtoan").addEventListener("click", function () {
    let sourceTable = document.getElementById("tbody");
    let destinationTable = document.getElementById("tbodyhd");
    document.getElementById("tong").innerHTML = total;
    document.getElementById("tenkh").innerHTML = tenv;
    document.getElementById("sdtkh").innerHTML = sdtv;

    destinationTable.innerHTML = ""; 

    let selectedCols = [0, 1, 2, 3]; 

    for (let row of sourceTable.rows) {
        let newRow = destinationTable.insertRow();
        for (let i of selectedCols) {
            let newCell = newRow.insertCell();
            newCell.innerHTML = row.cells[i].innerHTML;
        }
    }
}
);


