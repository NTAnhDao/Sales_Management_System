// Add server-provided permission detection

let userCategoryPermissions = {
    canManageCategories: false,
    isAdmin: false,
};

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
    }, 22000);
}

document.addEventListener("DOMContentLoaded", function () {
    // Get permissions from server-side data attributes
    const permissionsEl = document.getElementById("user-permissions");
    if (permissionsEl) {
        userCategoryPermissions.canManageCategories =
            permissionsEl.dataset.canManageCategories === "1";
        userCategoryPermissions.isAdmin = permissionsEl.dataset.isAdmin === "1";
    }
});

// Biến để kiểm tra chế độ (thêm hoặc chỉnh sửa)
function announcement(message, type) {
    const successDiv = document.getElementById("success-message");
    const errorDiv = document.getElementById("error-message");

    successDiv.classList.add("d-none");
    errorDiv.classList.add("d-none");

    if (type === "success-message") {
        successDiv.innerText = message;
        successDiv.classList.remove("d-none");
    } else if (type === "error-message") {
        errorDiv.innerText = message;
        errorDiv.classList.remove("d-none");
    }
    setTimeout(() => {
        successDiv.classList.add("d-none");
        errorDiv.classList.add("d-none");
    }, 3000);
}

let isEditMode = false;

// Hàm xóa từng danh mục
function deleteCategory(row) {
    // Check permission before proceeding
    if (!userCategoryPermissions.canManageCategories) {
        announcement("Bạn không có quyền xóa danh mục!", "error-message");
        return;
    }

    const id =
        row.getAttribute("data-id") ||
        row.querySelector(".delete-btn").getAttribute("data-id");

    if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        $.ajax({
            url: "../models/danhmuc_sanpham/deleteCatalog.php",
            type: "POST",
            data: { id: id },
            dataType: "json",
            success: function (response) {
                console.log("Phản hồi từ server:", response);
                if (response.success) {
                    row.remove();
                    announcement(response.message, "success-message");
                } else {
                    announcement(response.message, "error-message");
                }
            },
            error: function (xhr, status, error) {
                console.log("Lỗi AJAX:", status, error);
                announcement(
                    "Lỗi khi gửi yêu cầu đến server: " + error,
                    "error-message"
                );
            },
        });
    }
}

// Hàm điền dữ liệu vào form để chỉnh sửa
function editCategory(row) {
    // Check permission before proceeding
    if (!userCategoryPermissions.canManageCategories) {
        announcement("Bạn không có quyền chỉnh sửa danh mục!", "error-message");
        return;
    }

    const id = row.getAttribute("data-id");
    const cells = row.getElementsByTagName("td");
    const tendm = cells[1].innerText;
    const mota = cells[2].innerText;
    const ngaytao = cells[3].innerText;

    document.getElementById("edit-id").value = id;
    document.getElementById("name").value = tendm;
    document.getElementById("description").value = mota;
    document.getElementById("date").value = ngaytao;

    isEditMode = true;
    document.getElementById("form-title").innerText = "Chỉnh sửa danh mục";
    document.getElementById("save-btn").innerText = "Cập nhật";

    chuyengiaodien("list-catalog", "add-catalog");
}

// Hàm thêm hoặc cập nhật danh mục
function addCategory(event) {
    event.preventDefault();

    // Check permission before proceeding
    if (!userCategoryPermissions.canManageCategories) {
        announcement(
            "Bạn không có quyền thêm hoặc chỉnh sửa danh mục!",
            "error-message"
        );
        return;
    }

    let id = document.getElementById("edit-id").value;
    let tendm = document.getElementById("name").value.trim();
    let mota = document.getElementById("description").value.trim();
    let ngaytao = document.getElementById("date").value.trim();

    if (tendm === "") {
        announcement("Vui lòng nhập đầy đủ thông tin!", "error-message");
        return;
    }

    let url = isEditMode
    ? "../models/danhmuc_sanpham/editCatalog.php"
    : "../models/danhmuc_sanpham/addCatalog.php";
    let data = {
        tendm: tendm,
        mota: mota,
        ngaytao: ngaytao,
    };
    if (isEditMode) {
        data.id = id;
    }

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        success: function (response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, "success-message");
                chuyengiaodien("add-catalog", "list-catalog");
                location.reload(); // Reload để cập nhật danh sách
            } else {
                announcement(response.message, "error-message");
            }
        },
        error: function (xhr, status, error) {
            console.log("Lỗi AJAX:", status, error);
            announcement(
                "Lỗi khi gửi yêu cầu đến server: " + error,
                "error-message"
            );
        },
    });
}

// Gắn sự kiện
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("add-catalog");
    if (form) {
        form.addEventListener("submit", addCategory);
    } else {
        console.log("Không tìm thấy form với id 'add-catalog'");
    }

    document
        .getElementById("catalog-infor")
        .addEventListener("click", function (e) {
            if (e.target.classList.contains("edit-btn")) {
                e.preventDefault();
                const row = e.target.closest("tr");
                editCategory(row);
            }
            if (e.target.classList.contains("delete-btn")) {
                e.preventDefault();
                const row = e.target.closest("tr");
                deleteCategory(row);
            }
        });

    document
        .querySelector(".btn-danger")
        .addEventListener("click", function (e) {
            e.preventDefault();
            deleteAllCategories();
        });
});
