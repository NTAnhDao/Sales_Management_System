// $(document).ready(function() {
//     $("#search-sdt").submit(function(e) {
//         e.preventDefault(); // Ngăn form tải lại trang

//         $.ajax({
//             type: "POST",
//             url: "func/findsdt.php",
//             data: $(this).serialize(), // Lấy toàn bộ dữ liệu form
//             dataType: "json", // Đọc dữ liệu JSON từ PHP
//             success: function(response) {
//                 if (response.error) {
//                     $("#hoten").html("");
//                     $("#diachi").html("");
//                     $("#err").html(response.error);
//                 } else {
//                     $("#hoten").html("Họ tên: "+ response.name);
//                     $("#diachi").html("Địa ch: " + response.address);
//                     $("#err").html("");
//                 }
//             },
//             error: function() {
//                 $("#hoten").html("");
//                 $("#diachi").html("");
//                 $("#err").html("Có lỗi xảy ra!");
//             }
//         });
//     });
// });

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("search-sdt").addEventListener("click", function () {
        let sdt = $("#customer-phone").val().trim();

        if (sdt === "") {
            showError("Vui lòng nhập đủ thông tin!",'err-findsdt')
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
                showError(product.error,'err-findsdt');
                return;
            }

            document.getElementById("hoten").innerHTML = "Khách hàng: " + product.name + " - " + sdt;
            document.getElementById("diachi").innerHTML = "Địa chỉ: " + product.address;
            showError(null,'err-findsdt')
        })
        .catch(error => console.error("Lỗi:", error));
    });
});


