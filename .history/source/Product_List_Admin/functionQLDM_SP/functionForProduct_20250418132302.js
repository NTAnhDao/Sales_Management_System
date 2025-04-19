let isEditModeProduct = false;

function announcement(message, type) {
    const successDiv = document.getElementById('success-message');
    const errorDiv = document.getElementById('error-message');

    if (successDiv) successDiv.classList.add('d-none');
    if (errorDiv) errorDiv.classList.add('d-none');

    if (type === 'success-message' && successDiv) {
        successDiv.innerText = message;
        successDiv.classList.remove('d-none');
    } else if (type === 'error-message' && errorDiv) {
        errorDiv.innerText = message;
        errorDiv.classList.remove('d-none');
    }
    setTimeout(() => {
        if (successDiv) successDiv.classList.add('d-none');
        if (errorDiv) errorDiv.classList.add('d-none');
    }, 3000);
}

function chuyengiaodien(fromSection, toSection) {
    const fromElement = document.querySelector(`.${fromSection}`);
    const toElement = document.querySelector(`.${toSection}`);
    if (fromElement && toElement) {
        fromElement.classList.add('d-none');
        toElement.classList.remove('d-none');

        const form = document.getElementById('add-product');
        if (form && toSection === 'add-product') {
            const inputs = form.querySelectorAll('input, textarea, select, button');
            inputs.forEach(input => input.disabled = false);
            setTimeout(() => {
                attachFormEvent();
            }, 100);
        }
    } else {
        console.error(`Không tìm thấy section: ${fromSection} hoặc ${toSection}`);
    }
}

function previewImage(event) {
    const file = event.target.files[0];
    const previewImg = document.getElementById('preview-img');
    const fileInfo = document.getElementById('file-info');
    const fileName = document.getElementById('file-name');
    const fileType = document.getElementById('file-type');
    const fileSize = document.getElementById('file-size');

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            previewImg.src = e.target.result;
            previewImg.style.display = 'block';
            fileInfo.style.display = 'block';
            fileName.textContent = file.name;
            fileType.textContent = file.type;
            fileSize.textContent = file.size;
        };
        reader.readAsDataURL(file);
    } else {
        resetFileUpload();
    }
}

function resetFileUpload() {
    const fileInput = document.getElementById('file-upload');
    const previewImg = document.getElementById('preview-img');
    const fileInfo = document.getElementById('file-info');
    const fileName = document.getElementById('file-name');
    const fileType = document.getElementById('file-type');
    const fileSize = document.getElementById('file-size');

    if (fileInput) fileInput.value = '';
    if (previewImg) {
        previewImg.src = '';
        previewImg.style.display = 'none';
    }
    if (fileInfo) fileInfo.style.display = 'none';
    if (fileName) fileName.textContent = '';
    if (fileType) fileType.textContent = '';
    if (fileSize) fileSize.textContent = '';
}

function reloadProductTable() {
    const tbody = document.getElementById('product-infor');
    if (!tbody) {
        console.error("Không tìm thấy tbody với id 'product-infor'");
        return;
    }

    $.ajax({
        url: "functionQLDM_SP/ShowProduct.php",
        type: "GET",
        dataType: "html",
        success: function(response) {
            tbody.innerHTML = response;
        },
        error: function(xhr, status, error) {
            console.log("Lỗi khi reload bảng sản phẩm:", status, error);
            announcement("Lỗi khi tải lại danh sách sản phẩm: " + error, 'error-message');
        }
    });
}

function addProduct(event) {
    event.preventDefault();

    const form = document.getElementById('add-product');
    if (!form) {
        announcement("Không tìm thấy form thêm sản phẩm!", 'error-message');
        return;
    }

    const mavachElement = document.getElementById('mavach');
    const tenspElement = document.getElementById('tensp');
    const giagocElement = document.getElementById('giagoc');
    const giabanleElement = document.getElementById('giabanle');
    const idDanhmucElement = document.getElementById('id_danhmuc');
    const fileUploadElement = document.getElementById('file-upload');
    const mavachOldElement = document.getElementById('mavach_old');

    if (!mavachElement || !tenspElement || !giagocElement || !giabanleElement || !idDanhmucElement || !fileUploadElement) {
        announcement("Không tìm thấy các trường cần thiết trong form!", 'error-message');
        return;
    }

    const mavach = mavachElement.value;
    const tensp = tenspElement.value;
    const giagoc = giagocElement.value;
    const giabanle = giabanleElement.value;
    const id_danhmuc = idDanhmucElement.value;
    const file = fileUploadElement.files[0];
    const mavach_old = mavachOldElement.value;

    if (!mavach || !tensp || !giagoc || !giabanle || !id_danhmuc || (!file && !isEditModeProduct)) {
        announcement("Vui lòng điền đầy đủ thông tin sản phẩm!", 'error-message');
        return;
    }

    const formData = new FormData();
    formData.append('mavach', mavach);
    formData.append('tensp', tensp);
    formData.append('giagoc', giagoc);
    formData.append('giabanle', giabanle);
    formData.append('id_danhmuc', id_danhmuc);
    if (file) {
        formData.append('file-upload', file);
    }
    if (isEditModeProduct) {
        formData.append('mavach_old', mavach_old);
    }

    const url = isEditModeProduct ? "functionQLDM_SP/editProduct.php" : "functionQLDM_SP/addProduct.php";

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
                form.reset();
                resetFileUpload();
                isEditModeProduct = false;
                document.getElementById('form-title').innerText = "Thêm sản phẩm";
                document.getElementById('submit-btn').innerText = "Lưu";
                chuyengiaodien('add-product', 'list-product');
                reloadProductTable();
            } else {
                announcement(response.message, 'error-message');
            }
        },
        error: function(xhr, status, error) {
            console.log("Lỗi AJAX:", status, error);
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
    if (!cells[2]) {
        announcement("Dữ liệu bảng không đúng định dạng!", 'error-message');
        return;
    }

    const tensp = cells[2].innerText;
    const giagoc = cells[3].innerText.replace(/[^\d]/g, '');
    const giabanle = cells[4].innerText.replace(/[^\d]/g, '');
    const id_danhmuc = row.getAttribute('data-id_danhmuc');
    const ngaytao = cells[6].innerText;
    const img = row.getAttribute('data-img');

    const mavachElement = document.getElementById('mavach');
    const tenspElement = document.getElementById('tensp');
    const giagocElement = document.getElementById('giagoc');
    const giabanleElement = document.getElementById('giabanle');
    const idDanhmucElement = document.getElementById('id_danhmuc');
    const ngaytaoElement = document.getElementById('ngaytao');
    const mavachOldElement = document.getElementById('mavach_old');
    const previewImg = document.getElementById('preview-img');
    const fileInfo = document.getElementById('file-info');

    if (!mavachElement || !tenspElement || !giagocElement || !giabanleElement || !idDanhmucElement || !ngaytaoElement) {
        announcement("Không tìm thấy các trường cần thiết để chỉnh sửa!", 'error-message');
        return;
    }

    mavachElement.value = mavach;
    tenspElement.value = tensp;
    giagocElement.value = giagoc;
    giabanleElement.value = giabanle;
    idDanhmucElement.value = id_danhmuc;
    ngaytaoElement.value = ngaytao.split(' ')[0];
    mavachOldElement.value = mavach;
    previewImg.src = img ? `../images/${img}` : '';
    previewImg.style.display = img ? 'block' : 'none';
    fileInfo.style.display = img ? 'block' : 'none';

    isEditModeProduct = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa sản phẩm";
    document.getElementById('submit-btn').innerText = "Cập nhật";
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
                    if (!document.getElementById('add-product').classList.contains('d-none')) {
                        chuyengiaodien('add-product', 'list-product');
                    }
                } else {
                    announcement(response.message, 'error-message');
                }
            },
            error: function(xhr, status, error) {
                console.log("Lỗi AJAX:", status, error);
                announcement("Lỗi khi gửi yêu cầu đến server: " + error, 'error-message');
            }
        });
    }
}

function attachFormEvent() {
    const form = document.getElementById('add-product');
    if (form) {
        console.log("Tìm thấy form add-product, gắn sự kiện submit!");
        form.removeEventListener('submit', addProduct);
        form.addEventListener('submit', addProduct);
        const inputs = form.querySelectorAll('input, textarea, select, button');
        inputs.forEach(input => input.disabled = false);

        const fileInput = document.getElementById('file-upload');
        if (fileInput) {
            fileInput.removeEventListener('change', previewImage);
            fileInput.addEventListener('change', previewImage);
        }

        const dropZone = document.getElementById('drop-zone');
        if (dropZone) {
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
                fileInput.files = e.dataTransfer.files;
                previewImage({ target: fileInput });
            });
        }
    } else {
        console.log("Không tìm thấy form với id 'add-product', thử lại sau 500ms...");
        setTimeout(attachFormEvent, 500);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    attachFormEvent();

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
    }
});