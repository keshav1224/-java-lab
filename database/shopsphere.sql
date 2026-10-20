-- ShopSphere Electronics E-Commerce Database (MySQL 8.0+)
CREATE DATABASE IF NOT EXISTS shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

CREATE TABLE users (
    user_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    mobile       VARCHAR(20),
    role         ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status       TINYINT(1) NOT NULL DEFAULT 1,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE addresses (
    address_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city         VARCHAR(100) NOT NULL,
    state        VARCHAR(100) NOT NULL,
    pincode      VARCHAR(10)  NOT NULL,
    address_type ENUM('HOME','WORK','OTHER') DEFAULT 'HOME',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE categories (
    category_id  INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL UNIQUE,
    description  TEXT,
    status       TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE products (
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_id  INT NOT NULL,
    name         VARCHAR(200) NOT NULL,
    brand        VARCHAR(100),
    description  TEXT,
    price        DECIMAL(12,2) NOT NULL,
    discount     DECIMAL(5,2) NOT NULL DEFAULT 0,
    stock        INT NOT NULL DEFAULT 0,
    image_url    VARCHAR(500),
    status       TINYINT(1) NOT NULL DEFAULT 1,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    INDEX idx_products_category (category_id),
    INDEX idx_products_name (name),
    FULLTEXT idx_products_search (name, brand, description)
) ENGINE=InnoDB;

CREATE TABLE cart (
    cart_id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    product_id   INT NOT NULL,
    quantity     INT NOT NULL DEFAULT 1,
    UNIQUE KEY uq_cart_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE wishlist (
    wishlist_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    product_id   INT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_wish_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE orders (
    order_id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT NOT NULL,
    address_id     INT NOT NULL,
    total_amount   DECIMAL(12,2) NOT NULL,
    discount       DECIMAL(12,2) NOT NULL DEFAULT 0,
    payment_method VARCHAR(50) NOT NULL,
    payment_status ENUM('PENDING','PAID','FAILED','REFUNDED') DEFAULT 'PENDING',
    order_status   ENUM('PLACED','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PLACED',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (address_id) REFERENCES addresses(address_id),
    INDEX idx_orders_user (user_id)
) ENGINE=InnoDB;

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id      INT NOT NULL,
    product_id    INT NOT NULL,
    quantity      INT NOT NULL,
    price         DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB;

CREATE TABLE payments (
    payment_id           INT AUTO_INCREMENT PRIMARY KEY,
    order_id             INT NOT NULL,
    payment_method       VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(255),
    amount               DECIMAL(12,2) NOT NULL,
    payment_status       ENUM('PENDING','SUCCESS','FAILED') DEFAULT 'PENDING',
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE reviews (
    review_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    product_id  INT NOT NULL,
    rating      TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_review_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE coupons (
    coupon_id        INT AUTO_INCREMENT PRIMARY KEY,
    code             VARCHAR(50) NOT NULL UNIQUE,
    discount_type    ENUM('FLAT','PERCENT') NOT NULL,
    discount_value   DECIMAL(10,2) NOT NULL,
    minimum_order    DECIMAL(10,2) DEFAULT 0,
    maximum_discount DECIMAL(10,2),
    expiry_date      DATE,
    status           TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE coupon_usage (
    usage_id  INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id   INT NOT NULL,
    order_id  INT NOT NULL,
    used_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE=InnoDB;

-- Seed data
-- BCrypt hashes: admin123 / customer123
INSERT INTO users (name, email, password, mobile, role) VALUES
('Admin', 'admin@shopsphere.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '9999999999', 'ADMIN'),
('John Customer', 'john@example.com', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfgqhIRX5Q0N7XQ0GxH1Q0N7XQ0GxH1', '8888888888', 'CUSTOMER');

INSERT INTO categories (name, description) VALUES
('Laptops', 'Portable computers and notebooks'),
('Smartphones', 'Mobile phones and accessories'),
('Audio', 'Headphones, speakers, and earbuds'),
('Wearables', 'Smartwatches and fitness trackers'),
('TV & Displays', 'Televisions and monitors'),
('Accessories', 'Cables, chargers, and peripherals');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 'MacBook Pro 14" M3', 'Apple', 'Apple M3 chip, 16GB RAM, 512GB SSD, 14-inch Liquid Retina XDR.', 199900.00, 5, 12, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600', 1),
(1, 'Dell XPS 15', 'Dell', 'Intel Core i7-13700H, 16GB DDR5, 512GB SSD, 15.6" OLED.', 164900.00, 8, 20, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=600', 1),
(2, 'iPhone 15 Pro', 'Apple', 'A17 Pro chip, 6.1" Super Retina XDR, 256GB, Titanium Blue.', 129900.00, 3, 30, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600', 1),
(2, 'Samsung Galaxy S24 Ultra', 'Samsung', 'Snapdragon 8 Gen 3, 6.8" AMOLED, 12GB RAM, 256GB.', 129999.00, 10, 25, 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=600', 1),
(3, 'Sony WH-1000XM5', 'Sony', 'Industry-leading noise cancellation, 30-hour battery.', 29990.00, 12, 45, 'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=600', 1),
(3, 'AirPods Pro 2', 'Apple', 'Active Noise Cancellation, adaptive transparency, USB-C.', 24900.00, 5, 60, 'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600', 1),
(4, 'Apple Watch Series 9', 'Apple', '45mm GPS, S9 SiP chip, Always-On Retina display.', 41900.00, 7, 18, 'https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?w=600', 1),
(4, 'Samsung Galaxy Watch 6', 'Samsung', '44mm Bluetooth, sapphire crystal, sleep coaching.', 27999.00, 15, 22, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600', 1),
(5, 'LG C3 55" OLED', 'LG', '55-inch evo OLED, 4K 120Hz, Dolby Vision.', 149990.00, 18, 8, 'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=600', 1),
(5, 'Dell UltraSharp 27" 4K', 'Dell', '27-inch IPS Black, 4K UHD, USB-C hub, 98% DCI-P3.', 54990.00, 6, 15, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600', 1),
(6, 'Anker 65W GaN Charger', 'Anker', 'Compact 3-port GaN charger with USB-C PD and USB-A.', 4999.00, 10, 100, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600', 1),
(6, 'Logitech MX Master 3S', 'Logitech', 'Wireless performance mouse, 8K DPI, quiet clicks.', 8995.00, 8, 55, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600', 1);

INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
('WELCOME10', 'PERCENT', 10, 5000, 2000, '2027-12-31', 1),
('ELEC500',   'FLAT',    500, 10000, NULL, '2027-12-31', 1);

show tables;
