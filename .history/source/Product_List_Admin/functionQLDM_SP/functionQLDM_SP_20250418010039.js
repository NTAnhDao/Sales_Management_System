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

// Hàm chuyển giao diện
function chuyengiaodien(fromSection, toSection) {
    document.querySelector(`.${fromSection}`).classList.add('d-none');
    document.querySelector(`.${toSection}`).classList.remove('d-none');
}

function addCatalog(event) {
    event.preventDefault();

    const id = document.getElementById('edit-id').value;
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const date = document.getElementById('date').value;

    if (!name) {
        announcement("Tên danh mục không được để trống!", 'error-message');
        return;
    }

    const url = isEditModeCatalog ? "functionQLDM_SP/editCategory.php" : "functionQLDM_SP/addCategory.php";
    const data = { id, name, description, date };

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        success: function(response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, 'success-message');

                const tbody = document.getElementById('catalog-infor');
                if (!tbody) {
                    console.error("Không tìm thấy tbody với id 'catalog-infor'");
                    return;
                }

                if (isEditModeCatalog) {
                    let row = Array.from(tbody.children).find(tr => tr.getAttribute('data-id') === id);
                    if (row) {
                        row.cells[1].innerText = response.name;
                        row.cells[2].innerText = response.description;
                        row.cells[3].innerText = response.date;
                        row.cells[4].innerText = response.id_nguoitao || 'Không xác định';
                    } else {
                        console.error("Không tìm thấy hàng để cập nhật với id:", id);
                    }
                } else {
                    const rowCount = tbody.children.length + 1;
                    let tr = document.createElement('tr');
                    tr.setAttribute('data-id', response.id);
                    let td1 = document.createElement('td');
                    let td2 = document.createElement('td');
                    let td3 = document.createElement('td');
                    let td4 = document.createElement('td');
                    let td5 = document.createElement('td');
                    let td6 = document.createElement('td');

                    td1.innerHTML = rowCount;
                    td2.innerHTML = response.name;
                    td3.innerHTML = response.description;
                    td4.innerHTML = response.date;
                    td5.innerHTML = response.id_nguoitao || 'Không xác định';
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
    const name = cells[1].innerText;
    const description = cells[2].innerText;
    const date = cells[3].innerText;

    document.getElementById('edit-id').value = id;
    document.getElementById('name').value = name;
    document.getElementById('description').value = description;
    document.getElementById('date').value = date;

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
                    // Cập nhật lại STT
                    const tbody = document.getElementById('catalog-infor');
                    Array.from(tbody.children).forEach((tr, index) => {
                        tr.cells[0].innerText = index + 1;
                    });
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
        console.log("Tìm thấy form add-catalog, gắn sự kiện submit!");
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