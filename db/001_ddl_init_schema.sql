create table role (
    id serial primary key not null,
    name text
);

create table person (
    id serial primary key not null,
    login text,
    password text
);

create table room (
    id serial primary key not null,
    name text,
    created timestamp
);

create table message (
    id serial primary key not null,
    body text,
    created timestamp,
    room_id int references room (id),
    person_id int references person (id)
);

create table if not exists person_role (
  person_id int not null references person (id),
  role_id int not null references role (id)
);

create table if not exists room_person (
  person_id int not null references person (id),
  room_id int not null references room (id)
);

