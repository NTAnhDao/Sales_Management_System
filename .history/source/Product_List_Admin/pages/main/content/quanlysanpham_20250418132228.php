<div class="main-content" id="main">
    <main>
        <div class="page-header">
            <div>
                <h3>Quản lý sản phẩm</h3>
            </div>
        </div>
        <div id="success-message" class="alert alert-success d-none"></div>
        <div id="error-message" class="alert alert-danger d-none"></div>
        <div class="content">
            <div class="actions">
                <button type="button" class="btn btn-success" onclick="chuyengiaodien('list-product','add-product')"><i class="fa fa-plus"></i> Thêm sản phẩm</button>    
            </div>
            <section class="list-product">
                <div class="card-product">
                    <h5>Danh sách sản phẩm</h5>
                    <table class="cart-table" border="1">
                        <thead>
                            <tr>
                                <th>Mã vạch</th>
                                <th>Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <th>Giá gốc</th>
                                <th>Giá bán lẻ</th>
                                <th>Danh mục</th>
                                <th>Ngày tạo</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody id="product-infor">
                            <?php include 'functionQLDM_SP/ShowProduct.php'; ?>
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="add-product d-none">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 col-sm-12 my-4 mx-auto p-3">
                            <div class="card-product">
                                <h2 class="text-center mb-4" id="form-title">Thêm sản phẩm</h2>
                                <form id="add-product" class="d-flex flex-wrap position-relative" action="" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" id="mavach_old" name="mavach_old">
                                    <div class="form-left col-md-6 pr-3">
                                        <div class="form-group">
                                            <label for="mavach">Mã vạch</label>
                                            <input id="mavach" name="mavach" type="text" class="form-control" placeholder="Nhập mã vạch" />
                                        </div>
                                        <div class="form-group">
                                            <label for="tensp">Tên sản phẩm</label>
                                            <input id="tensp" name="tensp" type="text" class="form-control" placeholder="Nhập tên sản phẩm" />
                                        </div>
                                        <div class="form-group">
                                            <label for="giagoc">Giá gốc</label>
                                            <input id="giagoc" name="giagoc" type="number" step="0.01" class="form-control" placeholder="Nhập giá gốc" />
                                        </div>
                                        <div class="form-group">
                                            <label for="giabanle">Giá bán lẻ</label>
                                            <input id="giabanle" name="giabanle" type="number" step="0.01" class="form-control" placeholder="Nhập giá bán lẻ" />
                                        </div>
                                        <div class="form-group">
                                            <label for="ngaytao">Ngày tạo</label>
                                            <input id="ngaytao" name="ngaytao" type="date" class="form-control" value="<?php echo date('Y-m-d'); ?>" />
                                        </div>
                                        <div class="form-group">
                                            <label for="id_danhmuc">Chọn danh mục</label>
                                            <select id="id_danhmuc" name="id_danhmuc" class="form-control custom-select">
                                                <option value="">Chọn danh mục</option>
                                                <?php
                                                require '../database/db.php';
                                                $conn = connectDB();
                                                $sql = "SELECT id, tendm FROM danhmucsp";
                                                $result = $conn->query($sql);
                                                while ($row = $result->fetch_assoc()) {
                                                    echo "<option value='{$row["id"]}'>{$row["tendm"]}</option>";
                                                }
                                                $conn->close();
                                                ?>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-right col-md-6 pl-3">
                                        <div class="upload-area">
                                            <div class="drop-zone" id="drop-zone">
                                                <p>Kéo và thả tệp vào đây</p>
                                                <p>hoặc</p>
                                                <label for="file-upload" class="btn btn-outline-primary">Chọn tệp</label>
                                                <input type="file" id="file-upload" name="file-upload" class="d-none" accept="image/*" />
                                                <img id="preview-img" src="" alt="Preview" style="display: none; max-width: 100%; margin-top: 10px;" />
                                            </div>
                                            <div class="file-info" id="file-info" style="display: none;">
                                                <p><strong>Tên tệp:</strong> <span id="file-name"></span></p>
                                                <p><strong>Thư mục:</strong> <span>images/</span></p>
                                                <p><strong>Loại tệp:</strong> <span id="file-type"></span></p>
                                                <p><strong>Kích thước:</strong> <span id="file-size"></span> bytes</p>
                                            </div>
                                            <div class="file-actions">
                                                <button type="button" class="btn btn-primary" id="save-btn">Lưu file</button>
                                                <button type="button" class="btn btn-danger" id="cancel-file-btn" onclick="resetFileUpload()">Hủy file</button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-12 mt-3">
                                        <button type="button" class="btn btn-secondary" onclick="chuyengiaodien('add-product', 'list-product')">Quay lại</button> 
                                        <button type="submit" class="btn btn-success" id="submit-btn">Lưu</button> 
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main>
</div>