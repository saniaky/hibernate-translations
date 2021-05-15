insert into app.post (id, status)
values ('ecb3041e-52f1-473f-bce3-736b8dbe1975', 'PENDING'),
       ('40cdb05c-ff7a-4791-8f1e-933bd9152481', 'IN_PROGRESS'),
       ('4fe8ebe1-7dc9-473a-83a9-667158ccaa93', 'DONE'),
       ('96744cc1-9334-45d6-88ca-7d2f1467f22e', 'MISSING_TRANSLATION');

insert into app.translations (group_id, code, locale, translation)
values ('statusName', 'PENDING', 'en-US', 'Pending'),
       ('statusName', 'PENDING', 'ru-RU', 'В обработке'),
       ('statusName', 'IN_PROGRESS', 'en-US', 'In progress'),
       ('statusName', 'IN_PROGRESS', 'ru-RU', 'В работе'),
       ('statusName', 'DONE', 'en-US', 'Done'),
       ('statusName', 'DONE', 'ru-RU', 'Завершено');
