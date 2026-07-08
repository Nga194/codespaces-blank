USE northwind;
DROP TABLE IF EXISTS taikhoan;

CREATE TABLE taikhoan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hoten VARCHAR(100) NOT NULL,
    taikhoan VARCHAR(50) NOT NULL UNIQUE,
    matkhau VARCHAR(255) NOT NULL,
    email VARCHAR(100)
);

INSERT INTO taikhoan (hoten, taikhoan, matkhau, email) VALUES
('Đặng Nga', 'admin', SHA2('010406', 256), 'nga@gmail.com');