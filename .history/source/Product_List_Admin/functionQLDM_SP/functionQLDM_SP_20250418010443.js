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
    const fromElement = document.querySelector(`.${fromSection}`);
    const toElement = document.querySelector(`.${toSection}`);
    if (fromElement && toElement) {
        fromElement.classList.add('d-none');
        toElement.classList.remove('d-none');

        // Quản lý trạng thái của form
        const form = document.getElementById('add-catalog');
        if (form) {
            const inputs = form.querySelectorAll('input, textarea, button');
            if (toSection === 'add-catalog') {
                // Bật form khi hiển thị
                inputs.forEach(input => input.disabled = false);
                setTimeout(() => {
                    attachFormEvent();
                }, 100); // Thêm độ trễ 100ms để đảm bảo DOM cập nhật
            } else {
                // Tắt form khi ẩn
                inputs.forEach(input => input.disabled = true);
            }
        }
    } else {
        console.error(`Không tìm thấy section: ${fromSection} hoặc ${toSection}`);
    }
}

function addCatalog(event) {
    event.preventDefault();

    // Kiểm tra xem form có tồn tại và đang hiển thị không
    const form = document.getElementById('add-catalog');
    if (!form) {
        announcement("Không tìm thấy form thêm danh mục!", 'error-message');
        console.error("Form 'add-catalog' không tồn tại trong DOM");
        return;
    }

    if (form.classList.contains('d-none')) {
        announcement("Form thêm danh mục không được hiển thị!", 'error-message');
        console.error("Form 'add-catalog' đang bị ẩn (có lớp d-none)");
        return;
    }

    // Kiểm tra từng phần tử trước khi lấy giá trị
    const editIdElement = document.getElementById('edit-id');
    const nameElement = document.getElementById('name');
    const descriptionElement = document.getElementById('description');
    const dateElement = document.getElementById('date');

    // Debug: In ra console để kiểm tra
    console.log("edit-id element:", editIdElement);
    console.log("name element:", nameElement);
    console.log("description element:", descriptionElement);
    console.log("date element:", dateElement);

    if (!editIdElement || !nameElement || !descriptionElement || !dateElement) {
        announcement("Không tìm thấy các trường cần thiết trong form!", 'error-message');
        console.error("Một hoặc nhiều phần tử không tồn tại trong form add-catalog");
        return;
    }

    // Đảm bảo các phần tử là các input/textarea hợp lệ
    if (!(editIdElement instanceof HTMLInputElement) ||
        !(nameElement instanceof HTMLInputElement) ||
        !(descriptionElement instanceof HTMLTextAreaElement) ||
        !(dateElement instanceof HTMLInputElement)) {
        announcement("Các trường trong form không hợp lệ!", 'error-message');
        console.error("Một hoặc nhiều phần tử không phải là input/textarea hợp lệ");
        return;
    }

    // Kiểm tra xem phần tử có hiển thị và không bị vô hiệu hóa không
    if (!editIdElement.offsetParent || !nameElement.offsetParent || 
        !descriptionElement.offsetParent || !dateElement.offsetParent ||
        editIdElement.disabled || nameElement.disabled || 
        descriptionElement.disabled || dateElement.disabled) {
        announcement("Các trường không hiển thị hoặc bị vô hiệu hóa!", 'error-message');
        console.error("Một hoặc nhiều phần tử không hiển thị hoặc bị vô hiệu hóa");
        return;
    }

    const id = editIdElement.value;
    const name = nameElement.value;
    const description = descriptionElement.value;
    const date = dateElement.value;

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

                // Lấy tên người tạo từ session hoặc từ server nếu có
                const tenNguoiTao = response.ten_nguoitao || 'Không xác định';

                if (isEditModeCatalog) {
                    let row = Array.from(tbody.children).find(tr => tr.getAttribute('data-id') === id);
                    if (row) {
                        row.cells[1].innerText = response.name;
                        row.cells[2].innerText = response.description;
                        row.cells[3].innerText = response.date;
                        row.cells[4].innerText = tenNguoiTao;
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
                    td5.innerHTML = tenNguoiTao;
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

                editIdElement.value = '';
                nameElement.value = '';
                descriptionElement.value = '';
                dateElement.value = '';
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

    // Kiểm tra các phần tử trước khi gán giá trị
    const editIdElement = document.getElementById('edit-id');
    const nameElement = document.getElementById('name');
    const descriptionElement = document.getElementById('description');
    const dateElement = document.getElementById('date');

    if (!editIdElement || !nameElement || !descriptionElement || !dateElement) {
        announcement("Không tìm thấy các trường cần thiết để chỉnh sửa!", 'error-message');
        console.error("Không tìm thấy các trường để chỉnh sửa trong form add-catalog");
        return;
    }

    // Kiểm tra xem phần tử có hiển thị và không bị vô hiệu hóa không
    if (!editIdElement.offsetParent || !nameElement.offsetParent || 
        !descriptionElement.offsetParent || !dateElement.offsetParent ||
        editIdElement.disabled || nameElement.disabled || 
        descriptionElement.disabled || dateElement.disabled) {
        announcement("Các trường không hiển thị hoặc bị vô hiệu hóa!", 'error-message');
        console.error("Một hoặc nhiều phần tử không hiển thị hoặc bị vô hiệu hóa");
        return;
    }

    editIdElement.value = id;
    nameElement.value = name;
    descriptionElement.value = description;
    dateElement.value = date;

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
                    if (tbody) {
                        Array.from(tbody.children).forEach((tr, index) => {
                            tr.cells[0].innerText = index + 1;
                        });
                    }
                    announcement(response.message, 'success-message');

                    // Đảm bảo form add-catalog không được kích hoạt sau khi xóa
                    const form = document.getElementById('add-catalog');
                    if (form && !form.classList.contains('d-none')) {
                        chuyengiaodien('add-catalog', 'list-catalog');
                    }
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

// Gắn sự kiện submit cho form
function attachFormEvent() {
    const form = document.getElementById('add-catalog');
    if (form) {
        console.log("Tìm thấy form add-catalog, gắn sự kiện submit!");
        form.removeEventListener('submit', addCatalog); // Xóa sự kiện cũ nếu có
        form.addEventListener('submit', addCatalog);
        // Tắt form ban đầu vì section có lớp d-none
        const inputs = form.querySelectorAll('input, textarea, button');
        inputs.forEach(input => input.disabled = true);
    } else {
        console.log("Không tìm thấy form với id 'add-catalog', thử lại sau 500ms...");
        setTimeout(attachFormEvent, 500); // Thử lại sau 500ms nếu không tìm thấy
    }
}

// Ngăn Enter kích hoạt submit khi form không hiển thị
document.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        const form = document.getElementById('add-catalog');
        if (form && form.classList.contains('d-none')) {
            event.preventDefault();
            console.log("Ngăn sự kiện Enter vì form add-catalog đang ẩn");
        }
    }
});

// Đảm bảo DOM sẵn sàng trước khi gắn sự kiện
document.addEventListener('DOMContentLoaded', function() {
    attachFormEvent();

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