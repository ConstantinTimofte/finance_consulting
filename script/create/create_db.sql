
create table client
(
    id          SERIAL PRIMARY KEY,
    first_name   varchar(255) not null,
    last_name   varchar(255) not null,
    client      boolean      not null,
    payment      boolean,
    year_salary integer,
    email_adress  varchar(255),
    phone_number  varchar (20),
    adress      varchar(255),
    city        varchar(255),
    cnp         varchar(255),
    notes       varchar(10485760)
);


create table investment
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255) not null,
    description varchar(10485760)
);

create table client_investment
(   id                 SERIAL PRIMARY KEY ,
    id_client          integer not null,
    id_investment      integer not null,
    investment integer not null,
    mounth integer ,
    date_activation_invest date ,
    foreign key (id_client) references client (id),
    foreign key (id_investment) references investment (id)
)
