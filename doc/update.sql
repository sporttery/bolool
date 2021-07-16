update t_danchang_match a inner join (
select id,sp,result,w1,d1,l1,case result when '胜' then 1/(1/d1 + 1/l1) when '平' then 1/(1/w1 + 1/l1) else 1/(1/w1 + 1/d1) end as odds  from t_danchang_match where rq = '' and w1 > 1
) t on a.id = t.id set a.odds = t.odds where a.odds is null;

update t_danchang_match set return_race = 1/(1/sp + 1/odds) where return_race is null;


select avg(return_race) from t_danchang_match where return_race is not null;
