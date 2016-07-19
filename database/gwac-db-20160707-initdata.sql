/*
Navicat PGSQL Data Transfer

Source Server         : gwac-web-bj
Source Server Version : 90305
Source Host           : 10.0.3.62:5432
Source Database       : gwac
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90305
File Encoding         : 65001

Date: 2016-07-05 15:30:29
*/


-- ----------------------------
-- Records of data_process_machine
-- ----------------------------
BEGIN;
INSERT INTO "data_process_machine" VALUES ('1', 'M01', '190.168.1.11   ', '1', '0', '0.71', '0.72', '2016-05-28 03:02:15.332509', '2016-05-19 14:10:54.314371', '19', '0');
INSERT INTO "data_process_machine" VALUES ('2', 'M02', '190.168.1.12   ', '1', '0', '0.58', '0.76', '2016-05-28 03:02:14.607597', '2016-05-19 14:11:23.715599', '19', '0');
INSERT INTO "data_process_machine" VALUES ('3', 'M03', '190.168.1.13   ', '2', '0', '0.71', '0.9', '2016-05-27 23:32:05.095977', '2016-05-19 14:06:42.496711', '37', '0');
INSERT INTO "data_process_machine" VALUES ('4', 'M04', '190.168.1.14   ', '2', '0', '0.71', '0.94', '2016-05-28 03:02:15.625272', '2016-05-19 14:11:50.061293', '6', '0');
INSERT INTO "data_process_machine" VALUES ('5', 'M05', '190.168.1.15   ', '3', '0', '0.74', '0.88', '2016-05-28 03:02:15.183231', '2016-05-19 14:12:20.769629', '35', '0');
INSERT INTO "data_process_machine" VALUES ('6', 'M06', '190.168.1.16   ', '3', '0', '0.74', '0.93', '2016-05-28 03:02:15.586897', '2016-05-19 14:13:13.318633', '35', '0');
INSERT INTO "data_process_machine" VALUES ('7', 'M07', '190.168.1.17   ', '4', '0', '0.55', '0.79', '2016-05-28 00:52:16.133006', '2016-05-19 14:13:48.411978', '33', '0');
INSERT INTO "data_process_machine" VALUES ('8', 'M08', '190.168.1.18   ', '4', '0', '0.1', '0.58', '2016-06-22 12:40:20.061859', '2016-05-19 14:14:11.307065', '15', '0');
INSERT INTO "data_process_machine" VALUES ('9', 'M09', '190.168.1.19   ', '5', '0', '0.71', '0.93', '2016-05-28 03:02:15.718702', '2016-05-19 14:14:43.23725', '37', '0');
INSERT INTO "data_process_machine" VALUES ('10', 'M10', '190.168.1.20   ', '5', '0', '0.63', '0.91', '2016-05-28 03:02:15.294522', '2016-05-19 14:14:58.824019', '37', '0');
INSERT INTO "data_process_machine" VALUES ('11', 'M11', '190.168.1.21   ', '6', '0', '0', '0.75', '2016-05-18 22:29:57.591582', '2016-05-19 14:15:05.511618', '37', '0');
INSERT INTO "data_process_machine" VALUES ('12', 'M12', '190.168.1.22   ', '6', '0', '0', '0.74', '2016-05-18 22:29:57.25754', '2016-05-19 14:15:27.903213', '37', '0');
COMMIT;

-- ----------------------------
-- Records of match_table
-- ----------------------------
BEGIN;
INSERT INTO "match_table" VALUES ('2', 'cvs', 'cvs', 'CVS');
INSERT INTO "match_table" VALUES ('3', 'merged_other', 'merged_other', '其他');
INSERT INTO "match_table" VALUES ('4', 'rc3', 'rc3', 'RC3');
INSERT INTO "match_table" VALUES ('5', 'minor_planet', 'aoop_longlat_', '小行星');
INSERT INTO "match_table" VALUES ('6', 'ot_level2_his', 'ot_level2_his', 'OT2历史');
INSERT INTO "match_table" VALUES ('7', 'usno', '_nomad', 'usno');
COMMIT;

-- ----------------------------
-- Records of multimedia_resource
-- ----------------------------
BEGIN;
INSERT INTO "multimedia_resource" VALUES ('1', 'After_school', null, 'resource/audio/After_school.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('2', 'Beep', null, 'resource/audio/Beep.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('3', 'Blues', null, 'resource/audio/Blues.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('4', 'Bon_voyage', null, 'resource/audio/Bon_voyage.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('5', 'Buzzer', null, 'resource/audio/Buzzer.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('6', 'Call_of_love', null, 'resource/audio/Call_of_love.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('7', 'Crow', null, 'resource/audio/Crow.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('8', 'Crystal', null, 'resource/audio/Crystal.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('9', 'Digital', null, 'resource/audio/Digital.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('10', 'Dreamland', null, 'resource/audio/Dreamland.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('11', 'East_ville_memory', null, 'resource/audio/East_ville_memory.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('12', 'Encounter', null, 'resource/audio/Encounter.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('13', 'Fantasy_city', null, 'resource/audio/Fantasy_city.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('14', 'Guitar', null, 'resource/audio/Guitar.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('15', 'Heartbeat', null, 'resource/audio/Heartbeat.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('16', 'Home', null, 'resource/audio/Home.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('17', 'Life', null, 'resource/audio/Life.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('18', 'Machinery_ringtone', null, 'resource/audio/Machinery_ringtone.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('19', 'Marimba', null, 'resource/audio/Marimba.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('20', 'Mario', null, 'resource/audio/Mario.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('21', 'Mischief', null, 'resource/audio/Mischief.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('22', 'Morning', null, 'resource/audio/Morning.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('23', 'Morning_dew', null, 'resource/audio/Morning_dew.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('24', 'Morning_run', null, 'resource/audio/Morning_run.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('25', 'Music_box', null, 'resource/audio/Music_box.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('26', 'Nighty-night', null, 'resource/audio/Nighty-night.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('27', 'open_beer', null, 'resource/audio/open_beer.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('28', 'Passion', null, 'resource/audio/Passion.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('29', 'Phone_ring', null, 'resource/audio/Phone_ring.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('30', 'Piano', null, 'resource/audio/Piano.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('31', 'Progress', null, 'resource/audio/Progress.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('32', 'Rain_dance', null, 'resource/audio/Rain_dance.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('33', 'Running_in_the_wind', null, 'resource/audio/Running_in_the_wind.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('34', 'Simplicity', null, 'resource/audio/Simplicity.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('35', 'Soaring', null, 'resource/audio/Soaring.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('36', 'Summer_joys', null, 'resource/audio/Summer_joys.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('37', 'Technology', null, 'resource/audio/Technology.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('38', 'That_afternoon', null, 'resource/audio/That_afternoon.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('39', 'The_cantor', null, 'resource/audio/The_cantor.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('40', 'The_kittens', null, 'resource/audio/The_kittens.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('41', 'The_pitch', null, 'resource/audio/The_pitch.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('42', 'The_World_Cup', null, 'resource/audio/The_World_Cup.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('43', 'Tumbler', null, 'resource/audio/Tumbler.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('44', 'Twilight_dance', null, 'resource/audio/Twilight_dance.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('45', 'Wake_up', null, 'resource/audio/Wake_up.mp3', '1', null);
INSERT INTO "multimedia_resource" VALUES ('46', 'Waltz', null, 'resource/audio/Waltz.mp3', '1', null);
COMMIT;

-- ----------------------------
-- Records of observation_sky
-- ----------------------------
BEGIN;
INSERT INTO "observation_sky" VALUES ('2', '260060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('3', '351020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('4', '300060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('5', '031020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('6', '266020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('8', '340060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('9', '020060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('10', '053020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('11', '074020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('12', '100060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('13', '010020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('15', '060060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('16', '117020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('19', '244020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('21', '329020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('29', '140060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('30', '138020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('31', '151003', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('32', '159020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('33', '180060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('34', '180020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('35', '220060', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('36', '202020', null, null, null, null, null, null, null, null);
INSERT INTO "observation_sky" VALUES ('37', '223020', null, null, null, null, null, null, null, null);
COMMIT;

-- ----------------------------
-- Records of ot_type
-- ----------------------------
BEGIN;
INSERT INTO "ot_type" VALUES ('0', '未分类', null, '0', '4');
INSERT INTO "ot_type" VALUES ('1', '假OT', null, '99', '4');
INSERT INTO "ot_type" VALUES ('2', '小行星', null, '51', '2');
INSERT INTO "ot_type" VALUES ('3', '移动目标', null, '50', '2');
INSERT INTO "ot_type" VALUES ('4', '鬼像', null, '98', '3');
INSERT INTO "ot_type" VALUES ('5', '坏像素', null, '99', '3');
INSERT INTO "ot_type" VALUES ('6', '坏像列', null, '99', '3');
INSERT INTO "ot_type" VALUES ('7', '热像素', null, '99', '3');
INSERT INTO "ot_type" VALUES ('8', 'OT候选体', null, '10', '1');
INSERT INTO "ot_type" VALUES ('9', '超新星', null, '2', '1');
INSERT INTO "ot_type" VALUES ('10', 'GRB', null, '1', '1');
INSERT INTO "ot_type" VALUES ('11', '耀发候选体', null, '21', '1');
INSERT INTO "ot_type" VALUES ('12', '灰尘', null, '99', '3');
INSERT INTO "ot_type" VALUES ('13', '彗星', null, '52', '2');
INSERT INTO "ot_type" VALUES ('14', '近地天体', null, '53', '2');
INSERT INTO "ot_type" VALUES ('15', '变星', null, '20', '1');
INSERT INTO "ot_type" VALUES ('16', '空场', null, '99', '3');
INSERT INTO "ot_type" VALUES ('17', '凹槽', null, '99', '3');
INSERT INTO "ot_type" VALUES ('18', '跟踪不好', null, '99', '3');
COMMIT;

-- ----------------------------
-- Records of process_status
-- ----------------------------
BEGIN;
INSERT INTO "process_status" VALUES ('1', 'skyCal                          ', '天文位置定标，图像正常');
INSERT INTO "process_status" VALUES ('2', 'skyCalBadImage                  ', '天文位置定标，但是图像不好');
INSERT INTO "process_status" VALUES ('3', 'TempMaking                      ', '模板检测和制作请求');
INSERT INTO "process_status" VALUES ('4', 'TempMakeWaiting                 ', '等待制作模板');
INSERT INTO "process_status" VALUES ('5', 'BadComImage                     ', '图像合并后，发现图像质量不好，不能作为模板图像');
INSERT INTO "process_status" VALUES ('6', 'ObjNumSmall                     ', '图像的星象数量太少');
INSERT INTO "process_status" VALUES ('7', 'Retrack                         ', '图像匹配连续多次不成功（暂定20次），可能是天文位置没有做好，重新进行天文位置定标');
INSERT INTO "process_status" VALUES ('8', 'UpdateTemp                      ', '正在进行模板更新');
INSERT INTO "process_status" VALUES ('9', 'LargeOT1                        ', '图像匹配完成后，发现单帧一级OT数目太多，暂定40个，可能是图像匹配或者像值不好所致');
INSERT INTO "process_status" VALUES ('10', 'ObjMonitor                      ', '图像处理一切正常');
INSERT INTO "process_status" VALUES ('11', 'InitialImage                    ', 'CCD初始化图像');
INSERT INTO "process_status" VALUES ('12', 'bias                            ', 'bias');
INSERT INTO "process_status" VALUES ('13', 'dark                            ', '拍摄暗场dark');
INSERT INTO "process_status" VALUES ('14', 'flat                            ', '拍摄平场flat');
INSERT INTO "process_status" VALUES ('15', 'focus                           ', '调焦校准中');
INSERT INTO "process_status" VALUES ('16', 'WrongCCDtype                    ', '图像文件头出错，将舍弃');
INSERT INTO "process_status" VALUES ('17', 'Darkcom                         ', '暗场合并中');
INSERT INTO "process_status" VALUES ('18', 'Flatcom                         ', '平常合并中');
INSERT INTO "process_status" VALUES ('19', 'Biascom                         ', 'bias合并中');
INSERT INTO "process_status" VALUES ('20', 'BadTempatureControl             ', '温度控制失效，即设置温度和实际温度相差比较大，暂定5度。');
INSERT INTO "process_status" VALUES ('21', 'TempMakingCollectionImg         ', '合并图像数目还不够，正在等待足够的数据进行图像合并。');
INSERT INTO "process_status" VALUES ('25', 'WongCCDtype                     ', null);
COMMIT;

-- ----------------------------
-- Records of user_info
-- ----------------------------
BEGIN;
INSERT INTO "user_info" VALUES ('0', 'mini-GWAC', null, null, '123456', 'mini-GWAC', null, null, null, null, null);
INSERT INTO "user_info" VALUES ('1', '徐洋', null, null, '123456', 'xy', null, null, null, null, null);
INSERT INTO "user_info" VALUES ('2', '魏建彦', null, null, '123456', 'wjy', null, null, null, null, null);
INSERT INTO "user_info" VALUES ('3', 'wjy', null, null, null, null, null, null, null, null, null);
COMMIT;

-- ----------------------------
-- Records of variation_type
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
