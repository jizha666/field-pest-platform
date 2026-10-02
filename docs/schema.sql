-- Development schema reconstructed from the thesis source and mapper SQL.
-- This project stores demo passwords in plain text. Do not use it in production.

CREATE DATABASE IF NOT EXISTS pest
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE pest;

CREATE TABLE IF NOT EXISTS `user` (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  name VARCHAR(20) NOT NULL,
  password VARCHAR(64) NOT NULL,
  identity TINYINT UNSIGNED NOT NULL COMMENT '1=admin, 0=visitor',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS area (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  province VARCHAR(20) NOT NULL,
  city VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS field (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  area_id INT UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  KEY idx_field_area_id (area_id),
  CONSTRAINT fk_field_area FOREIGN KEY (area_id) REFERENCES area(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS equipment (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  field_id INT UNSIGNED NOT NULL,
  category VARCHAR(20) NOT NULL,
  status TINYINT UNSIGNED NOT NULL COMMENT '0=normal, 1=abnormal',
  PRIMARY KEY (id),
  KEY idx_equipment_field_id (field_id),
  CONSTRAINT fk_equipment_field FOREIGN KEY (field_id) REFERENCES field(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS environment_record (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  category VARCHAR(20) NOT NULL,
  field_id INT UNSIGNED NOT NULL,
  value VARCHAR(50) NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_environment_field_time (field_id, update_time),
  CONSTRAINT fk_environment_field FOREIGN KEY (field_id) REFERENCES field(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pest_record (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  category VARCHAR(20) NOT NULL,
  field_id INT UNSIGNED NOT NULL,
  url VARCHAR(300) NOT NULL,
  number INT UNSIGNED NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_pest_field_time (field_id, update_time),
  CONSTRAINT fk_pest_field FOREIGN KEY (field_id) REFERENCES field(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (username, name, password, identity)
VALUES ('demo-admin', '演示管理员', 'demo123', 1),
       ('demo-user', '演示访客', 'demo123', 0)
ON DUPLICATE KEY UPDATE name = VALUES(name), password = VALUES(password), identity = VALUES(identity);

INSERT INTO area (province, city)
SELECT '江苏省', '南京市'
WHERE NOT EXISTS (SELECT 1 FROM area WHERE province = '江苏省' AND city = '南京市');

INSERT INTO field (name, area_id)
SELECT '演示一号田', id FROM area WHERE province = '江苏省' AND city = '南京市' LIMIT 1;
