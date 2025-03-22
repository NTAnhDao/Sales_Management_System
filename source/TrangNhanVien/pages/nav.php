<nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top" id="nav">
    <div class="container-fluid">
        <div class="leftnav justify-content-start">
            <a class="logo navbar-brand" href="index.php">
                <img src="images/logo.png" alt="Avatar Logo" style="width:40px;" > 
                <h4>Point of Sale</h4>
            </a>
            <div class="openclose"><span class="navbar-toggler-icon" style="font-size:30px;cursor:pointer" onclick="open_closeNav() " class = "las la-bars"></span>  </div>
            
        </div>

        <div class="rightnav justify-content-end">
            <ul  class=" navbar-nav" >
                <li class="nav-item">
                    <a  href="#">
                    <span class = "fa fa-fw fa-envelope"></span>  
                    </a></li>
                <li class="nav-item">
                    <a  href="#">
                    <span class = "fa fa-bell"></span> 
                    </a></li>
                <li class="nav-item dropdown">
                        <a id="nav-user" class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">

                            <img id="nav-avatar" src = "images/trai_tre.jpeg" height = "30px"  width = "30px" alt = "Employee">
                            <div class="infor">
                                <p id ="user">Admin</p>
                                <p id="email">admin@gmail.com</p>
                            </div>
                        </a>
            
                    <ul class="dropdown-menu">
                        <li>
                            <a class="dropdown-item" href="index.php?nav=hoso">
                                <i class = "fa fa-user fa-fw "> </i> 
                                <span>Hồ sơ</span>
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="logout.php">
                                <i class = "fa fa-sign-out fa-fw "> </i> 
                                <span>Đăng xuất </span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div> 
    </div>
    
</nav>