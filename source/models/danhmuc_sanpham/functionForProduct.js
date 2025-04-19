let isEditModeProduct = false;

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

function deleteProduct(row) {
    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền xóa sản phẩm!", 'error-message');
        return;
    }

    const mavach = row.getAttribute('data-mavach') || row.querySelector('.delete-btn')?.getAttribute('data-mavach');
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", 'error-message');
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
        $.ajax({
            url: "deleteProduct.php",
            type: "POST",
            data: { mavach: mavach },
            dataType: "json",
            success: function(response) {
                console.log("Phản hồi từ server:", response);
                if (response.success) {
                    announcement(response.message, 'success-message');
                    window.location.reload(); // Tải lại trang sau khi xóa thành công
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

function editProduct(row) {
    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền chỉnh sửa sản phẩm!", 'error-message');
        return;
    }

    const mavach = row.getAttribute('data-mavach');
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", 'error-message');
        return;
    }

    const cells = row.getElementsByTagName('td');
    if (cells.length < 6) {
        announcement("Dữ liệu bảng không đúng định dạng!", 'error-message');
        return;
    }

    const tensp = cells[2].innerText;
    const giagoc = cells[3].innerText.replace(/[^0-9.-]+/g, '');
    const giabanle = cells[4].innerText.replace(/[^0-9.-]+/g, '');
    const id_danhmuc = row.querySelector('.edit-btn').getAttribute('data-id_danhmuc');
    const img = row.querySelector('.edit-btn').getAttribute('data-img') || '';

    document.getElementById('edit-mavach-old').value = mavach;
    document.getElementById('mavach').value = mavach;
    document.getElementById('name').value = tensp;
    document.getElementById('import-price').value = giagoc;
    document.getElementById('retail-price').value = giabanle;
    document.getElementById('type').value = id_danhmuc;
    document.getElementById('file-name').textContent = img || 'Không có hình ảnh';
    document.getElementById('file-type').textContent = img ? 'image' : '';
    document.getElementById('file-size').textContent = '';
    document.getElementById('preview-img').src = img ? '../images/' + img : '';
    document.getElementById('preview-img').style.display = img ? 'block' : 'none';

    isEditModeProduct = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa sản phẩm";
    document.getElementById('save-btn').innerText = "Cập nhật";
    chuyengiaodien('list-product', 'add-product');
}

function addProduct(event) {
    event.preventDefault();

    if (userRole !== 'Admin') {
        announcement("Bạn không có quyền thêm sản phẩm!", 'error-message');
        return;
    }

    let mavach_old = document.getElementById('edit-mavach-old').value;
    let mavach = document.getElementById('mavach').value;
    let tensp = document.getElementById('name').value;
    let giagoc = document.getElementById('import-price').value;
    let giabanle = document.getElementById('retail-price').value;
    let id_danhmuc = document.getElementById('type').value;
    let fileInput = document.getElementById('file-upload');

    if (!mavach || !tensp || !giagoc || !giabanle || !id_danhmuc) {
        announcement("Vui lòng nhập đầy đủ thông tin!", 'error-message');
        return;
    }

    let url = isEditModeProduct ? "editProduct.php" : "addProduct.php";
    let formData = new FormData();
    formData.append('mavach_old', mavach_old);
    formData.append('mavach', mavach);
    formData.append('tensp', tensp);
    formData.append('giagoc', giagoc);
    formData.append('giabanle', giabanle);
    formData.append('id_danhmuc', id_danhmuc);
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, 'success-message');
                window.location.reload(); // Tải lại trang sau khi thêm/sửa thành công
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

// Xử lý upload file (hiển thị preview)
document.addEventListener('DOMContentLoaded', function() {
    const dropZone = document.getElementById('drop-zone');
    const fileInput = document.getElementById('file-upload');
    const fileName = document.getElementById('file-name');
    const fileType = document.getElementById('file-type');
    const fileSize = document.getElementById('file-size');
    const previewImg = document.getElementById('preview-img');
    const cancelFileBtn = document.getElementById('cancel-file-btn');

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = '#007bff';
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.style.borderColor = '#ccc';
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = '#ccc';
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            handleFile(files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        }
    });

    function handleFile(file) {
        fileName.textContent = file.name;
        fileType.textContent = file.type;
        fileSize.textContent = file.size;
        previewImg.src = URL.createObjectURL(file);
        previewImg.style.display = 'block';
    }

    cancelFileBtn.addEventListener('click', () => {
        fileInput.value = '';
        fileName.textContent = 'Không có hình ảnh';
        fileType.textContent = '';
        fileSize.textContent = '';
        previewImg.src = '';
        previewImg.style.display = 'none';
    });

    const productForm = document.getElementById('add-product');
    if (productForm && userRole === 'Admin') {
        productForm.addEventListener('submit', addProduct);
    } else if (productForm) {
        productForm.style.display = 'none'; // Ẩn form thêm sản phẩm cho nhân viên
    }

    const productTable = document.getElementById('product-infor');
    if (productTable) {
        productTable.addEventListener('click', function(e) {
            const row = e.target.closest('tr');
            if (!row) return;

            if (e.target.classList.contains('edit-btn') && userRole === 'Admin') {
                e.preventDefault();
                editProduct(row);
            }
            if (e.target.classList.contains('delete-btn') && userRole === 'Admin') {
                e.preventDefault();
                deleteProduct(row);
            }
        });
    }
});