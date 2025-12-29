-- Migration Script: Thêm Authentication cho Staff
-- Chạy script này trong MySQL để cập nhật database

-- 1. Thêm columns username và password vào bảng staff
ALTER TABLE staff 
ADD COLUMN username VARCHAR(50) UNIQUE AFTER role,
ADD COLUMN password VARCHAR(255) AFTER username;

-- 2. Tạo tài khoản Admin mặc định
-- Username: admin
-- Password: admin123 (đã được hash bằng BCrypt)
INSERT INTO staff (staff_name, phone, email, address, role, username, password)
VALUES (
    'Administrator',
    '0123456789',
    'admin@mikifood.com',
    'Hà Nội',
    'ADMIN',
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu'
);

-- Password hash trên là của "admin123"
-- Bạn có thể đăng nhập với:
-- Username: admin
-- Password: admin123

-- 3. Tạo tài khoản User mẫu (optional)
INSERT INTO staff (staff_name, phone, email, address, role, username, password)
VALUES (
    'Nguyễn Văn A',
    '0987654321',
    'user@mikifood.com',
    'Hồ Chí Minh',
    'USER',
    'user',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu'
);

-- Password cho user cũng là "admin123"
-- Username: user
-- Password: admin123

-- 4. Kiểm tra kết quả
SELECT staff_id, staff_name, username, role FROM staff;
