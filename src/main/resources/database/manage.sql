##删除当前表的记录，插入到历史表
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;

##将某天的记录由历史库插入到当前库
INSERT INTO config_file SELECT * FROM config_file_his where substring(store_path, 1,6)='150129';
INSERT INTO ot_level2 SELECT * FROM ot_level2_his where date_str='151218';
INSERT INTO ot_observe_record SELECT * FROM ot_observe_record_his where date_str='151218';
INSERT INTO fits_file_cut SELECT * FROM fits_file_cut_his where substring(store_path, 1,6)='151218';

#展示151218那天的ot1，最大图像编号是2591
update data_process_machine set cur_process_number=2400;

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
delete from var_star_record;
update data_process_machine set first_record_number=0, cur_process_number=0;
delete from image_status_parameter;
##删除时注意
delete from upload_file_unstore;
delete from upload_file_record;

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
delete from upload_file_unstore;

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

##miniGWAC中OT2与USNO库在不同匹配半径时不同星等的匹配成功与失败数量曲线
select DISTINCT ot_type from follow_up_record;

(select 71 mag, count(*) from ot_level2_120 where mag>6.8 and mag<8 and usno_match=0)
union
(select 72 mag, count(*) from ot_level2_120 where mag>6.8 and mag<8 and usno_match>0)
union
(select 81 mag, count(*) from ot_level2_120 where mag>=8 and mag<9 and usno_match=0)
union
(select 82 mag, count(*) from ot_level2_120 where mag>=8 and mag<9 and usno_match>0)
union
(select 91 mag, count(*) from ot_level2_120 where mag>=9 and mag<10 and usno_match=0)
union
(select 92 mag, count(*) from ot_level2_120 where mag>=9 and mag<10 and usno_match>0)
union
(select 101 mag, count(*) from ot_level2_120 where mag>=10 and mag<11 and usno_match=0)
union
(select 102 mag, count(*) from ot_level2_120 where mag>=10 and mag<11 and usno_match>0)
union
(select 111 mag, count(*) from ot_level2_120 where mag>=11 and mag<12 and usno_match=0)
union
(select 112 mag, count(*) from ot_level2_120 where mag>=11 and mag<12 and usno_match>0)
union
(select 121 mag, count(*) from ot_level2_120 where mag>=12 and mag<12.5 and usno_match=0)
union
(select 122 mag, count(*) from ot_level2_120 where mag>=12 and mag<12.5 and usno_match>0)
order by mag;

SELECT
	ot2. NAME,
	ot2.mag,
	ot2m.mag,
	ot2m.distance
FROM
	ot_level2 ot2
INNER JOIN ot_level2_match ot2m ON ot2m.ot_id = ot2.ot_id AND ot2m.mag<12
WHERE
	ot2.is_match = 2;

#找出所有为空场的ot2的切图文件名
SELECT ot2.name ot_name, ffc.file_name
FROM fits_file_cut ffc
INNER JOIN ot_level2 ot2 ON ot2.ot_id=ffc.ot_id AND ot2.ot_type=17
ORDER BY ot2.name, ffc.file_name;



SELECT name, array_to_string(array_agg(date_ut), ',')  dateUt,  array_to_string(array_agg(mag_aper),',')  mag,  
array_to_string(array_agg(magerr_aper),',')  magerr  
FROM ( select ot2.name, extract(epoch from oorh.date_ut-(select min(found_time_utc) from ot_level2_his where ot_id in (SELECT ot_id FROM ot_level2_match WHERE mt_id=5 AND match_id=4))) as date_ut, oorh.mag_aper, oorh.magerr_aper 
from ot_observe_record_his oorh 
inner join ot_level2_his ot2 on oorh.ot_id =ot2.ot_id and ot2.ot_id in
(SELECT ot_id FROM ot_level2_match ot2m WHERE ot2m.mt_id=5 AND ot2m.match_id=4)
order by oorh.date_ut 
) as rst 
group by name;

#按日期、天区、CCD编号查找交叉证认的OT1
select x, y, ra_d, dec_d, date_ut, mag_aper, ff_number, sky_id
from ot_observe_record_his
WHERE ot_id=0 AND data_produce_method='1' and date_str='160131' and dpm_id=2 and sky_id=34
ORDER BY sky_id, ff_number;

#按日期查找，将结果转换为json
#SELECT row_to_json((SELECT r FROM (SELECT mov_id, mov_detail) r))
SELECT JSON_AGG((SELECT r FROM (SELECT mov_id, mov_detail) r))::text
FROM( SELECT
	moor.mov_id as mov_id, JSON_AGG((SELECT r FROM (SELECT moor.ff_number, moor.ra_d, moor.dec_d, moor.date_ut) r)) as mov_detail
FROM (
		SELECT oor.ff_number, oor.ra_d, oor.dec_d, oor.x_temp, oor.y_temp, oor.date_ut, oor.oor_id, mor.mov_id
		FROM ot_observe_record oor
		LEFT JOIN move_object_record mor ON mor.oor_id = oor.oor_id
		WHERE mor.mov_id IS NOT NULL AND oor.date_str='151218'
		ORDER BY mov_id, date_ut
	)as moor
GROUP BY moor.mov_id
order by moor.mov_id
)as moor2

#按日期查找，将结果转换为json
SELECT text(JSON_AGG((SELECT r FROM (SELECT mov_id, tt_frm_num, mov_detail) r))) 
FROM( SELECT 
moor.mov_id as mov_id, moor.total_frame_number as tt_frm_num, JSON_AGG((SELECT r FROM (SELECT moor.ff_number, moor.ra_d, moor.dec_d, moor.date_ut) r)) as mov_detail 
FROM ( 
SELECT oor.ff_number, oor.ra_d, oor.dec_d, oor.x_temp, oor.y_temp, oor.date_ut, oor.oor_id, mor.mov_id, mo.total_frame_number
FROM ot_observe_record oor 
INNER JOIN move_object_record mor ON mor.oor_id = oor.oor_id 
INNER JOIN move_object mo ON mo.mov_id = mor.mov_id
WHERE oor.ot_id=0 and mor.mov_id IS NOT NULL AND oor.date_str='151218'
ORDER BY mov_id, date_ut, dec_d 
)as moor 
GROUP BY moor.mov_id, moor.total_frame_number
)as moor2