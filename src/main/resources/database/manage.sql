##删除当前表的记录，插入到历史表
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;

##将某天的记录由历史库插入到当前库
INSERT INTO config_file SELECT * FROM config_file_his where substring(store_path, 1,6)='150129';
INSERT INTO ot_level2 SELECT * FROM ot_level2_his where date_str='150310';
INSERT INTO ot_observe_record SELECT * FROM ot_observe_record_his where date_str='150310';
INSERT INTO fits_file_cut SELECT * FROM fits_file_cut_his where substring(store_path, 1,6)='150312';

##统计模板切图表中，某天切图未返回的数量
select dpm_id, count(ot_id) from fits_file_cut_ref where length(file_name)=21 and substring(store_path, 1, 6)='150129' group by dpm_id order by dpm_id;

##按照日期统计当天的一级OT数量
select date_str, count(oor_id) from ot_observe_record_his group by date_str order by count desc;

##按照出现次数统计出现该次数的OT的个数
select total, count(*) from ot_level2 where data_produce_method='8' group by total order by total asc;

##统计每天OT的个数
select date_str, count(*) total from ot_level2_his where data_produce_method='8' group by date_str order by total desc;

##统计OT的匹配个数
select ot_id, count(*) total from ot_level2_match group by ot_id order by total desc;

##删除有历史备份表的当前表的数据
delete from config_file;
delete from fits_file_cut;
delete from ot_observe_record;
delete from ot_level2;
delete from upload_file_unstore;
delete from upload_file_record;
delete from var_star_record;
update data_process_machine set cur_process_number=0;
update data_process_machine set first_record_number=0;

##删除历史表中某一条的记录
delete from config_file_his where substring(store_path, 1,6)='141224';
delete from ot_level2_his where date_str='141224';
delete from ot_observe_record_his where date_str='141224';
delete from fits_file_cut_his where substring(store_path, 1,6)='141224';

##归档
WITH moved_rows AS ( DELETE FROM ot_level2 RETURNING * ) INSERT INTO ot_level2_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM fits_file_cut RETURNING * ) INSERT INTO fits_file_cut_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM ot_observe_record RETURNING * ) INSERT INTO ot_observe_record_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;
WITH moved_rows AS ( DELETE FROM image_status_parameter RETURNING * ) INSERT INTO image_status_parameter_his SELECT * FROM moved_rows;
update data_process_machine set first_record_number=0, cur_process_number=0;

##多表条件更新
update fits_file_cut
set ot_id=ot_observe_record.ot_id
from ot_observe_record
where ot_observe_record.ffc_id=fits_file_cut.ffc_id and ot_observe_record.ot_id=79737;

update ot_level2
set mag=ot_observe_record.mag_aper
from ot_observe_record
where ot_observe_record.ot_id=ot_level2.ot_id and ot_observe_record.ff_number=ot_level2.last_ff_number;

##计算ot2与匹配上usno的星等差
select ot.name, ot.mag, olm.mag, ot.mag-olm.mag
from ot_level2 ot
inner join ot_level2_match olm on olm.ot_id=ot.ot_id
where ot.usno_match=1 and olm.mag!=0 and abs(ot.mag-olm.mag)>1;

##upload_file_unstore去掉存储路径后面的/
update upload_file_unstore set store_path=substring(store_path, 0, 24) where file_type='9';

##按望远镜、图像编号统计一级OT的个数
select dpm_id, ff_number, count(ff_number) number from ot_observe_record group by dpm_id, ff_number order by number desc limit 20;

##对OT后随记录中多个目标用同一个fr_obj_id的数量进行统计排序
select fo_id, fu_serial_number, fr_obj_id, count(fr_id) count from follow_up_record group by fo_id, fu_serial_number, fr_obj_id order by count desc limit 20;
select fo_id, fu_serial_number, fr_obj_id, count(fr_id) count from follow_up_record where ot_type='MINIOT' group by fo_id, fu_serial_number, fr_obj_id order by count desc limit 20;
select f1.fu_serial_number, f1.fr_obj_id, count(f1.fr_id) num 
from follow_up_record f1
inner join follow_up_record f2 USING(fr_id)
where f1.fo_id=331 
group by f1.fu_serial_number, f1.fr_obj_id 
order by f1.fu_serial_number, f1.fr_obj_id , num desc
HAVING count(f2.fr_id)=1
limit 20;

select DISTINCT ot_type from follow_up_record;