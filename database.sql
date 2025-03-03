CREATE DATABASE restful_api;

-- USE restful_api;

CREATE TABLE users
(
    username    varchar(100) not null ,
    password    varchar(100) not null ,
    name    varchar(100) not null ,
    token varchar(100),
    token_expired_at bigint,
    primary key (username),
    unique (token)
);

CREATE TABLE contacts
(
    id varchar(100) not null ,
    username varchar(100) not null ,
    first_name varchar(100) not null ,
    last_name varchar(100),
    phone varchar(100),
    email varchar(100),
    primary key (id),
    constraint fk_users_contacts foreign key (username) references users(username)
);


CREATE TABLE addresses(
    id varchar(100) not null ,
    contact_id varchar(100) not null,
    street varchar(200),
    city varchar(100),
    province varchar(100),
    country varchar(100),
    postal_code varchar(100),
    primary key (id),
    constraint fk_contacts_addresses foreign key (contact_id) references contacts(id)
);
