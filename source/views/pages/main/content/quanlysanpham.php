<?php

$userRole = $_SESSION['role'] ?? 'Nhân viên';
?>

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
                <?php if ($userRole === 'Admin'): ?>
                    <button type="button" class="btn btn-success" onclick="chuyengiaodien('list-products','add-product')"><i class="fa fa-plus"></i> Thêm sản phẩm</button>
                <?php endif; ?>
            </div>
            <section class="list-products">
                <div class="card-product">
                    <h5>Danh sách sản phẩm</h5>
                    <table class="cart-table" >
                        <thead>
                            <tr>
                                <th>Mã vạch</th>
                                <th>Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <?php if ($userRole === 'Admin'): ?>
                                    <th>Giá nhập khẩu (giá gốc)</th>
                                <?php endif; ?>
                                <th>Giá bán lẻ</th>
                                <th>Danh mục</th>
                                <th>Ngày tạo</th>
                                <?php if ($userRole === 'Admin'): ?>
                                    <th>Hành động</th>
                                <?php endif; ?>
                            </tr>
                        </thead>
                        <tbody id="product-infor">
                            <?php include '../models/danhmuc_sanpham/ShowProduct.php'; ?>
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
                                <form id="add-product" class="d-flex flex-wrap position-relative" action="" method="POST">
                                    <input type="hidden" id="edit-mavach-old" name="mavach_old">
                                    <div class="form-left col-md-6 pr-3">
                                        <div class="form-group">
                                            <label for="mavach">Mã vạch</label>
                                            <input id="mavach" name="mavach" type="text" class="form-control" placeholder="Nhập mã vạch" />
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Tên sản phẩm</label>
                                            <input id="name" name="name" type="text" class="form-control" placeholder="Nhập tên sản phẩm" />
                                        </div>
                                        <div class="form-group">
                                            <label for="import-price">Giá nhập khẩu (giá gốc)</label>
                                            <input id="import-price" name="import-price" type="number" step="0.01" class="form-control" placeholder="Nhập giá gốc" />
                                        </div>
                                        <div class="form-group">
                                            <label for="retail-price">Giá bán lẻ</label>
                                            <input id="retail-price" name="retail-price" type="number" step="0.01" class="form-control" placeholder="Nhập giá bán lẻ" />
                                        </div>
                                        <input id="date" name="date" type="hidden" value="<?php echo date('Y-m-d'); ?>" />
                                        <div class="form-group">
                                            <label for="type">Chọn danh mục</label>
                                            <select id="type" name="type" class="form-control custom-select">
                                                <?php
                                                require '../database/db.php';
                                                $conn = connectDB();
                                                // Change the SQL to select both id and tendm
                                                $sql = "SELECT id, tendm FROM danhmucsp";
                                                $result = $conn->query($sql);
                                                while ($row = $result->fetch_assoc()) {
                                                    // Fix the option tag to include the category name
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
                                                <input type="file" id="file-upload" name="file-upload" accept="image/*" />
                                                <label for="file-upload" class="btn btn-outline-primary">Chọn tệp</label>
                                            </div>
                                            <div class="file-info" id="file-info">
                                                <p><strong>Tên tệp:</strong> <span id="file-name">Không có hình ảnh</span></p>
                                                <p><strong>Thư mục:</strong> <span>functionQLDM_SP/image/</span></p>
                                                <p><strong>Loại tệp:</strong> <span id="file-type"></span></p>
                                                <p><strong>Kích thước:</strong> <span id="file-size"></span> bytes</p>
                                            </div>
                                            <div class="file-actions">
                                                <button type="button" class="btn btn-danger" id="cancel-file-btn">Hủy file</button>
                                            </div>
                                            <img id="preview-img" src="" alt="Preview" style="display: none; max-width: 100%; margin-top: 10px;" />
                                        </div>
                                    </div >
                                    <div class = "actions">
                                        <button type="button" class="btn btn-secondary" onclick="chuyengiaodien('add-product', 'list-products')">Quay lại</button> 
                                        <button type="submit" class="btn btn-success" id="save-btn">Lưu</button> 
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
<script src="../models/danhmuc_sanpham/functionForProduct.js"></script>