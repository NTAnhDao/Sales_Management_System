let isEditModeProduct = false;
let uploadedImage = null;
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
    const saveFileBtn = document.getElementById('save-file-btn');
    const cancelFileBtn = document.getElementById('cancel-file-btn');

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
            handleFile(files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        }
    });

    saveFileBtn.addEventListener('click', () => {
        if (fileInput.files.length > 0) {
            let formData = new FormData();
            formData.append('file-upload', fileInput.files[0]);

            $.ajax({
                url: "functionQLDM_SP/uploadFile.php",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function(response) {
                    if (response.success) {
                        uploadedImage = response.fileName;
                        fileName.textContent = response.fileName;
                        previewImg.src = `image/${response.fileName}`;
                        previewImg.style.display = 'block';
                        announcement("File đã được lưu!", 'success-message');
                    } else {
                        announcement(response.message, 'error-message');
                    }
                },
                error: function(xhr, status, error) {
                    console.log("Lỗi AJAX:", status, error);
                    announcement("Lỗi khi gửi file lên server: " + error, 'error-message');
                }
            });
        } else {
            announcement("Vui lòng chọn một file trước!", 'error-message');
        }
    });

    cancelFileBtn.addEventListener('click', () => {
        fileInput.value = '';
        fileName.textContent = '';
        fileType.textContent = '';
        fileSize.textContent = '';
        previewImg.src = '';
        previewImg.style.display = 'none';
        uploadedImage = null;
        fileToUpload = null;
        announcement("File đã được hủy!", 'success-message');
    });

    function handleFile(file) {
        fileToUpload = file;
        fileName.textContent = file.name;
        fileType.textContent = file.type;
        fileSize.textContent = (file.size / 1024).toFixed(2) + ' KB';

        if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                previewImg.style.display = 'block';
            };
            reader.readAsDataURL(file);
        } else {
            previewImg.src = '';
            previewImg.style.display = 'none';
        }
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

    if (!isEditModeProduct && !uploadedImage) {
        announcement("Vui lòng lưu hình ảnh cho sản phẩm!", 'error-message');
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
    formData.append('img', uploadedImage);

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

                let tbody = document.getElementById('product-infor');
                if (!tbody) {
                    console.error("Không tìm thấy tbody với id 'product-infor'");
                    return;
                }

                if (isEditModeProduct) {
                    let row = Array.from(tbody.children).find(tr => tr.getAttribute('data-mavach') === mavach_old);
                    if (row) {
                        row.setAttribute('data-mavach', response.mavach);
                        row.cells[0].innerText = response.mavach;
                        row.cells[1].innerHTML = response.img ? `<img src="image/${response.img}" width="80" height="80">` : "Không có hình ảnh";
                        row.cells[2].innerText = tensp;
                        row.cells[3].innerText = new Intl.NumberFormat('vi-VN').format(giagoc);
                        row.cells[4].innerText = new Intl.NumberFormat('vi-VN').format(giabanle);
                        row.cells[5].innerText = document.querySelector(`#type option[value="${id_danhmuc}"]`).text;
                        row.cells[6].innerText = ngaytao || new Date().toISOString().split('T')[0];
                    } else {
                        console.error("Không tìm thấy hàng để cập nhật với mã vạch:", mavach_old);
                    }
                } else {
                    let tr = document.createElement('tr');
                    tr.setAttribute('data-mavach', response.mavach);
                    let td1 = document.createElement('td');
                    let td2 = document.createElement('td');
                    let td3 = document.createElement('td');
                    let td4 = document.createElement('td');
                    let td5 = document.createElement('td');
                    let td6 = document.createElement('td');
                    let td7 = document.createElement('td');
                    let td8 = document.createElement('td');

                    td1.innerHTML = response.mavach;
                    td2.innerHTML = response.img ? `<img src="image/${response.img}" width="80" height="80">` : "Không có hình ảnh";
                    td3.innerHTML = tensp;
                    td4.innerHTML = new Intl.NumberFormat('vi-VN').format(giagoc);
                    td5.innerHTML = new Intl.NumberFormat('vi-VN').format(giabanle);
                    td6.innerHTML = document.querySelector(`#type option[value="${id_danhmuc}"]`).text;
                    td7.innerHTML = ngaytao || new Date().toISOString().split('T')[0];
                    td8.innerHTML = `
                        <a href="#" class="btn btn-sm btn-primary mr-1 edit-btn">Chỉnh sửa</a>
                        <a href="#" class="btn btn-sm btn-danger delete-btn">Xóa</a>
                    `;

                    tr.appendChild(td1);
                    tr.appendChild(td2);
                    tr.appendChild(td3);
                    tr.appendChild(td4);
                    tr.appendChild(td5);
                    tr.appendChild(td6);
                    tr.appendChild(td7);
                    tr.appendChild(td8);

                    tbody.appendChild(tr);
                }

                document.getElementById('edit-mavach-old').value = '';
                document.getElementById('mavach').value = '';
                document.getElementById('name').value = '';
                document.getElementById('import-price').value = '';
                document.getElementById('retail-price').value = '';
                document.getElementById('date').value = '';
                document.getElementById('type').selectedIndex = 0;
                document.getElementById('file-upload').value = '';
                document.getElementById('file-name').textContent = '';
                document.getElementById('file-type').textContent = '';
                document.getElementById('file-size').textContent = '';
                document.getElementById('preview-img').src = '';
                document.getElementById('preview-img').style.display = 'none';
                uploadedImage = null;
                fileToUpload = null;
                isEditModeProduct = false;
                document.getElementById('form-title').innerText = "Thêm sản phẩm";
                document.getElementById('save-btn').innerText = "Lưu";

                chuyengiaodien('add-product', 'list-product');
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
    const tensp = cells[2].innerText;
    const giagoc = parseFloat(cells[3].innerText.replace(/[^0-9.-]+/g,""));
    const giabanle = parseFloat(cells[4].innerText.replace(/[^0-9.-]+/g,""));
    const id_danhmuc = Array.from(document.querySelectorAll('#type option'))
        .find(option => option.text === cells[5].innerText)?.value;
    let ngaytao = cells[6].innerText;
    const img = cells[1].querySelector('img')?.src.split('/').pop();

    if (ngaytao) {
        ngaytao = new Date(ngaytao).toISOString().split('T')[0];
    }

    document.getElementById('edit-mavach-old').value = mavach;
    document.getElementById('mavach').value = mavach;
    document.getElementById('name').value = tensp;
    document.getElementById('import-price').value = giagoc;
    document.getElementById('retail-price').value = giabanle;
    document.getElementById('date').value = ngaytao;
    document.getElementById('type').value = id_danhmuc;
    if (img) {
        uploadedImage = img;
        document.getElementById('file-name').textContent = img;
        document.getElementById('preview-img').src = `image/${img}`;
        document.getElementById('preview-img').style.display = 'block';
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