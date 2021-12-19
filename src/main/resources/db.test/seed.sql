   insert into cache_items (item_key, tag, item_value, expires_at) values
    ('key-1', 'USER_PROFILE', '{"first_name": "Francis"}', '3000-12-12 22:22:22'),
    ('key-1', 'USER_PROFILE', '{"first_name": "John"}', '1000-12-12 22:22:22'),
    ('key-2', 'USER_PROFILE', '{"first_name": "Ronald"}', '1000-12-12 22:22:22'),
    ('uid-100', 'USER_PROFILE', '{"first_name": "John", "last_name": "Doe", "id": "uid-100"}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '1000-12-12 22:22:22'),
    ('100', 'TEAM', '{"id": 100, "name": "First Local Team"}', '3000-12-12 22:22:22'),
    ('100', 'TEAM', '{"id": 100, "name": "First Expired Team"}', '1000-12-12 22:22:22');
insert into configurations (team_id, hourly_deadline, timezone) values
    (2, '22:00', 'Europe/Warsaw');
insert into reports (user_id, target_day, team_id, last_time, today, problem) values
    ('uid-100', '2030-01-01', 1, 'Last time', 'Today', 'Problem'),
    ('uid-100', current_date(), 2, 'Last time', 'Today', 'Problem'),
    ('uid-100', current_date(), 3, 'Last time', 'Today', 'Problem'),
    ('uid-100', current_date(), 4, 'Last time', 'Today', 'Problem');