let isEditMode = false;

// Lấy vai trò người dùng từ server
let userRole = 'Nhân viên';
$.ajax({
    url: 'getUserRole.php',
    type: 'GET',
    dataType: 'json',
    async: false,
    success: function(response) {
        if (response.success) {
            userRole = response.role;
        }
    },
    error: function() {
        console.error('Không thể lấy vai trò người dùng');
    }
});

function announcement(message, type) {
    const successDiv = document.getElementById('success-message');
    const errorDiv = document.getElementById('error-message');

    successDiv.classList.add('d-none');
    errorDiv.classList.add('d-none');

    if (type === 'success-message') {
        successDiv.innerText = message;
        successDiv.classList.remove('d-none');
    } else if (type === 'error-message') {
        errorDiv.innerText = message;
        errorDiv.classList.remove('d-none');
    }
    setTimeout(() => {
        successDiv.classList.add('d-none');
        errorDiv.classList.add('d-none');
    }, 3000);
}

function deleteCategory(row) {
    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền xóa danh mục!", 'error-message');
        return;
    }

    const id = row.getAttribute('data-id') || row.querySelector('.delete-btn')?.getAttribute('data-id');
    if (!id) {
        announcement("Không tìm thấy ID của danh mục!", 'error-message');
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        $.ajax({
            url: "deleteCatalog.php",
            type: "POST",
            data: { id: id },
            dataType: "json",
            success: function(response) {
                console.log("Phản hồi từ server:", response);
                if (response.success) {
                    row.remove();
                    announcement(response.message, 'success-message');
                } else {
                    announcement(response.message, 'error-message');
                }
            },
            error: function(xhr, status, error) {
                console.log("Lỗi AJAX:", status, error);
                console.log("Phản hồi từ server:", xhr.responseText);
                announcement("Lỗi khi gửi yêu cầu đến server: " + error, 'error-message');
            }
        });
    }
}

function editCategory(row) {
    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền chỉnh sửa danh mục!", 'error-message');
        return;
    }

    const id = row.getAttribute('data-id');
    if (!id) {
        announcement("Không tìm thấy ID của danh mục!", 'error-message');
        return;
    }

    const cells = row.getElementsByTagName('td');
    if (cells.length < 5) {
        announcement("Dữ liệu bảng không đúng định dạng!", 'error-message');
        return;
    }

    const tendm = cells[1].innerText;
    const mota = cells[2].innerText === 'Không có mô tả' ? '' : cells[2].innerText;

    document.getElementById('edit-id').value = id;
    document.getElementById('name').value = tendm;
    document.getElementById('description').value = mota;

    isEditMode = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa danh mục";
    document.getElementById('save-btn').innerText = "Cập nhật";
    chuyengiaodien('list-catalog', 'add-catalog');
}

function addCategory(event) {
    event.preventDefault();

    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền thêm danh mục!", 'error-message');
        return;
    }

    let id = document.getElementById('edit-id').value;
    let tendm = document.getElementById('name').value;
    let mota = document.getElementById('description').value;

    if (!tendm) {
        announcement("Vui lòng nhập tên danh mục!", 'error-message');
        return;
    }

    let url = isEditMode ? "editCatalog.php" : "addCatalog.php";
    let data = { id: id, tendm: tendm, mota: mota };

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        success: function(response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, 'success-message');
                window.location.reload(); // Tải lại trang để hiển thị người tạo mới
            } else {
                announcement(response.message, 'error-message');
            }
        },
        error: function(xhr, status, error) {
            console.log("Lỗi AJAX:", status, error);
            console.log("Phản hồi từ server:", xhr.responseText);
            announcement("Lỗi khi gửi yêu cầu đến server: " + error, 'error-message');
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const catalogForm = document.getElementById('add-catalog');
    const catalogTable = document.getElementById('catalog-infor');
    const addCatalogButton = document.querySelector('.actions .btn-success'); // Nút thêm danh mục

    // Ẩn form thêm danh mục và nút thêm nếu không phải admin
    if (userRole !== 'Admin') {
        if (catalogForm) {
            catalogForm.style.display = 'none';
        }
        if (addCatalogButton) {
            addCatalogButton.style.display = 'none';
        }
        // Ẩn các nút chỉnh sửa và xóa trong bảng nếu không phải admin
        if (catalogTable) {
            catalogTable.addEventListener('click', function(e) {
                if (e.target.classList.contains('edit-btn') || e.target.classList.contains('delete-btn')) {
                    e.preventDefault();
                    announcement("Bạn không có quyền thực hiện thao tác này!", 'error-message');
                }
            });
        }
    } else {
        // Chỉ thêm event listener cho form và bảng nếu là admin
        if (catalogForm) {
            catalogForm.addEventListener('submit', addCategory);
        }
        if (catalogTable) {
            catalogTable.addEventListener('click', function(e) {
                const row = e.target.closest('tr');
                if (!row) return;

                if (e.target.classList.contains('edit-btn')) {
                    e.preventDefault();
                    editCategory(row);
                }
                if (e.target.classList.contains('delete-btn')) {
                    e.preventDefault();
                    deleteCategory(row);
                }
            });
        }
    }

    // Loại bỏ hoặc điều kiện hóa event listener cho nút xóa tất cả nếu cần
    const deleteAllButton = document.querySelector('.btn-danger');
    if (deleteAllButton && userRole === 'Admin') {
        deleteAllButton.addEventListener('click', function(e) {
            e.preventDefault();
            // Thêm logic xóa tất cả danh mục ở đây nếu cần
            alert("Chức năng xóa tất cả chưa được triển khai!");
        });
    } else if (deleteAllButton) {
        deleteAllButton.style.display = 'none'; // Ẩn nút xóa tất cả nếu không phải admin
    }
});