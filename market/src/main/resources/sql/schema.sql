drop table if exists market.dibs;
drop table if exists market.item;
drop table if exists market.member;
drop table if exists market.email;

create table member(
      id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
      name varchar(255) NOT NULL,
      email varchar(255) NOT NULL UNIQUE,
      password varchar(255) NOT NULL,
      university int NOT NULL,
      auth enum('ROLE_USER','ROLE_VERIFY_USER','ROLE_ADMIN') NOT NULL,
      created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table email(
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(255) NOT NULL,
    verification_code varchar(255) NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table item(
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title varchar(255) NOT NULL,
    description TEXT NOT NULL,
    image_url varchar(255) NOT NULL,
    seller bigint NOT NULL,
    status enum('SELLING', 'TRADING', 'FINISH') NOT NULL,
    auction boolean NOT NULL,
    price int NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller) REFERENCES member(id)
);

create table dibs(
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item bigint NOT NULL,
    member bigint NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (item) REFERENCES item(id),
    FOREIGN KEY (member) REFERENCES member(id)
);

SET GLOBAL event_scheduler = ON;

CREATE EVENT delete_old_emails
ON SCHEDULE EVERY 1 MINUTE
DO
DELETE FROM email
WHERE created_at < NOW() - INTERVAL 1 MINUTE;