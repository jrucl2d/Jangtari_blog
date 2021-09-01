insert into member
(
    created_date
    , update_date
    , is_deleted
    , introduce
    , nickname
    , password
    , picture
    , role
    , username
) values
(
    NOW()
    , NOW()
    , FALSE
    , 'jangtariIntroduce'
    , 'jangtariNick'
    , '$2a$12$24EpMTjOWd4.WPvjWqctru4aQ7tYR5QlRnT5TZsmcjoVt/LAmKD/S'
    , 'jangtariProfile.jpg'
    , 'ADMIN'
    , 'jangtari'
);
