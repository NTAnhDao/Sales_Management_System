// function annoucement(message,id){
//     let messageID = document.getElementById(id)

//     if(message == null || message == undefined){
//         //hide
//         if(!messageID.classList.contains('d-none')){
//             messageID.classList.add('d-none')
//         }
//     }else{
//         messageID.classList.remove('d-none')
//         messageID.innerHTML = message
//     }
//     setTimeout(() => {
//         messageID.classList.add('d-none');
//     }, 2000);
// }

document.addEventListener('DOMContentLoaded', function () {
    const dropavatar = document.getElementById('drop-avatar');
    const avatarInput = document.getElementById('avatarInput');
    const preview = document.getElementById('preview');
    const saveFileBtn = document.getElementById('save-avatar-btn');

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

    saveFileBtn.addEventListener('click', () => {
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
