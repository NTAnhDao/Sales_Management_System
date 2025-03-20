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



