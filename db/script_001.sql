create table role (
    id serial primary key not null,
    name varchar(2000)
);

create table person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000)
);

create table room (
    id serial primary key not null,
    name varchar(2000),
    created timestamp
);

create table message (
    id serial primary key not null,
    body varchar,
    created timestamp,
    room_id int references room (id) ON DELETE CASCADE,
    person_id int references person (id) ON DELETE CASCADE
);

create table if not exists person_role (
  person_id int not null references person (id),
  role_id int not null references role (id)
);

insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_USER');
insert into person (login, password, role_id) values ('ban', '123', 1);
insert into person (login, password, role_id) values ('ivan', '123', 2);