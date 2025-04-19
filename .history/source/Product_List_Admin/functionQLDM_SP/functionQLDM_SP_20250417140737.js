// Biến để kiểm tra chế độ (thêm hoặc chỉnh sửa)
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

let isEditMode = false;



// Hàm xóa từng danh mục
function deleteCategory(row) {
    const id = row.getAttribute('data-id') || row.querySelector('.delete-btn').getAttribute('data-id');


    if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        $.ajax({
            url: "functionQLDM_SP/deleteCatalog.php",
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
                announcement("Lỗi khi gửi yêu cầu đến server: " + error, 'error-message');
            }
        });
    }
}

// Hàm điền dữ liệu vào form để chỉnh sửa
function editCategory(row) {
    const id = row.getAttribute('data-id');
    const cells = row.getElementsByTagName('td');
    const tendm = cells[1].innerText;
    const mota = cells[2].innerText;
    const ngaytao = cells[3].innerText;
    const id_nguoitao = cells[4].innerText;

    document.getElementById('edit-id').value = id;
    document.getElementById('name').value = tendm;
    document.getElementById('description').value = mota;
    document.getElementById('date').value = ngaytao;
    document.getElementById('people').value = id_nguoitao;

    isEditMode = true;
    document.getElementById('form-title').innerText = "Chỉnh sửa danh mục";
    document.getElementById('save-btn').innerText = "Cập nhật";

    chuyengiaodien('list-catalog', 'add-catalog');
}

// Hàm thêm hoặc cập nhật danh mục
function addCategory(event) {
    event.preventDefault();

    let id = document.getElementById('edit-id').value;
    let tendm = document.getElementById('name').value;
    let mota = document.getElementById('description').value;
    let id_nguoitao = document.getElementById('people').value;
    let ngaytao = document.getElementById('date').value;

    if (tendm === '' || id_nguoitao === '') {
        announcement("Vui lòng nhập đầy đủ thông tin!", 'error-message');
        return;
    }

    let url = isEditMode ? "functionQLDM_SP/editCatalog.php" : "functionQLDM_SP/addCatalog.php";
    let data = {
        tendm: tendm,
        mota: mota,
        ngaytao: ngaytao,
        id_nguoitao: id_nguoitao

    };
    if (isEditMode) {
        data.id = id;
    }

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        success: function(response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, 'success-message');

                let tbody = document.getElementById('catalog-infor');
                if (isEditMode) {
                    let row = Array.from(tbody.children).find(tr => tr.getAttribute('data-id') === id);
                    if (row) {
                        row.cells[0].innerText = id;
                        row.cells[1].innerText = tendm;
                        row.cells[2].innerText = mota || '';
                        row.cells[3].innerText = ngaytao || new Date().toISOString().split('T')[0];
                        row.cells[4].innerText = id_nguoitao;
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
                    td3.innerHTML = mota || '';
                    td4.innerHTML = ngaytao || new Date().toISOString().split('T')[0];
                    td5.innerHTML = id_nguoitao;
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
                document.getElementById('people').value = '';
                document.getElementById('date').value = '';
                isEditMode = false;
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

// Gắn sự kiện
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('add-catalog');
    if (form) {
        form.addEventListener('submit', addCategory);
    } else {
        console.log("Không tìm thấy form với id 'add-catalog'");
    }

    document.getElementById('catalog-infor').addEventListener('click', function(e) {
        if (e.target.classList.contains('edit-btn')) {
            e.preventDefault();
            const row = e.target.closest('tr');
            editCategory(row);
        }
        if (e.target.classList.contains('delete-btn')) {
            e.preventDefault();
            const row = e.target.closest('tr');
            deleteCategory(row);
        }
    });

});
