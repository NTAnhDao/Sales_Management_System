let isEditModeCatalog = false;

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

function addCatalog(event) {
    event.preventDefault();

    let id = document.getElementById('edit-id').value;
    let tendm = document.getElementById('name').value;
    let mota = document.getElementById('description').value;
    let ngaytao = document.getElementById('date').value;

    if (!tendm) {
        announcement("Vui lòng nhập tên danh mục!", 'error-message');
        return;
    }

    let url = isEditModeCatalog ? "functionQLDM_SP/editCategory.php" : "functionQLDM_SP/addCategory.php";
    let formData = new FormData();
    formData.append('id', id);
    formData.append('tendm', tendm);
    formData.append('mota', mota);
    formData.append('ngaytao', ngaytao);

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

                let tbody = document.getElementById('catalog-infor');
                if (!tbody) {
                    console.error("Không tìm thấy tbody với id 'catalog-infor'");
                    return;
                }

                if (isEditModeCatalog) {
                    let row = Array.from(tbody.children).find(tr => tr.getAttribute('data-id') === id);
                    if (row) {
                        row.cells[1].innerText = tendm;
                        row.cells[2].innerText = mota;
                        row.cells[3].innerText = ngaytao || new Date().toISOString().split('T')[0];
                        row.cells[4].innerText = response.id_nguoitao; // Cập nhật id_nguoitao từ phản hồi
                    } else {
                        console.error("Không tìm thấy hàng để cập nhật với id:", id);
                    }
                } else {
                    let tr = document.createElement('tr');
                    tr.setAttribute('data-id', response.id);
                    let td1 = document.createElement('td');
                    let td2 = document.createElement('td');
                    let td3 = document.createElement('td');
                    let td4 = document.createElement('td');
                    let td5 = document.createElement('td');
                    let td6 = document.createElement('td');

                    td1.innerHTML = response.id;
                    td2.innerHTML = tendm;
                    td3.innerHTML = mota;
                    td4.innerHTML = ngaytao || new Date().toISOString().split('T')[0];
                    td5.innerHTML = response.id_nguoitao; // Sử dụng id_nguoitao từ phản hồi
                    td6.innerHTML = `
                        <a href="#" class="btn btn-sm btn-primary mr-1 edit-btn">Chỉnh sửa</a>
                        <a href="#" class="btn btn-sm btn-danger delete-btn">Xóa</a>
                    `;

                    tr.appendChild(td1);
                    tr.appendChild(td2);
                    tr.appendChild(td3);
                    tr.appendChild(td4);
                    tr.appendChild(td5);
                    tr.appendChild(td6);

                    tbody.appendChild(tr);
                }

                document.getElementById('edit-id').value = '';
                document.getElementById('name').value = '';
                document.getElementById('description').value = '';
                document.getElementById('date').value = '';
                isEditModeCatalog = false;
                document.getElementById('form-title').innerText = "Thêm danh mục";
                document.getElementById('save-btn').innerText = "Lưu";

                chuyengiaodien('add-catalog', 'list-catalog');
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

function editCatalog(row) {
    const id = row.getAttribute('data-id');
    if (!id) {
        announcement("Không tìm thấy ID của danh mục!", 'error-message');
        return;
    }

    const cells = row.getElementsByTagName('td');
    const tendm = cells[1].innerText;
    const mota = cells[2].innerText;
    let ngaytao = cells[3].innerText;

    if (ngaytao) {
        ngaytao = new Date(ngaytao).toISOString().split('T')[0];
    }

    document.getElementById('edit-id').value = id;
    document.getElementById('name').value = tendm;
    document.getElementById('description').value = mota;
    document.getElementById('date').value = ngaytao;

    isEditModeCatalog = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa danh mục";
    document.getElementById('save-btn').innerText = "Cập nhật";

    chuyengiaodien('list-catalog', 'add-catalog');
}

function deleteCatalog(row) {
    const id = row.getAttribute('data-id');
    if (!id) {
        announcement("Không tìm thấy ID của danh mục!", 'error-message');
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        $.ajax({
            url: "functionQLDM_SP/deleteCategory.php",
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

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('add-catalog');
    if (form) {
        form.addEventListener('submit', addCatalog);
    } else {
        console.log("Không tìm thấy form với id 'add-catalog'");
    }

    const catalogTable = document.getElementById('catalog-infor');
    if (catalogTable) {
        catalogTable.addEventListener('click', function(e) {
            if (e.target.classList.contains('edit-btn')) {
                e.preventDefault();
                const row = e.target.closest('tr');
                editCatalog(row);
            }
            if (e.target.classList.contains('delete-btn')) {
                e.preventDefault();
                const row = e.target.closest('tr');
                deleteCatalog(row);
            }
        });
    } else {
        console.log("Không tìm thấy tbody với id 'catalog-infor'");
    }
});