# Đồ án cuối kỳ
# Hướng dẫn cài đặt và chạy dự án

1. Yêu cầu hệ thống
- Hệ điều hành: Windows hoặc macOS
- Phần mềm cần cài đặt: XAMPP
- Trình duyệt: Chrome, Firefox hoặc Safari

2. Cài đặt môi trường
- Cài đặt XAMPP. Sau khi cài đặt XAMPP, chạy Apache và MySQL từ bảng điều khiển của XAMPP.

3. Cấu hình dự án
- Bước 1: Tải dự án:
    Clone dự án vào thư mục htdocs của XAMPP.
    Ví dụ: C:\xampp\htdocs\

- Bước 2: Cấu hình cơ sở dữ liệu
    + Truy cập vào phpMyAdmin (`http://localhost/phpmyadmin/`).
    + Tạo một cơ sở dữ liệu mới với tên là `quanlytrangweb` hoặc tên khác(phải sửa lại thông tin kết nối MySQL).
    + Import file `quanlytrangweb.sql` trong thư mục `source/database` của dự án vào cơ sở dữ liệu mới tạo.

- Bước 3: Cấu hình kết nối cơ sở dữ liệu
    + Mở file cấu hình `db.php` trong thư mục `source/database` của dự án.
    + Sửa thông tin kết nối MySQL:
    ```
    $servername = "localhost";
    $username = "root";  //Tên người dùng
    $password = "";      //Mật khẩu (trống nếu không có mật khẩu)
    $dbname = "quanlytrangweb";  //Tên cơ sở dữ liệu đã tạo
    ```

4. Thông tin đăng nhập
- Tài khoản dùng để đăng nhập vào hệ thống admin
    + Username: admin
    + Password: admin

- Tài khoản dùng để đăng nhập vào hệ thống nhân viên
    + Username: 52300048
    + Password: 52300048

5. Chạy hệ thống
- Bước 1: Mở ứng dụng:
    Mở trình duyệt và nhập `http://localhost/d-an-cu-i-k/source/index.php` vào thanh địa chỉ.

- Bước 2: Đăng nhập:
    Sử dụng thông tin đăng nhập trên để truy cập vào hệ thống.

6. Link video demo: https://drive.google.com/file/d/1TqNmDWAwNaolEEVtPX4gt6wWIZcfxnAl/view?usp=sharing


