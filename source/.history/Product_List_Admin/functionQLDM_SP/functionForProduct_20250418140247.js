let isEditModeProduct = false;
let fileToUpload = null;

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

document.addEventListener('DOMContentLoaded', function() {
    const dropZone = document.getElementById('drop-zone');
    const fileInput = document.getElementById('file-upload');
    const fileName = document.getElementById('file-name');
    const fileType = document.getElementById('file-type');
    const fileSize = document.getElementById('file-size');
    const previewImg = document.getElementById('preview-img');
    const cancelFileBtn = document.getElementById('cancel-file-btn');

    if (!dropZone || !fileInput || !fileName || !fileType || !fileSize || !previewImg || !cancelFileBtn) {
        console.error("Không tìm thấy một hoặc nhiều phần tử HTML cần thiết!");
        return;
    }

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.classList.add('dragover');
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('dragover');
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.classList.remove('dragover');
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files;
            handleFile(files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        } else {
            resetFileInfo();
        }
    });

    cancelFileBtn.addEventListener('click', () => {
        fileInput.value = '';
        resetFileInfo();
        fileToUpload = null;
        announcement("File đã được hủy!", 'success-message');
    });

    function handleFile(file) {
        fileToUpload = file;
        fileName.textContent = file.name || 'Không có tên file';
        fileType.textContent = file.type || 'Không xác định';
        fileSize.textContent = (file.size / 1024).toFixed(2) + ' KB' || '0 KB';

        if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                previewImg.style.display = 'block';
            };
            reader.onerror = function() {
                console.error("Lỗi khi đọc file hình ảnh!");
                previewImg.src = '';
                previewImg.style.display = 'none';
            };
            reader.readAsDataURL(file);
        } else {
            previewImg.src = '';
            previewImg.style.display = 'none';
            announcement("Vui lòng chọn file hình ảnh!", 'error-message');
        }
    }

    function resetFileInfo() {
        fileName.textContent = '';
        fileType.textContent = '';
        fileSize.textContent = '';
        previewImg.src = '';
        previewImg.style.display = 'none';
    }
});

function addProduct(event) {
    event.preventDefault();

    let mavach_old = document.getElementById('edit-mavach-old').value;
    let mavach = document.getElementById('mavach').value;
    let tensp = document.getElementById('name').value;
    let giagoc = parseFloat(document.getElementById('import-price').value);
    let giabanle = parseFloat(document.getElementById('retail-price').value);
    let id_danhmuc = document.getElementById('type').value;
    let ngaytao = document.getElementById('date').value;
    let fileInput = document.getElementById('file-upload');

    if (!mavach || !tensp || giagoc <= 0 || giabanle <= 0 || !id_danhmuc) {
        announcement("Vui lòng nhập đầy đủ thông tin sản phẩm, bao gồm mã vạch!", 'error-message');
        return;
    }

    if (!isEditModeProduct && fileInput.files.length === 0) {
        announcement("Vui lòng chọn hình ảnh cho sản phẩm!", 'error-message');
        return;
    }

    let url = isEditModeProduct ? "functionQLDM_SP/editProduct.php" : "functionQLDM_SP/addProduct.php";
    let formData = new FormData();
    formData.append('mavach', mavach);
    formData.append('mavach_old', mavach_old);
    formData.append('tensp', tensp);
    formData.append('giagoc', giagoc);
    formData.append('giabanle', giabanle);
    formData.append('id_danhmuc', id_danhmuc);
    formData.append('ngaytao', ngaytao);
    if (fileInput.files.length > 0) {
        formData.append('file-upload', fileInput.files[0]);
    }

    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function(response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, 'success-message');
                window.location.reload();
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

function editProduct(row) {
    const mavach = row.getAttribute('data-mavach');
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", 'error-message');
        return;
    }
    const cells = row.getElementsByTagName('td');
    if (cells.length < 8) {
        announcement("Dữ liệu bảng không đúng định dạng!", 'error-message');
        return;
    }
    const tensp = cells[2].innerText;
    const giagoc = parseFloat(cells[3].innerText.replace(/[^\d]/g, ''));
    const giabanle = parseFloat(cells[4].innerText.replace(/[^\d]/g, ''));
    const id_danhmuc = row.getAttribute('data-id_danhmuc') || document.querySelector(`#type option:contains("${cells[5].innerText}")`)?.value;
    const ngaytao = cells[6].innerText;
    const img = cells[1].querySelector('img')?.src.split('/').pop();

    document.getElementById('edit-mavach-old').value = mavach;
    document.getElementById('mavach').value = mavach;
    document.getElementById('name').value = tensp;
    document.getElementById('import-price').value = giagoc;
    document.getElementById('retail-price').value = giabanle;
    document.getElementById('type').value = id_danhmuc;
    document.getElementById('date').value = ngaytao.split(' ')[0];

    if (img) {
        document.getElementById('file-name').textContent = img;
        document.getElementById('preview-img').src = `images/${img}`;
        document.getElementById('preview-img').style.display = 'block';
    } else {
        document.getElementById('file-name').textContent = '';
        document.getElementById('preview-img').src = '';
        document.getElementById('preview-img').style.display = 'none';
    }

    isEditModeProduct = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa sản phẩm";
    document.getElementById('save-btn').innerText = "Cập nhật";
    chuyengiaodien('list-product', 'add-product');
}

function deleteProduct(row) {
    const mavach = row.getAttribute('data-mavach');
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", 'error-message');
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
        $.ajax({
            url: "functionQLDM_SP/deleteProduct.php",
            type: "POST",
            data: { mavach: mavach },
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

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('add-product');
    if (form) {
        form.addEventListener('submit', addProduct);
    } else {
        console.log("Không tìm thấy form với id 'add-product'");
    }

    const productTable = document.getElementById('product-infor');
    if (productTable) {
        productTable.addEventListener('click', function(e) {
            if (e.target.classList.contains('edit-btn')) {
                e.preventDefault();
                const row = e.target.closest('tr');
                editProduct(row);
            }
            if (e.target.classList.contains('delete-btn')) {
                e.preventDefault();
                const row = e.target.closest('tr');
                deleteProduct(row);
            }
        });
    } else {
        console.log("Không tìm thấy tbody với id 'product-infor'");
    }
});