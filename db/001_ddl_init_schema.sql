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

create table if not exists room_person (
  person_id int not null references person (id),
  room_id int not null references room (id)
);

