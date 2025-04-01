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
        else{
            sidebar.style.width = "220px";
            main.style.marginLeft = "220px";
        }
    }
}

// Gọi hàm lặp lại mỗi 2 giây
setInterval(changeWebsize, 100);



function annoucement(message,id){
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



function removeallProduct(element){
    element.parentElement.parentElement.remove()
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


var isNew = false;
var tenv = "";
var diachiv = "";
var sdtv = "";
// Kiểm tra xem đã nhập thông tin khách hàng chưa
function checkData() {
    tenv = document.getElementById("enter-name").value;
    diachiv = document.getElementById("enter-diachi").value;
    sdtv = document.getElementById("customer-phone").value;
    document.getElementById("enter-name").focus();
    if (sdtv === "" || tenv === "" || diachiv === "" ){
        annoucement("Lỗi. Vui lòng nhập đầy đủ thông tin khách hàng!", 'err-addkh');
        return;
    }else{
        let nhapcustomer = bootstrap.Modal.getInstance(document.getElementById('nhapcustomer'));
        nhapcustomer.hide(); 
        isNew = true;
    } 
}

// Thêm thông tin khách hàng khi nhấn thanh toán
function insertData() { 
    if(isNew){
        tenv = document.getElementById("enter-name").value;
        diachiv = document.getElementById("enter-diachi").value;
        sdtv = document.getElementById("customer-phone").value;
        if (sdtv === "" || tenv === "" || diachiv === "" ){
            annoucement("Vui lòng nhập đầy đủ thông tin khách hàng trước khi thanh toán!", 'err-findsdt');
            return;
        }
        $.ajax({
            
            url: "func/addcustomer.php",
            type: "POST",
            data: { ten: tenv, diachi: diachiv, sdt: sdtv},
            success: function() {
                annoucement(null,'err-addkh');
                annoucement("Đã thêm khách hàng: " + ten + "-"+sdt + "-" + diachi ,'suc-addkh');
            }
        });
        isNew = false;
    }
    chuyengiaodien('purchase','bill');
}

