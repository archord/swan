##删除当前表的记录，插入到历史表
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;

##将某天的记录由历史库插入到当前库
INSERT INTO config_file SELECT * FROM config_file_his where substring(store_path, 1,6)='150129';

##统计模板切图表中，某天切图未返回的数量
select dpm_id, count(ot_id) from fits_file_cut_ref where length(file_name)=21 and substring(store_path, 1, 6)='150129' group by dpm_id order by dpm_id;

##按照日期统计当天的一级OT数量
select date_str, count(oor_id) from ot_observe_record_his group by date_str order by count desc;

##删除有历史备份表的当前表的数据
delete from config_file;
delete from fits_file_cut;
delete from ot_observe_record;
delete from ot_level2;

##归档
WITH moved_rows AS ( DELETE FROM ot_level2 RETURNING * ) INSERT INTO ot_level2_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM fits_file_cut RETURNING * ) INSERT INTO fits_file_cut_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM ot_observe_record RETURNING * ) INSERT INTO ot_observe_record_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;

