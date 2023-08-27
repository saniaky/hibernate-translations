# Hibernate Translations

## DDL

```sql
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
```

## Post sample

| id                                   | status              |
|:-------------------------------------|:--------------------|
| ecb3041e-52f1-473f-bce3-736b8dbe1975 | PENDING             |
| 40cdb05c-ff7a-4791-8f1e-933bd9152481 | IN_PROGRESS         |
| 4fe8ebe1-7dc9-473a-83a9-667158ccaa93 | DONE                |
| 96744cc1-9334-45d6-88ca-7d2f1467f22e | MISSING_TRANSLATION |

## Status translation sample

| code         | locale | translation |
|:-------------|:-------|:------------|
| PENDING      | en-US  | Pending     |
| PENDING      | ru-RU  | В обработке |
| IN\_PROGRESS | en-US  | In progress |
| IN\_PROGRESS | ru-RU  | В работе    |
| DONE         | en-US  | Done        |
| DONE         | ru-RU  | Завершено   |

# Generated Hibernate Query

```postgresql
select post0_.id                as col_0_0_,
       statustran1_.translation as col_1_0_,
       post0_.id                as id1_0_,
       post0_.status            as status2_0_
from app.post post0_
left outer join app.status_translation statustran1_ 
    on post0_.status = statustran1_.code and (statustran1_.locale = ?)
where statustran1_.translation like ?
order by statustran1_.translation desc
```