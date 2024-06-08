drop table if exists market.email;
drop table if exists market.member;

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

SET GLOBAL event_scheduler = ON;

CREATE EVENT delete_old_emails
ON SCHEDULE EVERY 1 MINUTE
DO
DELETE FROM email
WHERE created_at < NOW() - INTERVAL 1 MINUTE;