var tensp=$("#product_id").val();
var giabanle =  $("#soluong").val();
var bool = true;
function open_closeNav() {
    var sidebar = document.getElementById("mySidebar");
    var main = document.getElementById("main");
    if(!bool){
        sidebar.style.width = "220px";
        main.style.marginLeft = "220px";
        
        bool = true;
    }else{    
        sidebar.style.width = "0";
        main.style.marginLeft= "0";
        bool = false;
    }
}


function changeWebsize() {
    var sidebar = document.getElementById("mySidebar");
    var main = document.getElementById("main");
    if(bool){
        if (window.innerWidth <= 768) {
            sidebar.style.width = "65px";
            main.style.marginLeft = "65px";
        }
        else if(sidebar){
            sidebar.style.width = "220px";
            main.style.marginLeft = "220px";
        }
    }
}

// Gọi hàm lặp lại mỗi 2 giây
setInterval(changeWebsize, 100);



function errormessage(message,id){
    let error = document.getElementById(id)

    if(message == null || message == undefined){
        //hide
        if(!error.classList.contains('d-none')){
            error.classList.add('d-none')
        }
    }else{
        error.classList.remove('d-none')
        error.innerHTML = message
    }
}


function chuyengiaodien(class1,class2){
    let box1 = document.getElementsByClassName(class1) [0]
    let box2 = document.getElementsByClassName(class2) [0]

     if(!box1.classList.contains('d-none')){ 
        //Nếu class1 không có d-none thì thêm vào để ẩn
        box1.classList.add('d-none')
        box2.classList.remove('d-none')
    }else{
        box2.classList.add('d-none')
        box1.classList.remove('d-none')
    }
}

// Tìm kiếm 
function filterTable(input, tableId) {
    const filter = removeVietnameseTones(input.value).toUpperCase();
    const table = document.getElementById(tableId);
    const rows = table.getElementsByTagName("tr");
  
    for (let i = 0; i < rows.length; i++) {
      const cells = rows[i].getElementsByTagName("td");
      if (cells.length > 1) {
        const value = cells[1].textContent || cells[1].innerText;
        const normalizedText = removeVietnameseTones(value.toUpperCase());
        rows[i].style.display = normalizedText.includes(filter) ? "" : "none";
      }
    }
  }
// Loại bỏ dấu tiếng việt
function removeVietnameseTones(str) {
return str.normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d")
            .replace(/Đ/g, "D");
}


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
            url: "modules/xulygiaodich//addcustomer.php",
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

