-- drop table if exists Account;
    create table if not exists Account
    (
        id       uuid default random_uuid() primary key,
        email    varchar unique                            not null,
        username varchar(60) check (length(username) >= 6) not null,
        password varchar check (length(password) >= 8)     not null,
        role     varchar(15)                               not null
    );
    commit;