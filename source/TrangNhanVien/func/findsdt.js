var hoten = document.getElementById("hoten");
var diachi = document.getElementById("diachi");
var idkh = "";
var btnfindsdt = document.getElementById("search-sdt");
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


