document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("add-account").addEventListener("click", function(event) {
        event.preventDefault(); //ngăn chặn form gửi đi theo cách mặc định
        
        //lấy dữ liệu từ form
        let fullname = document.getElementById("fullname").value;
        let email = document.getElementById("emailNV").value;
        let gender = document.querySelector("input[name='gender']");
        let sdt = document.getElementById("SDT").value;

        if(fullname === "" || email === "") {
            alert("Vui lòng nhập đầy đủ thông tin họ tên và email!");
            return;
        }
        
        fetch("functionQLNV/addAcc.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `fullname=${fullname}&email=${email}&gender=${gender}&SDT=${sdt}`
        })
        // .then(response => response.json()) // Chuyển đổi kết quả JSON
        .then(response => response.text())
        .then(text => {
        console.log("Phản hồi từ API:", text);
        return JSON.parse(text);
        })
        .then(result => {
            if(result.success) {
                alert("Tạo tài khoản thành công!");
                //alert(result.mail);
            } else {
                alert("Lỗi: " + result.error);
            }
            
        })
        .catch(error => console.error("Lỗi khi tạo tài khoản:", error));
    })

})