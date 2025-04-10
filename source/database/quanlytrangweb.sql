-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 10, 2025 lúc 06:07 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlytrangweb`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `baocao`
--

CREATE TABLE `baocao` (
  `id` int(11) NOT NULL,
  `moctg` enum('ngày','tuần','tháng','tùy chọn') NOT NULL,
  `ngaybd` date NOT NULL,
  `ngaykt` date NOT NULL,
  `tongsp` int(11) NOT NULL,
  `tongdonhang` int(11) NOT NULL,
  `tongtien` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id_donhang` int(11) NOT NULL,
  `id_sanpham` varchar(50) NOT NULL,
  `soluong` int(11) NOT NULL,
  `tongtien` decimal(10,0) NOT NULL,
  `giaban` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`id_donhang`, `id_sanpham`, `soluong`, `tongtien`, `giaban`) VALUES
(2, '12347', 1, 550000, 550000),
(2, '12348', 1, 2500000, 2500000),
(3, '12346', 2, 11380000, 5690000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhmucsp`
--

CREATE TABLE `danhmucsp` (
  `id` int(11) NOT NULL,
  `tendm` varchar(50) NOT NULL,
  `mota` varchar(100) DEFAULT NULL,
  `ngaytao` timestamp NOT NULL DEFAULT current_timestamp(),
  `id_nguoitao` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `danhmucsp`
--

INSERT INTO `danhmucsp` (`id`, `tendm`, `mota`, `ngaytao`, `id_nguoitao`) VALUES
(1, 'Điện thoại', NULL, '2025-03-09 14:55:14', NULL),
(2, 'Tai nghe', NULL, '2025-03-09 14:55:17', NULL),
(3, 'Sạc dự phòng', NULL, '2025-03-29 15:27:48', NULL),
(4, 'Ốp lưng', NULL, '2025-03-29 15:27:48', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `id` int(11) NOT NULL,
  `id_khachhang` int(11) NOT NULL,
  `id_nhanvien` int(11) NOT NULL,
  `tongtien` decimal(10,0) NOT NULL,
  `tienkhachdua` decimal(10,0) NOT NULL,
  `tientra` decimal(10,0) NOT NULL,
  `ngaymua` timestamp NOT NULL DEFAULT current_timestamp(),
  `soluongsp` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `donhang`
--

INSERT INTO `donhang` (`id`, `id_khachhang`, `id_nhanvien`, `tongtien`, `tienkhachdua`, `tientra`, `ngaymua`, `soluongsp`) VALUES
(1, 1, 2, 900000, 1000000, 100000, '2025-03-12 14:29:32', 2),
(2, 1, 3, 3050000, 3100000, 50000, '2025-04-04 12:16:55', 2),
(3, 5, 3, 11380000, 12000000, 620000, '2025-04-04 12:19:11', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `id` int(11) NOT NULL,
  `hoten` varchar(50) NOT NULL,
  `diachi` text NOT NULL,
  `sdt` varchar(11) NOT NULL,
  `ngaytao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`id`, `hoten`, `diachi`, `sdt`, `ngaytao`) VALUES
(1, 'Trương Lê Hiếu An', 'Bình Chánh', '0828828176', '2025-03-12 14:25:19'),
(2, 'Nguyễn Thị Anh Đào', 'Quận 7', '0289712912', '2025-03-12 14:25:19'),
(3, 'Nguyễn Thúc Thùy Tiên', 'Phú Mỹ Hưng, quận 7', '0717717717', '2025-03-29 15:17:47'),
(5, 'Trần Trung Dũng', 'Korea', '0552552552', '2025-03-29 15:18:43');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `id` int(11) NOT NULL,
  `hoten` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `gioitinh` enum('Nữ','Nam') DEFAULT NULL,
  `sdt` int(11) DEFAULT NULL,
  `img` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`id`, `hoten`, `email`, `gioitinh`, `sdt`, `img`) VALUES
(1, 'Lee Min Hoo', 'admin@gmail.com', 'Nam', 828828176, 'user.jpg'),
(2, 'Trần Thị Mỹ Nữ', '52300048@student.tdtu.edu.vn', 'Nữ', NULL, 'user.jpg'),
(3, 'Nguyễn Thị Anh Đào', '52300014@student.tdtu.edu.vn', 'Nữ', NULL, 'user.jpg'),
(7, 'Trần Thị A', 'trannuu2005@gmail.com', 'Nam', 0, 'user.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `mavach` varchar(50) NOT NULL,
  `tensp` varchar(100) NOT NULL,
  `giagoc` decimal(10,0) NOT NULL,
  `giabanle` decimal(10,0) NOT NULL,
  `img` varchar(50) DEFAULT NULL,
  `id_danhmuc` int(11) NOT NULL,
  `ngaytao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`mavach`, `tensp`, `giagoc`, `giabanle`, `img`, `id_danhmuc`, `ngaytao`) VALUES
('12345', 'Điện thoại OPPO Reno12 5G 12GB/256GB', 10000000, 11490000, NULL, 1, '2025-03-09 15:00:16'),
('12346', 'Tai nghe Bluetooth AirPods Pro Gen 2 MagSafe Charge (USB-C) Apple MTJV3', 4999999, 5690000, NULL, 2, '2025-03-09 15:00:16'),
('12347', 'Tai nghe Bluetooth Chụp Tai Baseus Bass 35 Max', 450000, 550000, NULL, 2, '2025-03-09 15:04:00'),
('12348', 'Tai nghe Bluetooth Chụp Tai Sony WH-CH720N', 2300000, 2500000, NULL, 2, '2025-03-09 15:04:00'),
('12349', 'OPPO Find N5', 40000000, 45990000, NULL, 1, '2025-03-29 15:24:40'),
('12350', 'Điện thoại iPhone 16 Plus 128GB', 20000000, 23690000, NULL, 1, '2025-03-29 15:24:40'),
('12351', 'Pin sạc dự phòng Polymer 20000mAh Type C 10.5W AVA+ G-DX258 kèm Cáp Lightning và Cáp Type C', 400000, 450000, NULL, 3, '2025-03-29 16:08:36'),
('12352', 'Pin sạc dự phòng Polymer 20000mAh Type C PD QC 3.0 30W Anker Zolo A1681 kèm Cáp Lightning và Type C', 800000, 890000, NULL, 3, '2025-03-29 16:08:36'),
('12353', 'Ốp lưng iPhone 16 Pro Max nhựa cứng viền dẻo JM Bayer II', 180000, 200000, NULL, 4, '2025-03-29 16:10:55'),
('12354', 'Ốp lưng Galaxy S24 FE Nhựa cứng viền dẻo Jincase YF9261', 125000, 160000, NULL, 4, '2025-03-29 16:10:55'),
('12355', 'Ốp lưng Galaxy S24 Ultra Nhựa cứng viền dẻo Samsung kèm dây đứng Chính hãng', 650000, 833000, NULL, 4, '2025-03-29 16:13:27'),
('12356', 'Ốp lưng Galaxy A26 TPU có khe đựng thẻ Samsung Chính hãng', 250000, 330000, NULL, 4, '2025-03-29 16:13:27');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `id` int(11) NOT NULL,
  `id_nhanvien` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `role` enum('Admin','Nhân viên') NOT NULL,
  `trangthai_hd` tinyint(1) NOT NULL,
  `activate_token` varchar(10) DEFAULT NULL,
  `expire_time` int(11) DEFAULT NULL,
  `pass_changed` tinyint(1) DEFAULT NULL,
  `trangthai_khoa` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`id`, `id_nhanvien`, `username`, `password`, `role`, `trangthai_hd`, `activate_token`, `expire_time`, `pass_changed`, `trangthai_khoa`) VALUES
(1, 1, 'admin', 'admin', 'Admin', 1, NULL, NULL, 1, 0),
(2, 2, '52300048', '52300048', 'Nhân viên', 1, 'e58d174b', 1744036625, 1, 0),
(3, 7, 'trannuu2005', '123456', 'Nhân viên', 1, 'e58d194a', 1744038659, 1, 0);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `baocao`
--
ALTER TABLE `baocao`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id_donhang`,`id_sanpham`),
  ADD KEY `fk_ctdh_sanpham` (`id_sanpham`);

--
-- Chỉ mục cho bảng `danhmucsp`
--
ALTER TABLE `danhmucsp`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_donhang_khachhang` (`id_khachhang`),
  ADD KEY `fk_donhang_nhanvien` (`id_nhanvien`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`mavach`),
  ADD KEY `fk_sanpham_danhmuc` (`id_danhmuc`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_nhanvien` (`id_nhanvien`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `baocao`
--
ALTER TABLE `baocao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `danhmucsp`
--
ALTER TABLE `danhmucsp`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `fk_chdh_donhang` FOREIGN KEY (`id_donhang`) REFERENCES `donhang` (`id`),
  ADD CONSTRAINT `fk_ctdh_sanpham` FOREIGN KEY (`id_sanpham`) REFERENCES `sanpham` (`mavach`);

--
-- Các ràng buộc cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `fk_donhang_khachhang` FOREIGN KEY (`id_khachhang`) REFERENCES `khachhang` (`id`),
  ADD CONSTRAINT `fk_donhang_nhanvien` FOREIGN KEY (`id_nhanvien`) REFERENCES `nhanvien` (`id`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sanpham_danhmuc` FOREIGN KEY (`id_danhmuc`) REFERENCES `danhmucsp` (`id`);

--
-- Các ràng buộc cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `fk_taikhoan_nhanvien` FOREIGN KEY (`id_nhanvien`) REFERENCES `nhanvien` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
