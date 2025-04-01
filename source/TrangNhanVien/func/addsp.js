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
