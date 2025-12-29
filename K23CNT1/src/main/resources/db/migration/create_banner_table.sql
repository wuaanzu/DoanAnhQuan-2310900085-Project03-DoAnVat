-- Tạo bảng banner
CREATE TABLE IF NOT EXISTS banner (
    banner_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description VARCHAR(500),
    image VARCHAR(255),
    link_url VARCHAR(255),
    category_id INT,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE SET NULL
);

-- Thêm banner mẫu
INSERT INTO banner (title, description, image, link_url, category_id, display_order, is_active)
VALUES 
('Khuyến mãi đặc biệt', 'Giảm giá 20% cho tất cả sản phẩm', 'banner1.jpg', '/products', NULL, 1, TRUE),
('Sản phẩm mới', 'Khám phá các sản phẩm mới nhất', 'banner2.jpg', '/products', NULL, 2, TRUE);
