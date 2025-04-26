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
        } else if (!checkEmail(email)) {
            alert("Vui lòng nhập đúng định dạng email!");
            return;
        } else if (!checkHoten(fullname)) {
            alert("Họ tên không được chứa ký tự đặc biệt hoặc số!");
            return;
        }
        
            
        fetch("../models/nhanvien/addAcc.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `fullname=${fullname}&email=${email}&gender=${gender}&SDT=${sdt}`
        })
        .then(response => response.json()) //Chuyển đổi kết quả JSON
        .then(result => {
            if(result.success) {
                alert("Tạo tài khoản thành công!");
                alert(result.mess);
                //Làm sạch các trường input
                document.getElementById("add-form").reset();
            } else {
                alert("Lỗi: " + result.mess);
            }
            
        })
        .catch(error => console.error("Lỗi khi tạo tài khoản:", error));
    })

})

function checkHoten(input) {
    const alphabetRegex = /^[\p{L}\s]+$/u;
    return alphabetRegex.test(input);
}
  
function checkEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
    return emailRegex.test(email);
}
  
function quaylai() {
    //Làm sạch các trường input khi bỏ dỡ giữa chừng
    document.getElementById("add-form").reset();

    chuyengiaodien('display-content','listAccount');
}