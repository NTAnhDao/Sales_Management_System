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



// function addProduct(){
//     let soluongfield = document.getElementById('soluong')
//     let maspfield = document.getElementById('product_id')

    
//     let stt = 1
//     let ma = maspfield.value
//     let soluong =soluongfield.value

//     let tr = document.createElement('tr')
//     let td1 = document.createElement('td')
//     let td2 = document.createElement('td')
//     let td3 = document.createElement('td')
//     let td4 = document.createElement('td')
//     let td5 = document.createElement('td')
//     let td6= document.createElement('td')

//     tr.appendChild(td1)
//     tr.appendChild(td2);
//     tr.appendChild(td3);
//     tr.appendChild(td4);
//     tr.appendChild(td5);
//     tr.appendChild(td6);

//     if(ma == '' || soluong == '' ){
//         showError("Vui lòng nhập đủ thông tin!")
//         return false
//     }
//     else{
//         td1.innerHTML = stt
//         td2.innerHTML = tensp
//         td3.innerHTML = giabanle
//         td4.innerHTML = soluong
//         td5.innerHTML = soluong*giabanle
//         td6.innerHTML = '<button onclick = "removeProduct(this)" class="btn btn-danger btn-sm">Delete</button>'
//         stt++
//     }
//     showError(null)
//     let tbody = document.getElementById('tbody') 
//     tbody.appendChild(tr)
//     soluong = ''
//     ma = ''
//     maspfield.focus()
//     return true
//    }

function showError(message,id){
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

function removeProduct(element){
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