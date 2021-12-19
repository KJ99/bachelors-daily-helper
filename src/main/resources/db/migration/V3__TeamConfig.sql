create table configurations (
    team_id int not null primary key,
    hourly_deadline varchar(8) not null,
    timezone varchar(32) not null
);