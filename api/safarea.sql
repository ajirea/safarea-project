-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 30, 2019 at 04:02 AM
-- Server version: 10.4.11-MariaDB-1:10.4.11+maria~bionic-log
-- PHP Version: 7.2.25-1+ubuntu18.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `safarea`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED DEFAULT NULL,
  `buyer_id` int(10) UNSIGNED DEFAULT NULL,
  `address` varchar(255) NOT NULL COMMENT 'Address line 1',
  `village` varchar(30) NOT NULL,
  `district` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `province` varchar(30) NOT NULL,
  `postal_code` varchar(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`id`, `user_id`, `buyer_id`, `address`, `village`, `district`, `city`, `province`, `postal_code`) VALUES
(1, NULL, 6, 'Kp Selakopi RT 02 RW 11', 'Cijengkol', 'Caringin', 'Sukabumi', 'Jawa Barat', '43154');

-- --------------------------------------------------------

--
-- Table structure for table `api_tokens`
--

CREATE TABLE `api_tokens` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `token` varchar(16) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `api_tokens`
--

INSERT INTO `api_tokens` (`id`, `user_id`, `token`, `created_at`) VALUES
(4, 13, '3Ogvk5Y26ZYIygPY', '2019-12-24 10:04:01'),
(5, 14, '0IBuk4jHQJ2M33YU', '2019-12-26 07:48:42'),
(6, 16, 'p8V0f4xwDmsr6VWX', '2019-12-26 08:32:46'),
(7, 17, 'xaknALxc4gv6G12C', '2019-12-26 08:33:15');

-- --------------------------------------------------------

--
-- Table structure for table `buyers`
--

CREATE TABLE `buyers` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(70) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `buyers`
--

INSERT INTO `buyers` (`id`, `user_id`, `name`, `phone`, `deleted_at`, `created_at`) VALUES
(1, 14, 'John Terkapar', '085863331766', NULL, '2019-12-28 02:43:18'),
(6, 14, 'Barian Ogo', '0872373473622', NULL, '2019-12-28 10:29:18');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `buyer_id` int(10) UNSIGNED NOT NULL,
  `product_id` int(10) UNSIGNED NOT NULL,
  `price` decimal(12,2) NOT NULL,
  `profit_price` decimal(12,2) NOT NULL,
  `qty` int(11) NOT NULL,
  `total` decimal(12,2) NOT NULL COMMENT '(price+profit_price)*qty',
  `description` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `buyer_id`, `product_id`, `price`, `profit_price`, `qty`, `total`, `description`, `created_at`) VALUES
(7, 14, 1, 1, '80000.00', '30000.00', 2, '220000.00', 'Ukuran L warna hitam', '2019-12-29 20:57:00'),
(8, 14, 1, 1, '80000.00', '30000.00', 2, '220000.00', 'Ukuran L warna putih', '2019-12-29 20:57:00');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(120) NOT NULL,
  `slug` varchar(120) NOT NULL,
  `thumbnail` varchar(255) NOT NULL,
  `price` decimal(12,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `description` text NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `slug`, `thumbnail`, `price`, `stock`, `description`, `created_at`) VALUES
(1, 'Kids Pajama Short Sleeves', 'kids-pajama-1', 'pajama.png', '80000.00', 30, 'Lorem ipsum...', '2019-12-27 13:37:06');

-- --------------------------------------------------------

--
-- Table structure for table `product_images`
--

CREATE TABLE `product_images` (
  `id` int(10) UNSIGNED NOT NULL,
  `product_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(120) NOT NULL,
  `path` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product_images`
--

INSERT INTO `product_images` (`id`, `product_id`, `name`, `path`, `created_at`) VALUES
(1, 1, 'pajama-1.png', '/uploads/products/pajama-1.png', '2019-12-27 13:38:51');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `username` varchar(16) NOT NULL,
  `email` varchar(120) NOT NULL,
  `name` varchar(70) NOT NULL,
  `password` varchar(120) NOT NULL,
  `store_name` varchar(60) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `name`, `password`, `store_name`, `phone`, `avatar`, `is_admin`, `created_at`) VALUES
(8, 'asha', 'ashanaufal1@gmail.com', 'Naufal', '$2y$10$2ht0Z4gw3nTkydDde6.1ge0/iEFmhms029MtYRZL2X0Fqa7nQ4izS', 'Naufal Store 2', '088899922222', 'aang1.png', 0, '2019-12-24 04:37:20'),
(13, 'ashar', 'ashanaufal12@gmail.com', 'NaufalA!', '$2y$10$0WeIn2Bes0rLZFm5WleN0.Qmm3kTeB484U9EZAAHgw2z2v.nE/gKi', 'Naufal Store 2', '088899922222', 'aang1.png', 0, '2019-12-24 10:04:01'),
(14, 'satmaxt', 'satriamaxt@gmail.co', 'Satria Aji Putra', '$2y$10$lktkIvFMA98TzvIBwA1ri.r04n90LCM.wtkNMZa0qYMP.sgkExB8W', 'Stdev', '0872373473622', '/uploads/avatar/satmaxt.jpg', 0, '2019-12-26 07:48:42'),
(16, 'satmaxt2', 'satriamaxt@gmail.com2', 'Satria Aji Putra', '$2y$10$Hgk4OgM61UI1Xjkv1QIKHOeG4KVgumuDsu8zqPiU1hXycclFv5XtK', 'Stdev', '08723734736', '', 0, '2019-12-26 08:32:46'),
(17, 'satmaxt3', 'satriamaxt@gmail.com3', 'Satria Aji Putra', '$2y$10$3lzpa22OfB8ZowbS7qrZMewu/qLU9kXVVDC1yiGMMm.I7gflb.Us.', 'Stdev', '08723734736', '/uploads/avatar/satmaxt3.jpg', 0, '2019-12-26 08:33:15');

-- --------------------------------------------------------

--
-- Table structure for table `user_products`
--

CREATE TABLE `user_products` (
  `id` int(10) UNSIGNED NOT NULL,
  `product_id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `profit_price` decimal(12,2) NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 0,
  `status` varchar(15) NOT NULL COMMENT '''''take'''', ''''sending'''', ''''active'''', ''''non-active'''', ''''returning'''''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_products`
--

INSERT INTO `user_products` (`id`, `product_id`, `user_id`, `profit_price`, `qty`, `status`) VALUES
(1, 1, 14, '30000.00', 15, 'take');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `addresses_buyers` (`buyer_id`),
  ADD KEY `addresses_users` (`user_id`);

--
-- Indexes for table `api_tokens`
--
ALTER TABLE `api_tokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `api_tokens_users` (`user_id`);

--
-- Indexes for table `buyers`
--
ALTER TABLE `buyers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `buyers_users` (`user_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `orders_buyers` (`buyer_id`),
  ADD KEY `orders_users` (`user_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `products_ak_1` (`slug`);

--
-- Indexes for table `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_images_products` (`product_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_ak_1` (`username`,`email`,`store_name`,`phone`);

--
-- Indexes for table `user_products`
--
ALTER TABLE `user_products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_products_products` (`product_id`),
  ADD KEY `user_products_users` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `api_tokens`
--
ALTER TABLE `api_tokens`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `buyers`
--
ALTER TABLE `buyers`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `user_products`
--
ALTER TABLE `user_products`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `addresses_buyers` FOREIGN KEY (`buyer_id`) REFERENCES `buyers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `addresses_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `api_tokens`
--
ALTER TABLE `api_tokens`
  ADD CONSTRAINT `api_tokens_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `buyers`
--
ALTER TABLE `buyers`
  ADD CONSTRAINT `buyers_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_buyers` FOREIGN KEY (`buyer_id`) REFERENCES `buyers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `product_images_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `user_products`
--
ALTER TABLE `user_products`
  ADD CONSTRAINT `user_products_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `user_products_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;