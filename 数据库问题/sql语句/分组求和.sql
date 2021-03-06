select box_code,
    sum(case  when t.ERROR_CODE like '%30404%'then 1 else 0 end) as "30404" ,
    sum(case  when t.ERROR_CODE like '%30301%'then 1 else 0 end) as  "30301",
    sum(case  when t.ERROR_CODE like '%20702%'then 1 else 0 end) as "20702" ,
    sum(case  when t.ERROR_CODE like '%21404%'then 1 else 0 end) as  "21404",
    sum(case  when t.ERROR_CODE like '%21304%'then 1 else 0 end) as  "21304",
    sum(case  when t.ERROR_CODE like '%20202%'then 1 else 0 end) as  "20202",
    sum(case  when t.ERROR_CODE like '%20104%'then 1 else 0 end) as "20104" ,
    sum(case  when t.ERROR_CODE like '%21203%'then 1 else 0 end) as  "21203",
    sum(case  when t.ERROR_CODE like '%30303%'then 1 else 0 end) as  "30303",
    sum(case  when t.ERROR_CODE like '%30304%'then 1 else 0 end) as  "30304",
    sum(case  when t.ERROR_CODE like '%20102%'then 1 else 0 end) as  "20102",
    sum(case  when t.ERROR_CODE like '%20601%'then 1 else 0 end) as  "20601",
    sum(case  when t.ERROR_CODE like '%21104%'then 1 else 0 end) as "21104" ,
    sum(case  when t.ERROR_CODE like '%20301%'then 1 else 0 end) as  "20301",
    sum(case  when t.ERROR_CODE like '%21201%'then 1 else 0 end) as "21201" ,
    sum(case  when t.ERROR_CODE like '%21204%'then 1 else 0 end) as  "21204",
    sum(case  when t.ERROR_CODE like '%90104%'then 1 else 0 end) as  "90104",
    sum(case  when t.ERROR_CODE like '%21303%'then 1 else 0 end) as  "21303",
    sum(case  when t.ERROR_CODE like '%21504%'then 1 else 0 end) as  "21504",
    sum(case  when t.ERROR_CODE like '%20304%'then 1 else 0 end) as  "20304",
    sum(case  when t.ERROR_CODE like '%21703%'then 1 else 0 end) as "21703" ,
    sum(case  when t.ERROR_CODE like '%19003%'then 1 else 0 end) as  "19003",
    sum(case  when t.ERROR_CODE like '%20302%'then 1 else 0 end) as "20302" ,
    sum(case  when t.ERROR_CODE like '%21603%'then 1 else 0 end) as  "21603",
    sum(case  when t.ERROR_CODE like '%20604%'then 1 else 0 end) as "20604" ,
    sum(case  when t.ERROR_CODE like '%21301%'then 1 else 0 end) as "21301" ,
    sum(case  when t.ERROR_CODE like '%21701%'then 1 else 0 end) as "21701" ,
    sum(case  when t.ERROR_CODE like '%21702%'then 1 else 0 end) as "21702" ,
    sum(case  when t.ERROR_CODE like '%21002%'then 1 else 0 end) as "21002" ,
    sum(case  when t.ERROR_CODE like '%30403%'then 1 else 0 end) as "30403" ,
    sum(case  when t.ERROR_CODE like '%20204%'then 1 else 0 end) as "20204" ,
    sum(case  when t.ERROR_CODE like '%21102%'then 1 else 0 end) as "21102" ,
    sum(case  when t.ERROR_CODE like '%20703%'then 1 else 0 end) as "20703" ,
    sum(case  when t.ERROR_CODE like '%21604%'then 1 else 0 end) as "21604" ,
    sum(case  when t.ERROR_CODE like '%21001%'then 1 else 0 end) as  "21001",
    sum(case  when t.ERROR_CODE like '%30302%'then 1 else 0 end) as "30302" ,
    sum(case  when t.ERROR_CODE like '%21101%'then 1 else 0 end) as  "21101",
    sum(case  when t.ERROR_CODE like '%20701%'then 1 else 0 end) as "20701" ,
    sum(case  when t.ERROR_CODE like '%20203%'then 1 else 0 end) as "20203" ,
    sum(case  when t.ERROR_CODE like '%20303%'then 1 else 0 end) as "20303" ,
    sum(case  when t.ERROR_CODE like '%21602%'then 1 else 0 end) as "21602" ,
    sum(case  when t.ERROR_CODE like '%20103%'then 1 else 0 end) as "20103" ,
    sum(case  when t.ERROR_CODE like '%21302%'then 1 else 0 end) as "21302" ,
    sum(case  when t.ERROR_CODE like '%20602%'then 1 else 0 end) as "20602" ,
    sum(case  when t.ERROR_CODE like '%20603%'then 1 else 0 end) as "20603" ,
    sum(case  when t.ERROR_CODE like '%20503%'then 1 else 0 end) as "20503" ,
    sum(case  when t.ERROR_CODE like '%20502%'then 1 else 0 end) as "20502" ,
    sum(case  when t.ERROR_CODE like '%30401%'then 1 else 0 end) as  "30401",
    sum(case  when t.ERROR_CODE like '%20704%'then 1 else 0 end) as  "20704",
    sum(case  when t.ERROR_CODE like '%30402%'then 1 else 0 end) as "30402" ,
    sum(case  when t.ERROR_CODE like '%20403%'then 1 else 0 end) as "20403" ,
    sum(case  when t.ERROR_CODE like '%20501%'then 1 else 0 end) as "20501" ,
    sum(case  when t.ERROR_CODE like '%21202%'then 1 else 0 end) as "21202" ,
    sum(case  when t.ERROR_CODE like '%21103%'then 1 else 0 end) as "21103" ,
    sum(case  when t.ERROR_CODE like '%20504%'then 1 else 0 end) as "20504" ,
    sum(case  when t.ERROR_CODE like '%20401%'then 1 else 0 end) as "20401" 
    
    from update_testtable t where ERROR_CODE is not null  
    group by t.box_code