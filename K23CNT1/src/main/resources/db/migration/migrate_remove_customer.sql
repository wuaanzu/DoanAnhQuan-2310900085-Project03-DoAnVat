-- ============================================
-- Migration Script: Remove Customer Table
-- Sử dụng Staff cho cả Khách hàng và Nhân viên
-- ============================================

-- Bước 1: Thêm email vào bảng staff (nếu cần)
ALTER TABLE `staff` 
ADD COLUMN `email` VARCHAR(100) DEFAULT NULL AFTER `phone`,
ADD COLUMN `address` VARCHAR(255) DEFAULT NULL AFTER `email`;

-- Bước 2: Migrate dữ liệu từ customer sang staff
-- Chuyển khách hàng hiện tại thành staff với role 'Khách hàng'
INSERT INTO `staff` (`staff_name`, `phone`, `email`, `address`, `role`)
SELECT `full_name`, `phone`, `email`, `address`, 'Khách hàng'
FROM `customer`;

-- Bước 3: Lưu mapping customer_id -> staff_id để update orders
-- Tạo bảng tạm để lưu mapping
CREATE TEMPORARY TABLE customer_staff_mapping AS
SELECT 
    c.customer_id,
    s.staff_id
FROM customer c
INNER JOIN staff s ON c.email = s.email AND s.role = 'Khách hàng';

-- Bước 4: Thêm cột staff_id vào bảng orders
ALTER TABLE `orders` 
ADD COLUMN `staff_id` INT(11) DEFAULT NULL AFTER `order_id`;

-- Bước 5: Update orders với staff_id từ mapping
UPDATE `orders` o
INNER JOIN customer_staff_mapping m ON o.customer_id = m.customer_id
SET o.staff_id = m.staff_id;

-- Bước 6: Xóa foreign key constraint cũ
ALTER TABLE `orders` DROP FOREIGN KEY `orders_ibfk_1`;

-- Bước 7: Xóa cột customer_id
ALTER TABLE `orders` DROP COLUMN `customer_id`;

-- Bước 8: Thêm foreign key constraint mới
ALTER TABLE `orders` 
ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

-- Bước 9: Xóa bảng customer
DROP TABLE `customer`;

-- Bước 10: Xóa bảng tạm
DROP TEMPORARY TABLE IF EXISTS customer_staff_mapping;

-- ============================================
-- Kết quả: 
-- - Bảng customer đã bị xóa
-- - Bảng staff có thêm email và address
-- - Bảng orders liên kết với staff thay vì customer
-- - Dữ liệu khách hàng cũ đã được migrate sang staff
-- ============================================

-- Kiểm tra kết quả
SELECT * FROM staff;
SELECT * FROM orders;
