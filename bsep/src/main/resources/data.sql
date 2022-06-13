insert into role(id, name) values (0, 'ROLE_ADMIN');
insert into role(id, name) values (1, 'ROLE_CLIENT');

insert into client (id, username, password, full_name, email, role_id) values (1, 'admin', '$2a$12$QZbGfE/NIUWAcxxpT8/o/evBtG5iH/zYWiDuc/MJKpqJilNLRz3iG', 'Mirko Vojinovic', 'aaa@mail.com', 0);
insert into client (id, username, password, full_name, email, role_id) values (2, 'mitar', '$2a$12$QZbGfE/NIUWAcxxpT8/o/evBtG5iH/zYWiDuc/MJKpqJilNLRz3iG', 'Mitar Brankovic', 'bbb@mail.com', 1);
insert into client (id, username, password, full_name, email, role_id) values (3, 'radisa', '$2a$12$QZbGfE/NIUWAcxxpT8/o/evBtG5iH/zYWiDuc/MJKpqJilNLRz3iG', 'Radisa Milovcevic', 'ccc@mail.com', 1);