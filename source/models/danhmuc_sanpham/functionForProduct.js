let isEditModeProduct = false;
let fileToUpload = null;

// Add server-provided permission detection
let userPermissions = {
    canManageProducts: false,
    canManageCategories: false,
    canViewImportPrice: false,
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
    }, 2000);
}



document.addEventListener("DOMContentLoaded", function () {
    // Get permissions from server-side data attributes
    const permissionsEl = document.getElementById("user-permissions");
    if (permissionsEl) {
        userPermissions.canManageProducts =
            permissionsEl.dataset.canManageProducts === "1";
        userPermissions.canManageCategories =
            permissionsEl.dataset.canManageCategories === "1";
        userPermissions.canViewImportPrice =
            permissionsEl.dataset.canViewImportPrice === "1";
        userPermissions.isAdmin = permissionsEl.dataset.isAdmin === "1";
    }

    // Original functionality

    const dropZone = document.getElementById("drop-zone");
    const fileInput = document.getElementById("file-upload");
    const fileName = document.getElementById("file-name");
    const fileType = document.getElementById("file-type");
    const fileSize = document.getElementById("file-size");
    const previewImg = document.getElementById("preview-img");
    const cancelFileBtn = document.getElementById("cancel-file-btn");

    // Thêm style trực tiếp cho input file
    if (fileInput) {
        fileInput.style.width = "0.1px";
        fileInput.style.height = "0.1px";
        fileInput.style.opacity = "0";
        fileInput.style.overflow = "hidden";
        fileInput.style.position = "absolute";
        fileInput.style.zIndex = "-1";
        console.log("Đã thiết lập style cho input file");
    }

    // Kiểm tra và log DOM elements để debug
    console.log("dropZone:", dropZone);
    console.log("fileInput:", fileInput);
    console.log("previewImg:", previewImg);

    if (
        !dropZone ||
        !fileInput ||
        !fileName ||
        !fileType ||
        !fileSize ||
        !previewImg ||
        !cancelFileBtn
    ) {
        console.error("Không tìm thấy một hoặc nhiều phần tử HTML cần thiết!");
        return;
    }

    dropZone.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropZone.classList.add("dragover");
    });

    dropZone.addEventListener("dragleave", () => {
        dropZone.classList.remove("dragover");
    });

    dropZone.addEventListener("drop", (e) => {
        e.preventDefault();
        dropZone.classList.remove("dragover");
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files;
            handleFile(files[0]);
        }
    });

    fileInput.addEventListener("click", (e) => {
        console.log("File input clicked");
    });

    fileInput.addEventListener("change", (e) => {
        console.log("File input changed", e.target.files);
        if (fileInput.files && fileInput.files.length > 0) {
            console.log("Selected file:", fileInput.files[0].name);
            handleFile(fileInput.files[0]);
        } else {
            console.log("No files selected");
            resetFileInfo();
        }
    });

    // Thêm sự kiện click trực tiếp cho label
    document
        .querySelector('label[for="file-upload"]')
        .addEventListener("click", function (e) {
            console.log("Label clicked");
            // Đảm bảo sự kiện click được chuyển đến input file
            setTimeout(function () {
                if (fileInput && typeof fileInput.click === "function") {
                    console.log("Triggering file input click programmatically");
                }
            }, 100);
        });

    cancelFileBtn.addEventListener("click", () => {
        fileInput.value = "";
        resetFileInfo();
        fileToUpload = null;
        announcement("File đã được hủy!", "success-message");
    });

    function handleFile(file) {
        console.log("Handling file:", file.name, file.type, file.size);
        fileToUpload = file;
        fileName.textContent = file.name || "Không có tên file";
        fileType.textContent = file.type || "Không xác định";
        fileSize.textContent = (file.size / 1024).toFixed(2) + " KB" || "0 KB";

        if (file.type.startsWith("image/")) {
            console.log("File is an image");
            const reader = new FileReader();
            reader.onload = function (e) {
                console.log("File loaded successfully");
                previewImg.src = e.target.result;
                previewImg.style.display = "block";
            };
            reader.onerror = function (e) {
                console.error("Lỗi khi đọc file hình ảnh!", e);
                previewImg.src = "";
                previewImg.style.display = "none";
            };
            reader.readAsDataURL(file);
        } else {
            console.warn("File is not an image:", file.type);
            previewImg.src = "";
            previewImg.style.display = "none";
            announcement("Vui lòng chọn file hình ảnh!", "error-message");
        }
    }

    function resetFileInfo() {
        fileName.textContent = "Không có hình ảnh";
        fileType.textContent = "";
        fileSize.textContent = "";
        previewImg.src = "";
        previewImg.style.display = "none";
    }
});

function addProduct(event) {
    event.preventDefault();

    // Check permission before proceeding
    if (!userPermissions.canManageProducts) {
        announcement(
            "Bạn không có quyền thêm hoặc chỉnh sửa sản phẩm!",
            "error-message"
        );
        return;
    }

    let mavach_old = document.getElementById("edit-mavach-old").value;
    let mavach = document.getElementById("mavach").value;
    let tensp = document.getElementById("name").value;
    let giagoc = parseFloat(document.getElementById("import-price").value);
    let giabanle = parseFloat(document.getElementById("retail-price").value);
    let id_danhmuc = document.getElementById("type").value;
    let ngaytao = document.getElementById("date").value;
    let fileInput = document.getElementById("file-upload");

    if (!mavach || !tensp || giagoc <= 0 || giabanle <= 0 || !id_danhmuc) {
        announcement(
            "Vui lòng nhập đầy đủ thông tin sản phẩm, bao gồm mã vạch!",
            "error-message"
        );
        return;
    }

    if (!isEditModeProduct && fileInput.files.length === 0) {
        announcement("Vui lòng chọn hình ảnh cho sản phẩm!", "error-message");
        return;
    }

    let url = isEditModeProduct
        ? "../models/danhmuc_sanpham/editProduct.php"
        : "../models/danhmuc_sanpham/addProduct.php";
    let formData = new FormData();
    formData.append("mavach", mavach);
    formData.append("mavach_old", mavach_old);
    formData.append("tensp", tensp);
    formData.append("giagoc", giagoc);
    formData.append("giabanle", giabanle);
    formData.append("id_danhmuc", id_danhmuc);
    if (fileInput.files.length > 0) {
        formData.append("file-upload", fileInput.files[0]);
    }

    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function (response) {
            console.log("Phản hồi từ server:", response);
            if (response.success) {
                announcement(response.message, "success-message");
                window.location.reload();
            } else {
                announcement(response.message, "error-message");
            }
        },
        error: function (xhr, status, error) {
            console.log("Lỗi AJAX:", status, error);
            console.log("Phản hồi từ server:", xhr.responseText);
            announcement(
                "Lỗi khi gửi yêu cầu đến server: " + error,
                "error-message"
            );
        },
    });
}

function editProduct(row) {
    // Check permission before proceeding
    if (!userPermissions.canManageProducts) {
        announcement(
            "Bạn không có quyền thêm hoặc chỉnh sửa sản phẩm!",
            "error-message"
        );
        return;
    }

    const mavach = row.getAttribute("data-mavach");
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", "error-message");
        return;
    }

    const cells = row.getElementsByTagName("td");
    if (cells.length < 8) {
        announcement("Dữ liệu bảng không đúng định dạng!", "error-message");
        return;
    }

    const tensp = cells[2].innerText;
    const giagoc = parseFloat(cells[3].innerText.replace(/[^0-9.-]+/g, ""));
    const giabanle = parseFloat(cells[4].innerText.replace(/[^0-9.-]+/g, ""));
    const id_danhmuc =
        row.getAttribute("data-id_danhmuc") ||
        cells[5].innerText.match(/\(ID: (\d+)\)/)?.[1] ||
        "";

    // Lấy tên file hình ảnh từ thuộc tính data-img hoặc từ thẻ img
    let img = row.getAttribute("data-img");
    if (!img && cells[1] && cells[1].querySelector("img")) {
        const imgSrc = cells[1].querySelector("img").getAttribute("src");
        if (imgSrc) {
            img = imgSrc.replace(/^.*[\\\/]/, "");
            console.log("Tên file hình ảnh đã trích xuất:", img);
        }
    }

    console.log("Giá trị img cuối cùng:", img);

    document.getElementById("edit-mavach-old").value = mavach;
    document.getElementById("mavach").value = mavach;
    document.getElementById("name").value = tensp;
    document.getElementById("import-price").value = giagoc;
    document.getElementById("retail-price").value = giabanle;

    // Đợi một chút để đảm bảo DOM đã được tải khi chuyển giao diện
    setTimeout(() => {
        // Đặt danh mục cũ trong <select id="type">
        const typeSelect = document.getElementById("type");
        if (typeSelect) {
            if (id_danhmuc) {
                typeSelect.value = id_danhmuc;
                if (typeSelect.value != id_danhmuc) {
                    console.warn(
                        `Không tìm thấy danh mục với id_danhmuc: ${id_danhmuc}`
                    );
                    announcement(
                        "Danh mục cũ không tồn tại trong danh sách!",
                        "error-message"
                    );
                    typeSelect.selectedIndex = 0;
                }
            } else {
                typeSelect.selectedIndex = 0;
            }
        }

        // Xử lý hình ảnh
        if (img) {
            const imagePath = `../models/danhmuc_sanpham/images/${img}`; 
            console.log(`Đường dẫn hình ảnh: ${imagePath}`);
            document.getElementById("file-name").textContent = img;
            document.getElementById("file-type").textContent = "image/*";
            document.getElementById("preview-img").src = imagePath;
            document.getElementById("preview-img").style.display = "block";
        } else {
            document.getElementById("file-name").textContent = "Không có hình ảnh";
            document.getElementById("preview-img").src = "";
            document.getElementById("preview-img").style.display = "none";
        }
    }, 100);

    isEditModeProduct = true;
    document.getElementById("form-title").innerText = "Chỉnh sửa sản phẩm";
    document.getElementById("save-btn").innerText = "Cập nhật";
    chuyengiaodien("list-products", "add-product");
}

function deleteProduct(row) {
    // Check permission before proceeding
    if (!userPermissions.canManageProducts) {
        announcement("Bạn không có quyền xóa sản phẩm!", "error-message");
        return;
    }

    const mavach = row.getAttribute("data-mavach");
    if (!mavach) {
        announcement("Không tìm thấy mã vạch của sản phẩm!", "error-message");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
        $.ajax({
            url: "../models/danhmuc_sanpham/deleteProduct.php",
            type: "POST",
            data: { mavach: mavach },
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
                console.log("Phản hồi từ server:", xhr.responseText);
                announcement(
                    "Lỗi khi gửi yêu cầu đến server: " + error,
                    "error-message"
                );
            },
        });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("add-product");
    if (form) {
        form.addEventListener("submit", addProduct);
    } else {
        console.log("Không tìm thấy form với id 'add-product'");
    }

    const productTable = document.getElementById("product-infor");
    if (productTable) {
        productTable.addEventListener("click", function (e) {
            if (e.target.classList.contains("edit-btn")) {
                e.preventDefault();
                const row = e.target.closest("tr");
                editProduct(row);
            }
            if (e.target.classList.contains("delete-btn")) {
                e.preventDefault();
                const row = e.target.closest("tr");
                deleteProduct(row);
            }
        });
    } else {
        console.log("Không tìm thấy tbody với id 'product-infor'");
    }
});
