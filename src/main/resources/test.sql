select *, t1.translation as status_name
from app.post p
inner join app.translations t1 on (
  t1.group_id = 'statusName'
  and p.status = t1.code
  and t1.locale='en-US'
)
where t1.translation like '%progress%';


select *, t1.translation as status_name
from app.post p
left outer join app.translations t1 on (
    t1.group_id = 'statusName'
    and p.status = t1.code
    and t1.locale='en-US'
)
where coalesce(t1.translation, t1.code) like '%progress%';


select post0_.id as col_0_0_, localizedp1_.translation as col_1_0_, post0_.id as id1_0_, post0_.status as status2_0_
from app.post post0_
  left outer join app.translations localizedp1_
  on (localizedp1_.group_id = 'statusName' and post0_.status = localizedp1_.code and
                             localizedp1_.locale=?)
where localizedp1_.translation like ?
