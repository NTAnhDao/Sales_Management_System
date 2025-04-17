-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 12, 2025 at 03:16 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quanlytrangweb`
--

-- --------------------------------------------------------

--
-- Table structure for table `baocao`
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
-- Table structure for table `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id_donhang` int(11) NOT NULL,
  `id_sanpham` varchar(50) NOT NULL,
  `soluong` int(11) NOT NULL,
  `tongtien` decimal(10,0) NOT NULL,
  `giaban` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`id_donhang`, `id_sanpham`, `soluong`, `tongtien`, `giaban`) VALUES
(2, '12347', 1, 550000, 550000),
(2, '12348', 1, 2500000, 2500000),
(3, '12346', 2, 11380000, 5690000),
(4, '12354', 1, 160000, 160000),
(4, '12356', 2, 660000, 330000);
-- --------------------------------------------------------

--
-- Table structure for table `danhmucsp`
--

CREATE TABLE `danhmucsp` (
  `id` int(11) NOT NULL,
  `tendm` varchar(50) NOT NULL,
  `mota` varchar(100) DEFAULT NULL,
  `ngaytao` timestamp NOT NULL DEFAULT current_timestamp(),
  `id_nguoitao` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `danhmucsp`
--

INSERT INTO `danhmucsp` (`id`, `tendm`, `mota`, `ngaytao`, `id_nguoitao`) VALUES
(1, 'Điện thoại', NULL, '2025-03-09 14:55:14', NULL),
(2, 'Tai nghe', NULL, '2025-03-09 14:55:17', NULL),
(3, 'Sạc dự phòng', NULL, '2025-03-29 15:27:48', NULL),
(4, 'Ốp lưng', NULL, '2025-03-29 15:27:48', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `donhang`
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
-- Dumping data for table `donhang`
--

INSERT INTO `donhang` (`id`, `id_khachhang`, `id_nhanvien`, `tongtien`, `tienkhachdua`, `tientra`, `ngaymua`, `soluongsp`) VALUES
(1, 1, 2, 900000, 1000000, 100000, '2025-03-12 14:29:32', 2),
(2, 1, 3, 3050000, 3100000, 50000, '2025-04-04 12:16:55', 2),
(3, 3, 3, 11380000, 12000000, 620000, '2025-04-04 12:19:11', 2),
(4, 1, 2, 820000, 1000000, 180000, '2025-04-12 13:01:53', 3);
-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

CREATE TABLE `khachhang` (
  `id` int(11) NOT NULL,
  `hoten` varchar(50) NOT NULL,
  `diachi` text NOT NULL,
  `sdt` varchar(11) NOT NULL,
  `ngaytao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `khachhang`
--

INSERT INTO `khachhang` (`id`, `hoten`, `diachi`, `sdt`, `ngaytao`) VALUES
(1, 'Trương Lê Hiếu An', 'Bình Chánh', '0828828176', '2025-03-12 14:25:19'),
(2, 'Nguyễn Thị Anh Đào', 'Quận 7', '0289712912', '2025-03-12 14:25:19'),
(3, 'Nguyễn Thúc Thùy Tiên', 'Phú Mỹ Hưng, quận 7', '0717717717', '2025-03-29 15:17:47'),
(4, 'Trần Trung Dũng', 'Korea', '0552552552', '2025-03-29 15:18:43');

-- --------------------------------------------------------

--
-- Table structure for table `nhanvien`
--

CREATE TABLE `nhanvien` (
  `id` int(11) NOT NULL,
  `hoten` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `gioitinh` enum('Nữ','Nam') DEFAULT NULL,
  `sdt` varchar(11) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `diachi` varchar(100) DEFAULT NULL,
  `img` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `nhanvien`
--

INSERT INTO `nhanvien` (`id`, `hoten`, `email`, `gioitinh`, `sdt`, `ngaysinh`, `diachi`, `img`) VALUES
(1, 'Lee Min Hoo', 'admin@gmail.com', 'Nam', '828828176', NULL, NULL, 'user.jpg'),
(2, 'Trần Thị Mỹ Nữ', '52300048@student.tdtu.edu.vn', 'Nữ', NULL, NULL, NULL, 'user.jpg'),
(3, 'Nguyễn Thị Anh Đào', '52300014@student.tdtu.edu.vn', 'Nữ', NULL, NULL, NULL, 'user.jpg'),
(4, 'Trần Thị A', 'trannuu2005@gmail.com', 'Nam', NULL, NULL, NULL, 'user.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `sanpham`
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
-- Dumping data for table `sanpham`
--

INSERT INTO `sanpham` (`mavach`, `tensp`, `giagoc`, `giabanle`, `img`, `id_danhmuc`, `ngaytao`) VALUES
('12345', 'Điện thoại OPPO Reno12 5G 12GB/256GB', 10000000, 11490000, 'reno12-5g-3.png', 1, '2025-03-09 15:00:16'),
('12346', 'Tai nghe Bluetooth AirPods Pro Gen 2 MagSafe Charge (USB-C) Apple MTJV3', 4999999, 5690000, 'airpods-pro-2nd.jpg', 2, '2025-03-09 15:00:16'),
('12347', 'Tai nghe Bluetooth Chụp Tai Baseus Bass 35 Max', 450000, 550000, 'tai-nghe-bluetooth-chup-tai-baseus-bass-35-max-kem-5.jpg', 2, '2025-03-09 15:04:00'),
('12348', 'Tai nghe Bluetooth Chụp Tai Sony WH-CH720N', 2300000, 2500000, 'tai-nghe-bluetooth-chup-tai-sony.jpgjpg', 2, '2025-03-09 15:04:00'),
('12349', 'OPPO Find N5', 40000000, 45990000, 'oppo_find_n3_oppo.png', 1, '2025-03-29 15:24:40'),
('12350', 'Điện thoại iPhone 16 Plus 128GB', 20000000, 23690000, 'dien_thoai_apple_iphone_16_plus_hong_3.jpg', 1, '2025-03-29 15:24:40'),
('12351', 'Pin sạc dự phòng Polymer 20000mAh Type C 10.5W AVA+ G-DX258 kèm Cáp Lightning và Cáp Type C', 400000, 450000, 'pin-sac-du-phong_Ava.j'L, 3, '2025-03-29 16:08:36'),
('12352', 'Pin sạc dự phòng Polymer 20000mAh Type C PD QC 3.0 30W Anker Zolo A1681 kèm Cáp Lightning và Type C', 800000, 890000, NULL, 3, '2025-03-29 16:08:36'),
('12353', 'Ốp lưng iPhone 16 Pro Max nhựa cứng viền dẻo JM Bayer II', 180000, 200000, NULL, 4, '2025-03-29 16:10:55'),
('12354', 'Ốp lưng Galaxy S24 FE Nhựa cứng viền dẻo Jincase YF9261', 125000, 160000, NULL, 4, '2025-03-29 16:10:55'),
('12355', 'Ốp lưng Galaxy S24 Ultra Nhựa cứng viền dẻo Samsung kèm dây đứng Chính hãng', 650000, 833000, NULL, 4, '2025-03-29 16:13:27'),
('12356', 'Ốp lưng Galaxy A26 TPU có khe đựng thẻ Samsung Chính hãng', 250000, 330000, NULL, 4, '2025-03-29 16:13:27');

-- --------------------------------------------------------

--
-- Table structure for table `taikhoan`
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
-- Dumping data for table `taikhoan`
--

INSERT INTO `taikhoan` (`id`, `id_nhanvien`, `username`, `password`, `role`, `trangthai_hd`, `activate_token`, `expire_time`, `pass_changed`, `trangthai_khoa`) VALUES
(1, 1, 'admin', 'admin', 'Admin', 1, NULL, NULL, 1, 0),
(2, 2, '52300048', '52300048', 'Nhân viên', 1, 'e58d174b', 1744346223, 1, 0),
(3, 4, 'trannuu2005', '123456', 'Nhân viên', 1, 'e58d194a', 1744038659, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `baocao`
--
ALTER TABLE `baocao`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id_donhang`,`id_sanpham`),
  ADD KEY `fk_ctdh_sanpham` (`id_sanpham`);

--
-- Indexes for table `danhmucsp`
--
ALTER TABLE `danhmucsp`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_donhang_khachhang` (`id_khachhang`),
  ADD KEY `fk_donhang_nhanvien` (`id_nhanvien`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Indexes for table `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Indexes for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`mavach`),
  ADD KEY `fk_sanpham_danhmuc` (`id_danhmuc`);

--
-- Indexes for table `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_nhanvien` (`id_nhanvien`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `baocao`
--
ALTER TABLE `baocao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `danhmucsp`
--
ALTER TABLE `danhmucsp`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `taikhoan`
--
ALTER TABLE `taikhoan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `fk_chdh_donhang` FOREIGN KEY (`id_donhang`) REFERENCES `donhang` (`id`),
  ADD CONSTRAINT `fk_ctdh_sanpham` FOREIGN KEY (`id_sanpham`) REFERENCES `sanpham` (`mavach`);

--
-- Constraints for table `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `fk_donhang_khachhang` FOREIGN KEY (`id_khachhang`) REFERENCES `khachhang` (`id`),
  ADD CONSTRAINT `fk_donhang_nhanvien` FOREIGN KEY (`id_nhanvien`) REFERENCES `nhanvien` (`id`);

--
-- Constraints for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sanpham_danhmuc` FOREIGN KEY (`id_danhmuc`) REFERENCES `danhmucsp` (`id`);

--
-- Constraints for table `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `fk_taikhoan_nhanvien` FOREIGN KEY (`id_nhanvien`) REFERENCES `nhanvien` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
