<?php
session_start();
$userRole = $_SESSION['role'] ?? 'Nhân viên';
?>

<div class="main-content" id="main">
    <main>
        <div class="page-header">
            <div>
                <h3>Quản lý danh mục</h3>
            </div>
        </div>
        <div id="success-message" class="alert alert-success d-none"></div>
        <div id="error-message" class="alert alert-danger d-none"></div>
        <div class="content">
            <div class="actions">
                <?php if ($userRole === 'Admin'): ?>
                    <button type="button" class="btn btn-success" onclick="chuyengiaodien('list-catalog','add-catalog')"><i class="fa fa-plus"></i> Thêm danh mục</button>
                <?php endif; ?>
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
                                <?php if ($userRole === 'Admin'): ?>
                                    <th>Hành động</th>
                                <?php endif; ?>
                            </tr>
                        </thead>
                        <tbody id="catalog-infor">
                            <?php include '<functionQLDM_SP/showCatalog.php'; ?>
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
                                <form id="add-catalog" class="position-relative" action="" method="POST">
                                    <input type="hidden" id="edit-id" name="edit-id">
                                    <div class="form-group">
                                        <label for="name">Tên danh mục</label>
                                        <input
                                            id="name"
                                            name="name"
                                            type="text"
                                            class="form-control"
                                            placeholder="Nhập tên danh mục"
                                        />
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
</DAV>
<script src="functionQLDM_SP/functionQLDM_SP.js" ></script>

<script>
    document.getElementById('add-catalog-btn')?.addEventListener('click', () => {
        isEditMode = false;
        document.getElementById('form-title').innerText = "Thêm danh mục";
        document.getElementById('save-btn').innerText = "Lưu";
        document.getElementById('edit-id').value = '';
        document.getElementById('name').value = '';
        document.getElementById('description').value = '';
        chuyengiaodien('list-catalog', 'add-catalog');
    });
</script>
