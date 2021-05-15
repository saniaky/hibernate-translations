create schema if not exists app;
set search_path to app;

create table app.post
(
    id     UUID PRIMARY KEY,
    status varchar(50) NOT NULL
);

create table app.translations
(
    group_id    varchar(20) NOT NULL,
    code        varchar(20) NOT NULL,
    locale      char(5)     NOT NULL,
    translation varchar(20) NOT NULL,
    constraint pk_translations primary key (group_id, code, locale)
);

-- drop view if exists app.status_translation;
create view app.status_translation as
select t.code as code, t.locale as locale, t.translation as translation
from app.translations as t
where group_id = 'statusName';
