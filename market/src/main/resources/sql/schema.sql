drop table if exists market.item;
drop table if exists market.member;

create table member(
      id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
      name varchar(255) NOT NULL,
      email varchar(255) NOT NULL,
      password varchar(255) NOT NULL,
      university int NOT NULL,
      auth enum('ROLE_USER','ROLE_ADMIN') NOT NULL,
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


