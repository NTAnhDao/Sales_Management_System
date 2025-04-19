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

// Hàm chuyển giao diện
function chuyengiaodien(fromSection, toSection) {
    const fromElement = document.querySelector(`.${fromSection}`);
    const toElement = document.querySelector(`.${toSection}`);
    if (fromElement && toElement) {
        fromElement.classList.add('d-none');
        toElement.classList.remove('d-none');

        const form = document.getElementById('add-product');
        if (form) {
            const inputs = form.querySelectorAll('input, textarea, select, button');
            if (toSection === 'add-product') {
                inputs.forEach(input => input.disabled = false);
                setTimeout(() => {
                    attachFormEvent();
                }, 100);
            } else {
                inputs.forEach(input => input.disabled = true);
            }
        }
    } else {
        console.error(`Không tìm thấy section: ${fromSection} hoặc ${toSection}`);
    }
}

function addProduct(event) {
    event.preventDefault();

    const form = document.getElementById('add-product');
    if (!form) {
        announcement("Không tìm thấy form thêm sản phẩm!", 'error-message');
        console.error("Form 'add-product' không tồn tại trong DOM");
        return;
    }

    if (form.classList.contains('d-none')) {
        announcement("Form thêm sản phẩm không được hiển thị!", 'error-message');
        console.error("Form 'add-product' đang bị ẩn (có lớp d-none)");
        return;
    }

    const mavachElement = document.getElementById('mavach');
    const tenspElement = document.getElementById('tensp');
    const giagocElement = document.getElementById('giagoc');
    const giabanleElement = document.getElementById('giabanle');
    const idDanhmucElement = document.getElementById('id_danhmuc');
    const imgElement = document.getElementById('img');

    if (!mavachElement || !tenspElement || !giagocElement || !giabanleElement || !idDanhmucElement || !imgElement) {
        announcement("Không tìm thấy các trường cần thiết trong form!", 'error-message');
        console.error("Một hoặc nhiều phần tử không tồn tại trong form add-product");
        return;
    }

    if (!mavachElement.offsetParent || !tenspElement.offsetParent || 
        !giagocElement.offsetParent || !giabanleElement.offsetParent || 
        !idDanhmucElement.offsetParent || !imgElement.offsetParent ||
        mavachElement.disabled || tenspElement.disabled || 
        giagocElement.disabled || giabanleElement.disabled || 
        idDanhmucElement.disabled || imgElement.disabled) {
        announcement("Các trường không hiển thị hoặc bị vô hiệu hóa!", 'error-message');
        console.error("Một hoặc nhiều phần tử không hiển thị hoặc bị vô hiệu hóa");
        return;
    }

    const mavach = mavachElement.value;
    const tensp = tenspElement.value;
    const giagoc = giagocElement.value;
    const giabanle = giabanleElement.value;
    const id_danhmuc = idDanhmucElement.value;
    const imgFile = imgElement.files[0];

    if (!mavach || !tensp || !giagoc || !giabanle || !id_danhmuc) {
        announcement("Vui lòng điền đầy đủ thông tin sản phẩm!", 'error-message');
        return;
    }

    const formData = new FormData();
    formData.append('mavach', mavach);
    formData.append('tensp', tensp);
    formData.append('giagoc', giagoc);
    formData.append('giabanle', giabanle);
    formData.append('id_danhmuc', id_danhmuc);
    if (imgFile) {
        formData.append('img', imgFile);
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

                // Reset form
                mavachElement.value = '';
                tenspElement.value = '';
                giagocElement.value = '';
                giabanleElement.value = '';
                idDanhmucElement.value = '';
                imgElement.value = '';
                isEditModeProduct = false;
                document.getElementById('form-title').innerText = "Thêm sản phẩm";
                document.getElementById('save-btn').innerText = "Lưu";

                // Chuyển giao diện về danh sách sản phẩm
                chuyengiaodien('add-product', 'list-product');

                // Tự động reload trang để cập nhật hình ảnh
                setTimeout(() => {
                    window.location.reload();
                }, 1000); // Delay 1 giây để thông báo hiển thị trước khi reload
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
    const tensp = cells[1].innerText;
    const id_danhmuc = row.getAttribute('data-id-danhmuc');
    const giagoc = cells[4].innerText;
    const giabanle = cells[5].innerText;

    const mavachElement = document.getElementById('mavach');
    const tenspElement = document.getElementById('tensp');
    const giagocElement = document.getElementById('giagoc');
    const giabanleElement = document.getElementById('giabanle');
    const idDanhmucElement = document.getElementById('id_danhmuc');

    if (!mavachElement || !tenspElement || !giagocElement || !giabanleElement || !idDanhmucElement) {
        announcement("Không tìm thấy các trường cần thiết để chỉnh sửa!", 'error-message');
        console.error("Không tìm thấy các trường để chỉnh sửa trong form add-product");
        return;
    }

    if (!mavachElement.offsetParent || !tenspElement.offsetParent || 
        !giagocElement.offsetParent || !giabanleElement.offsetParent || 
        !idDanhmucElement.offsetParent ||
        mavachElement.disabled || tenspElement.disabled || 
        giagocElement.disabled || giabanleElement.disabled || 
        idDanhmucElement.disabled) {
        announcement("Các trường không hiển thị hoặc bị vô hiệu hóa!", 'error-message');
        console.error("Một hoặc nhiều phần tử không hiển thị hoặc bị vô hiệu hóa");
        return;
    }

    mavachElement.value = mavach;
    tenspElement.value = tensp;
    giagocElement.value = giagoc;
    giabanleElement.value = giabanle;
    idDanhmucElement.value = id_danhmuc;

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
                    const tbody = document.getElementById('product-infor');
                    if (tbody) {
                        Array.from(tbody.children).forEach((tr, index) => {
                            tr.cells[0].innerText = index + 1;
                        });
                    }
                    announcement(response.message, 'success-message');

                    const form = document.getElementById('add-product');
                    if (form && !form.classList.contains('d-none')) {
                        chuyengiaodien('add-product', 'list-product');
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

function attachFormEvent() {
    const form = document.getElementById('add-product');
    if (form) {
        console.log("Tìm thấy form add-product, gắn sự kiện submit!");
        form.removeEventListener('submit', addProduct);
        form.addEventListener('submit', addProduct);
        const inputs = form.querySelectorAll('input, textarea, select, button');
        inputs.forEach(input => input.disabled = true);
    } else {
        console.log("Không tìm thấy form với id 'add-product', thử lại sau 500ms...");
        setTimeout(attachFormEvent, 500);
    }
}

document.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        const form = document.getElementById('add-product');
        if (form && form.classList.contains('d-none')) {
            event.preventDefault();
            console.log("Ngăn sự kiện Enter vì form add-product đang ẩn");
        }
    }
});

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
    } else {
        console.log("Không tìm thấy tbody với id 'product-infor'");
    }
});