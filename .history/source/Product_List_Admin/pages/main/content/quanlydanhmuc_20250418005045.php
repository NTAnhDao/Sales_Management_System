<div class="main-content" id="main">
    <main>
        <div class="page-header">
            <div>
                <h3>Quản lý danh mục</h3>
            </div>
        </div>
        
        <div class="content">
            <div class="actions">
                <button type="button" class="btn btn-success" onclick="chuyengiaodien('list-catalog','add-catalog')"><i class="fa fa-plus"></i> Thêm danh mục</button>
            </div>
            <section class="list-catalog">
                <div class="card-catalog">
                    <h5>Danh mục sản phẩm</h5>
                    <table class="cart-table" border="1">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Tên danh mục</th>
                                <th>Mô tả</th>
                                <th>Ngày tạo</th>
                                <th>Người tạo</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody id="catalog-infor">
                            <?php include 'functionQLDM_SP/ShowCatalog.php'; ?>
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="add-catalog d-none">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 col-sm-12 my-4 mx-auto p-3">
                            <div class="card-product">
                                <h2 class="text-center mb-4" id="form-title">Thêm danh mục</h2>
                                <!-- Thêm phần hiển thị thông báo -->
                                <div id="error-message" class="alert alert-danger d-none" role="alert"></div>
                                <div id="success-message" class="alert alert-success d-none" role="alert"></div>
                                <form id="add-catalog" class="position-relative" action="" method="POST">
                                    <input type="hidden" id="edit-id" name="edit-id">
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="name">Tên danh mục</label>
                                            <input
                                                id="name"
                                                name="name"
                                                type="text"
                                                class="form-control"
                                                placeholder="Nhập tên danh mục"
                                            />
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="date">Ngày tạo</label>
                                            <input
                                                id="date"
                                                name="date"
                                                type="date"
                                                class="form-control"
                                            />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="description">Mô tả</label>
                                        <textarea
                                            id="description"
                                            name="description"
                                            class="form-control"
                                            placeholder="Nhập mô tả"
                                            style="height: 100px"
                                        ></textarea>
                                    </div>
                                    <button type="button" class="btn btn-secondary" onclick="chuyengiaodien('add-catalog', 'list-catalog')">Quay lại</button> 
                                    <button type="submit" class="btn btn-success" id="save-btn">Lưu</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main>
</div>
<script src="functionQLDM_SP/functionQLDM_SP.js"></script>