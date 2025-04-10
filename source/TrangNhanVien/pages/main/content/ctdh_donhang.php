<div class = "main-content " id="main">
    <main>
        <div class = "page-header">
            <a  href="index.php?sidebar=donhang">
                <h3>Đơn hàng </h3>
            </a>
            <h3> \ Chi tiết đơn hàng </h3>

        </div>
        <div class="content">
            <div class="d-flex justify-content-center">
                <label class = "search-text" for="searchBox" > Tìm kiếm: </label>
                <div  class="searchBox">
                    <i class="fa fa-search btn-search"> </i>
                    <input onkeyup="filterTable(this,'ctdh')" type="text" placeholder="Nhập tên sản phẩm" name="searchBox">
                </div>
            </div>               
             

            <section class="detail-bill">
                
                <?php include("bangCTDH.php"); ?>
                
                <a class='btn-custom btn btn-secondary' href='index.php?sidebar=donhang'>
                    <span class = "	fa fa-arrow-left"></span> Quay lại 
                </a>
            </section>

            
        </div>
       
    </main>
</div> 