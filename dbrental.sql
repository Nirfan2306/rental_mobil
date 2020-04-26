/*
 Navicat Premium Data Transfer

 Source Server         : a
 Source Server Type    : MySQL
 Source Server Version : 100411
 Source Host           : localhost:3306
 Source Schema         : dbrental

 Target Server Type    : MySQL
 Target Server Version : 100411
 File Encoding         : 65001

 Date: 26/04/2020 08:40:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for isi
-- ----------------------------
DROP TABLE IF EXISTS `isi`;
CREATE TABLE `isi`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `noservis` int(9) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_arbi
-- ----------------------------
DROP TABLE IF EXISTS `tb_arbi`;
CREATE TABLE `tb_arbi`  (
  `id_area` int(9) NOT NULL AUTO_INCREMENT,
  `jenis_kendaraan` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jam` int(2) NOT NULL,
  `tarif` decimal(12, 2) NOT NULL,
  `sopir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `bbm` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `area` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_area`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_arbi
-- ----------------------------
INSERT INTO `tb_arbi` VALUES (1, 'Mobilio', 12, 1400000.00, 'Adel', 'Bensin', 'Luar Kota');
INSERT INTO `tb_arbi` VALUES (2, 'Jazz', 24, 3500000.00, 'Adam', 'Solar', 'Dalam Kota');
INSERT INTO `tb_arbi` VALUES (4, 'BMW M3', 12, 1250000.00, 'Anto', 'Bensin', 'Dalam Kota');

-- ----------------------------
-- Table structure for tb_kendaraan
-- ----------------------------
DROP TABLE IF EXISTS `tb_kendaraan`;
CREATE TABLE `tb_kendaraan`  (
  `id_kendaraan` int(9) NOT NULL AUTO_INCREMENT,
  `nama_mobil` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `no_polisi` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `th_buat` int(4) NOT NULL,
  `jenis_mobil` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_kendaraan`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_kendaraan
-- ----------------------------
INSERT INTO `tb_kendaraan` VALUES (1, 'Mobilio', 'AD 1 S', 2018, '6 Seat');
INSERT INTO `tb_kendaraan` VALUES (2, 'Jazz', 'AD 4 M', 2016, '4 Seat');
INSERT INTO `tb_kendaraan` VALUES (3, 'BMW M3', 'AD 3 L', 2012, '4 Seat');
INSERT INTO `tb_kendaraan` VALUES (4, 'Xenia', 'AD 1 T', 2012, '6 Seat');
INSERT INTO `tb_kendaraan` VALUES (5, 'Mobilio', 'AD 5 BL', 2010, '6 Seat');

-- ----------------------------
-- Table structure for tb_klien
-- ----------------------------
DROP TABLE IF EXISTS `tb_klien`;
CREATE TABLE `tb_klien`  (
  `id_klien` int(9) NOT NULL AUTO_INCREMENT,
  `nama_klien` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `alamat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `telp` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_klien`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_klien
-- ----------------------------
INSERT INTO `tb_klien` VALUES (6, 'Joko', 'Jakarta', '085642789654');
INSERT INTO `tb_klien` VALUES (7, 'Dimas', 'Solo', '0815677722');
INSERT INTO `tb_klien` VALUES (8, 'Indra', 'Semarang', '0857895566');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id_user` int(9) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nama_lengkap` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `alamat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Admin ', 'Solo', 'Admin');
INSERT INTO `tb_user` VALUES (3, 'pegawai', '047aeeb234644b9e2d4138ed3bc7976a', 'Pegawai', 'Solo', 'User');
INSERT INTO `tb_user` VALUES (4, 'anton', 'e10adc3949ba59abbe56e057f20f883e', 'Anton', 'Solo', 'User');
INSERT INTO `tb_user` VALUES (5, 'andi', 'ce0e5bf55e4f71749eade7a8b95c4e46', 'Andi', 'Solo', 'User');

-- ----------------------------
-- Table structure for transaksi
-- ----------------------------
DROP TABLE IF EXISTS `transaksi`;
CREATE TABLE `transaksi`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `notransaksi` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pelanggan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mobil` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `biaya` decimal(12, 2) NOT NULL,
  `diskon` decimal(12, 2) NOT NULL,
  `total` decimal(12, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transaksi
-- ----------------------------
INSERT INTO `transaksi` VALUES (1, '001', 'Andi', 'Dalam Kota', 'Jazz', 3500000.00, 0.00, 3500000.00);
INSERT INTO `transaksi` VALUES (2, '002', 'Joko', 'Dalam Kota', 'BMW M3', 1250000.00, 0.00, 1250000.00);
INSERT INTO `transaksi` VALUES (3, '003', 'Dimas', 'Dalam Kota', 'Jazz', 3500000.00, 10.00, 3150000.00);

SET FOREIGN_KEY_CHECKS = 1;
