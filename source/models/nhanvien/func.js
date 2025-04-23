document.addEventListener("DOMContentLoaded", function() {
    //Tạo tài khoản
    document.getElementById("add-account").addEventListener("click", function(event) {
        event.preventDefault(); //ngăn chặn form gửi đi theo cách mặc định
        
        //lấy dữ liệu từ form
        let fullname = document.getElementById("fullname").value;
        let email = document.getElementById("emailNV").value;
        
        //lấy giá trị gender(nếu có)
        let gender = document.querySelector("input[name='gender']:checked");
        gender = gender ? gender.value : null;  //nếu không có giá trị, gán null

        let sdt = document.getElementById("SDT").value;

        if(fullname === "" || email === "") {
            alert("Vui lòng nhập đầy đủ thông tin họ tên và email!");
            return;
        }
            
        fetch("../models/nhanvien/addAcc.php", {
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
                alert(result.mess);
            } else {
                alert("Lỗi: " + result.mess);
            }
            
        })
        .catch(error => console.error("Lỗi khi tạo tài khoản:", error));
    })


    
})