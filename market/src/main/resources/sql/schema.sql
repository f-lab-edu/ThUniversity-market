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

INSERT INTO member (name, email, password, university, auth, created_at, updated_at)
VALUES ('admin', 'admin@example.com', '$2a$10$TVB5rOGgv0w0O1Kr.T5yCeSaKsCdtM.kfjFRhrTj0egEgvV6pqm5y', 0, 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
