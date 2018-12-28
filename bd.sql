drop database if exists poo20182_lanchonete;
create database if not exists poo20182_lanchonete;

use poo20182_lanchonete;

-- DOWN
-- drop table if exists historico;
drop table if exists itens_pedido;
drop table if exists pedidos;
drop table if exists produtos;
drop table if exists tipos;
-- drop table if exists usuarios;
-- ENDDOWN

-- UP
-- create table usuarios(
--     id          int primary key auto_increment,
--     nome        varchar(255)
-- );

create table tipos(
    id          int primary key auto_increment,
    tipo        varchar(255)
);

create table produtos(
    id          int primary key auto_increment,
    tipo_id     int,
    nome        varchar(255),
    preco       decimal,
    foreign key (tipo_id) references tipos(id)
);

create table pedidos(
    id          int primary key auto_increment,
    pronto      boolean,
    pago        boolean,
    pedido_em   timestamp default current_timestamp
);

create table itens_pedido(
    id          int primary key auto_increment,
    produto_id  int,
    pedido_id   int,
    quantidade  int,
    pronto      boolean,
    foreign key (produto_id) references produtos(id),
    foreign key (pedido_id) references pedidos(id)
);

-- create table historico(
--     id             int primary key auto_increment,
--     item_pedido_id int,
--     usuario_id     int,
--     pronto_em      timestamp default current_timestamp,
--     foreign key (item_pedido_id) references itens_pedido(id),
--     foreign key (usuario_id) references usuarios(id)
-- );
-- ENDUP

-- drop user if exists web20182;
create user if not exists poo20182 identified with mysql_native_password by 'poo20182';
grant all privileges on poo20182_lanchonete.* to poo20182;