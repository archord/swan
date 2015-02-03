
WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;

INSERT INTO config_file SELECT * FROM config_file_his where substring(store_path, 1,6)='150129';

select dpm_id, count(ot_id) from fits_file_cut_ref where length(file_name)=21 and substring(store_path, 1, 6)='150129' group by dpm_id order by dpm_id;

delete from config_file;
delete from fits_file_cut;
delete from ot_observe_record;
delete from ot_level2;



