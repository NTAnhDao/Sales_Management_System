document.addEventListener('DOMContentLoaded', function () {
    const dropavatar = document.getElementById('drop-avatar');
    const avatarInput = document.getElementById('avatarInput');
    const preview = document.getElementById('preview');
    const saveAvatarBtn = document.getElementById('save-avatar-btn');

    let uploadedAvatar = null;

    dropavatar.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropavatar.classList.add('dragover');
    });

    dropavatar.addEventListener('dragleave', () => {
        dropavatar.classList.remove('dragover');
    });

    dropavatar.addEventListener('drop', (e) => {
        e.preventDefault();
        dropavatar.classList.remove('dragover');
        let files = e.dataTransfer.files;
        if (files.length > 0) {
            avatarInput.files = files; // Gán vào input
            handleFile(files[0]);
        }
    });

    avatarInput.addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            handleFile(file);
        }
    });

    saveAvatarBtn.addEventListener('click', () => {
        if (avatarInput.files.length > 0) {
            uploadedAvatar = avatarInput.files[0].name;
        }
    });

    function handleFile(file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
});

let editBtn = document.getElementById('edit-profile');
let editname = document.getElementById('name-edit');
