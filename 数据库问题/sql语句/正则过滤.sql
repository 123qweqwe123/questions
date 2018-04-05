with table1
as(

select ERROR_CODE from UPDATE_TESTTABLE
where ERROR_CODE is not null
)

select 
distinct col from(
select 
replace(REGEXP_SUBSTR(ERROR_CODE,'[0-9]{5}@@@',1,LEVEL,'i'),'@@@','') col
from table1
CONNECT BY LEVEL <= 1)


CONNECT BY 递归

