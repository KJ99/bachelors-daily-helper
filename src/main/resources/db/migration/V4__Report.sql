create table reports (
    user_id varchar(255) not null,
    target_day varchar(10) not null,
    team_id int not null,
    last_time varchar(255) default null,
    today varchar(255) default null,
    problem varchar(255) default null,

    primary key (user_id, target_day, team_id)
);