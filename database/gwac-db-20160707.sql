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

Date: 2016-07-05 15:11:01
*/


-- ----------------------------
-- Sequence structure for cf_id_seq
-- ----------------------------
DROP SEQUENCE "cf_id_seq";
CREATE SEQUENCE "cf_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 5779321
 CACHE 1;
SELECT setval('"public"."cf_id_seq"', 5779321, true);

-- ----------------------------
-- Sequence structure for cpf_id_seq
-- ----------------------------
DROP SEQUENCE "cpf_id_seq";
CREATE SEQUENCE "cpf_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 57
 CACHE 1;
SELECT setval('"public"."cpf_id_seq"', 57, true);

-- ----------------------------
-- Sequence structure for dpm_id_seq
-- ----------------------------
DROP SEQUENCE "dpm_id_seq";
CREATE SEQUENCE "dpm_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 13
 CACHE 1;
SELECT setval('"public"."dpm_id_seq"', 13, true);

-- ----------------------------
-- Sequence structure for env_id_seq
-- ----------------------------
DROP SEQUENCE "env_id_seq";
CREATE SEQUENCE "env_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ff_id_seq
-- ----------------------------
DROP SEQUENCE "ff_id_seq";
CREATE SEQUENCE "ff_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 3134420
 CACHE 1;
SELECT setval('"public"."ff_id_seq"', 3134420, true);

-- ----------------------------
-- Sequence structure for ffc_id_seq
-- ----------------------------
DROP SEQUENCE "ffc_id_seq";
CREATE SEQUENCE "ffc_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 9057685
 CACHE 1;
SELECT setval('"public"."ffc_id_seq"', 9057685, true);

-- ----------------------------
-- Sequence structure for ffcr_id_seq
-- ----------------------------
DROP SEQUENCE "ffcr_id_seq";
CREATE SEQUENCE "ffcr_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 142319
 CACHE 1;
SELECT setval('"public"."ffcr_id_seq"', 142319, true);

-- ----------------------------
-- Sequence structure for fo_id_seq
-- ----------------------------
DROP SEQUENCE "fo_id_seq";
CREATE SEQUENCE "fo_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 2905
 CACHE 1;
SELECT setval('"public"."fo_id_seq"', 2905, true);

-- ----------------------------
-- Sequence structure for fr_id_seq
-- ----------------------------
DROP SEQUENCE "fr_id_seq";
CREATE SEQUENCE "fr_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 96688
 CACHE 1;
SELECT setval('"public"."fr_id_seq"', 96688, true);

-- ----------------------------
-- Sequence structure for fuf_id_seq
-- ----------------------------
DROP SEQUENCE "fuf_id_seq";
CREATE SEQUENCE "fuf_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 27244
 CACHE 1;
SELECT setval('"public"."fuf_id_seq"', 27244, true);

-- ----------------------------
-- Sequence structure for fuo_id_seq
-- ----------------------------
DROP SEQUENCE "fuo_id_seq";
CREATE SEQUENCE "fuo_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 5159
 CACHE 1;
SELECT setval('"public"."fuo_id_seq"', 5159, true);

-- ----------------------------
-- Sequence structure for fuo_type_id_seq
-- ----------------------------
DROP SEQUENCE "fuo_type_id_seq";
CREATE SEQUENCE "fuo_type_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 8
 CACHE 1;
SELECT setval('"public"."fuo_type_id_seq"', 8, true);

-- ----------------------------
-- Sequence structure for gmb_id_seq
-- ----------------------------
DROP SEQUENCE "gmb_id_seq";
CREATE SEQUENCE "gmb_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for grb_id_seq
-- ----------------------------
DROP SEQUENCE "grb_id_seq";
CREATE SEQUENCE "grb_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for isp_id_seq
-- ----------------------------
DROP SEQUENCE "isp_id_seq";
CREATE SEQUENCE "isp_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 2889533
 CACHE 1;
SELECT setval('"public"."isp_id_seq"', 2889533, true);

-- ----------------------------
-- Sequence structure for mr_id_seq
-- ----------------------------
DROP SEQUENCE "mr_id_seq";
CREATE SEQUENCE "mr_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 46
 CACHE 1;
SELECT setval('"public"."mr_id_seq"', 46, true);

-- ----------------------------
-- Sequence structure for mt_id_seq
-- ----------------------------
DROP SEQUENCE "mt_id_seq";
CREATE SEQUENCE "mt_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 7
 CACHE 1;
SELECT setval('"public"."mt_id_seq"', 7, true);

-- ----------------------------
-- Sequence structure for oc_id_seq
-- ----------------------------
DROP SEQUENCE "oc_id_seq";
CREATE SEQUENCE "oc_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for oor_id_seq
-- ----------------------------
DROP SEQUENCE "oor_id_seq";
CREATE SEQUENCE "oor_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 14418716
 CACHE 1;
SELECT setval('"public"."oor_id_seq"', 14418716, true);

-- ----------------------------
-- Sequence structure for oort_id_seq
-- ----------------------------
DROP SEQUENCE "oort_id_seq";
CREATE SEQUENCE "oort_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ot_leve2_seq
-- ----------------------------
DROP SEQUENCE "ot_leve2_seq";
CREATE SEQUENCE "ot_leve2_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 383963
 CACHE 1;
SELECT setval('"public"."ot_leve2_seq"', 383963, true);

-- ----------------------------
-- Sequence structure for ot_leve3_seq
-- ----------------------------
DROP SEQUENCE "ot_leve3_seq";
CREATE SEQUENCE "ot_leve3_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ot_level2_match_id_seq
-- ----------------------------
DROP SEQUENCE "ot_level2_match_id_seq";
CREATE SEQUENCE "ot_level2_match_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 892563
 CACHE 1;
SELECT setval('"public"."ot_level2_match_id_seq"', 892563, true);

-- ----------------------------
-- Sequence structure for otn_id_seq
-- ----------------------------
DROP SEQUENCE "otn_id_seq";
CREATE SEQUENCE "otn_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 225
 CACHE 1;
SELECT setval('"public"."otn_id_seq"', 225, true);

-- ----------------------------
-- Sequence structure for ott_id_seq
-- ----------------------------
DROP SEQUENCE "ott_id_seq";
CREATE SEQUENCE "ott_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 18
 CACHE 1;
SELECT setval('"public"."ott_id_seq"', 18, true);

-- ----------------------------
-- Sequence structure for ps_id_seq
-- ----------------------------
DROP SEQUENCE "ps_id_seq";
CREATE SEQUENCE "ps_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 25
 CACHE 1;
SELECT setval('"public"."ps_id_seq"', 25, true);

-- ----------------------------
-- Sequence structure for sf_id_seq
-- ----------------------------
DROP SEQUENCE "sf_id_seq";
CREATE SEQUENCE "sf_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 6196
 CACHE 1;
SELECT setval('"public"."sf_id_seq"', 6196, true);

-- ----------------------------
-- Sequence structure for skor_id_seq
-- ----------------------------
DROP SEQUENCE "skor_id_seq";
CREATE SEQUENCE "skor_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for sky_id_seq
-- ----------------------------
DROP SEQUENCE "sky_id_seq";
CREATE SEQUENCE "sky_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 37
 CACHE 1;
SELECT setval('"public"."sky_id_seq"', 37, true);

-- ----------------------------
-- Sequence structure for slf_id_seq
-- ----------------------------
DROP SEQUENCE "slf_id_seq";
CREATE SEQUENCE "slf_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ss_id_seq
-- ----------------------------
DROP SEQUENCE "ss_id_seq";
CREATE SEQUENCE "ss_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for star_id_seq
-- ----------------------------
DROP SEQUENCE "star_id_seq";
CREATE SEQUENCE "star_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for tsp_id_seq
-- ----------------------------
DROP SEQUENCE "tsp_id_seq";
CREATE SEQUENCE "tsp_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ua_id_seq
-- ----------------------------
DROP SEQUENCE "ua_id_seq";
CREATE SEQUENCE "ua_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ual_id_seq
-- ----------------------------
DROP SEQUENCE "ual_id_seq";
CREATE SEQUENCE "ual_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for ufr_id_seq
-- ----------------------------
DROP SEQUENCE "ufr_id_seq";
CREATE SEQUENCE "ufr_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 15164777
 CACHE 1;
SELECT setval('"public"."ufr_id_seq"', 15164777, true);

-- ----------------------------
-- Sequence structure for ufu_id_seq
-- ----------------------------
DROP SEQUENCE "ufu_id_seq";
CREATE SEQUENCE "ufu_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 7415053
 CACHE 1;
SELECT setval('"public"."ufu_id_seq"', 7415053, true);

-- ----------------------------
-- Sequence structure for ui_id_seq
-- ----------------------------
DROP SEQUENCE "ui_id_seq";
CREATE SEQUENCE "ui_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 3
 CACHE 1;
SELECT setval('"public"."ui_id_seq"', 3, true);

-- ----------------------------
-- Sequence structure for vsr_id_seq
-- ----------------------------
DROP SEQUENCE "vsr_id_seq";
CREATE SEQUENCE "vsr_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for vt_id_seq
-- ----------------------------
DROP SEQUENCE "vt_id_seq";
CREATE SEQUENCE "vt_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Table structure for ccd_pix_filter
-- ----------------------------
DROP TABLE IF EXISTS "ccd_pix_filter";
CREATE TABLE "ccd_pix_filter" (
"cpf_id" int8 DEFAULT nextval('cpf_id_seq'::regclass) NOT NULL,
"min_x" float4,
"max_x" float4,
"min_y" float4,
"max_y" float4,
"comment" varchar(255) COLLATE "default",
"dpm_id" int2,
"ot_type_id" int2 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for config_file
-- ----------------------------
DROP TABLE IF EXISTS "config_file";
CREATE TABLE "config_file" (
"cf_id" int8 DEFAULT nextval('cf_id_seq'::regclass) NOT NULL,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"is_sync" bool,
"is_store" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for config_file_his
-- ----------------------------
DROP TABLE IF EXISTS "config_file_his";
CREATE TABLE "config_file_his" (
"cf_id" int8 DEFAULT nextval('cf_id_seq'::regclass) NOT NULL,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"is_sync" bool,
"is_store" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for data_process_machine
-- ----------------------------
DROP TABLE IF EXISTS "data_process_machine";
CREATE TABLE "data_process_machine" (
"dpm_id" int2 DEFAULT nextval('dpm_id_seq'::regclass) NOT NULL,
"name" varchar(255) COLLATE "default",
"ip" char(15) COLLATE "default",
"tsp_id" int2,
"cur_process_number" int4 DEFAULT 0,
"total_storage_size" float4 DEFAULT 0,
"used_storage_size" float4 DEFAULT 0,
"monitor_image_time" timestamp(6),
"last_active_time" timestamp(6),
"cur_sky_id" int2,
"first_record_number" int4 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for environment
-- ----------------------------
DROP TABLE IF EXISTS "environment";
CREATE TABLE "environment" (
"env_id" int8 DEFAULT nextval('env_id_seq'::regclass) NOT NULL,
"date" date,
"time" time(6),
"wather" varchar(64) COLLATE "default",
"wind_speed" float4,
"temperature" float4,
"humidity" float4,
"seeing" float4,
"cloud_percent" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for fits_file
-- ----------------------------
DROP TABLE IF EXISTS "fits_file";
CREATE TABLE "fits_file" (
"ff_id" int8 DEFAULT nextval('ff_id_seq'::regclass) NOT NULL,
"dpm_id" int2,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"gmb_id" int2,
"gmb_ra" float4,
"gmb_dec" float4,
"tsp_id" int2,
"tsp_ra" float4,
"tsp_dec" float4,
"field_width" float4,
"field_height" float4,
"pixel_resolution" float4,
"template_ff_id" int8,
"slf_id" int8,
"is_template" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for fits_file_cut
-- ----------------------------
DROP TABLE IF EXISTS "fits_file_cut";
CREATE TABLE "fits_file_cut" (
"ffc_id" int8 DEFAULT nextval('ffc_id_seq'::regclass) NOT NULL,
"star_id" int8,
"dpm_id" int2,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"ff_id" int8,
"template_ffc_id" int8,
"is_template_cut" bool,
"ot_id" int8 DEFAULT 0,
"number" int4,
"request_cut" bool,
"success_cut" bool,
"img_x" float4,
"img_y" float4,
"is_missed" bool DEFAULT true,
"priority" int2 DEFAULT 32767
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for fits_file_cut_his
-- ----------------------------
DROP TABLE IF EXISTS "fits_file_cut_his";
CREATE TABLE "fits_file_cut_his" (
"ffc_id" int8 NOT NULL,
"star_id" int8,
"dpm_id" int2,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"ff_id" int8,
"template_ffc_id" int8,
"is_template_cut" bool,
"ot_id" int8 DEFAULT 0,
"number" int4,
"request_cut" bool,
"success_cut" bool,
"img_x" float4,
"img_y" float4,
"is_missed" bool DEFAULT true,
"priority" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for fits_file_cut_ref
-- ----------------------------
DROP TABLE IF EXISTS "fits_file_cut_ref";
CREATE TABLE "fits_file_cut_ref" (
"ffcr_id" int8 DEFAULT nextval('ffcr_id_seq'::regclass) NOT NULL,
"ot_id" int8,
"file_name" varchar(255) COLLATE "default",
"store_path" varchar(255) COLLATE "default",
"generate_time" timestamp(6),
"magnitude" float4 DEFAULT 0,
"request_cut" bool DEFAULT false,
"success_cut" bool DEFAULT false,
"dpm_id" int8,
"ff_id" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_fitsfile
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_fitsfile";
CREATE TABLE "follow_up_fitsfile" (
"fuf_id" int8 DEFAULT nextval('fuf_id_seq'::regclass) NOT NULL,
"ff_name" varchar(255) COLLATE "default",
"ff_path" varchar(255) COLLATE "default",
"fo_id" int4,
"is_upload" bool DEFAULT false
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_object
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_object";
CREATE TABLE "follow_up_object" (
"fuo_id" int8 DEFAULT nextval('fuo_id_seq'::regclass) NOT NULL,
"fuo_name" varchar(16) COLLATE "default",
"fuo_type_id" int2,
"start_time_utc" timestamp(6),
"last_ra" float4,
"last_dec" float4,
"last_x" float4,
"last_y" float4,
"found_serial_number" int4 DEFAULT 0,
"fo_id" int8,
"ot_id" int8,
"record_total" int4 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_object_his
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_object_his";
CREATE TABLE "follow_up_object_his" (
"fuo_id" int8 DEFAULT nextval('fuo_id_seq'::regclass) NOT NULL,
"fuo_name" varchar(16) COLLATE "default",
"fuo_type_id" int2,
"start_time_utc" timestamp(6),
"last_ra" float4,
"last_dec" float4,
"last_x" float4,
"last_y" float4,
"found_serial_number" int4 DEFAULT 0,
"fo_id" int8,
"ot_id" int8,
"record_total" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_object_type
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_object_type";
CREATE TABLE "follow_up_object_type" (
"fuo_type_id" int2 DEFAULT nextval('fuo_type_id_seq'::regclass) NOT NULL,
"fuo_type_name" varchar(16) COLLATE "default",
"fuo_type_comment" varchar COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_observation
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_observation";
CREATE TABLE "follow_up_observation" (
"fo_id" int8 DEFAULT nextval('fo_id_seq'::regclass) NOT NULL,
"fo_name" char(20) COLLATE "default",
"ot_id" int8,
"user_id" int4,
"ra" float4,
"dec" float4,
"epoch" varchar(10) COLLATE "default",
"image_type" varchar(10) COLLATE "default",
"expose_duration" int2,
"frame_count" int2,
"filter" varchar(10) COLLATE "default",
"priority" int2,
"trigger_time" timestamp(6),
"back_image_count" int4 DEFAULT 0,
"fo_parm_file" varchar(255) COLLATE "default",
"fo_obj_count" int2 DEFAULT 0,
"trigger_type" varchar(10) COLLATE "default",
"telescope_id" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_record
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_record";
CREATE TABLE "follow_up_record" (
"fr_id" int8 DEFAULT nextval('fr_id_seq'::regclass) NOT NULL,
"fo_id" int8,
"fr_obj_id" int2,
"date_utc" timestamp(6),
"ra" float4,
"dec" float4,
"x" float4,
"y" float4,
"mag_cal_usno" float4,
"mag_err" float4,
"ellipticity" float4,
"class_star" float4,
"fwhm" float4,
"flag" int2,
"b2" float4,
"r2" float4,
"i" float4,
"fuf_id" int8,
"filter" varchar(10) COLLATE "default",
"fu_serial_number" int4,
"fuo_type_id" int2,
"fuo_id" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for follow_up_record_his
-- ----------------------------
DROP TABLE IF EXISTS "follow_up_record_his";
CREATE TABLE "follow_up_record_his" (
"fr_id" int8 DEFAULT nextval('fr_id_seq'::regclass) NOT NULL,
"fo_id" int8,
"fr_obj_id" int2,
"date_utc" timestamp(6),
"ra" float4,
"dec" float4,
"x" float4,
"y" float4,
"mag_cal_usno" float4,
"mag_err" float4,
"ellipticity" float4,
"class_star" float4,
"fwhm" float4,
"flag" int2,
"b2" float4,
"r2" float4,
"i" float4,
"fuf_id" int8,
"filter" varchar(10) COLLATE "default",
"fu_serial_number" int4,
"fuo_type_id" int2,
"fuo_id" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for gimbal
-- ----------------------------
DROP TABLE IF EXISTS "gimbal";
CREATE TABLE "gimbal" (
"gmb_id" int2 DEFAULT nextval('gmb_id_seq'::regclass) NOT NULL,
"name" varchar(255) COLLATE "default",
"ra" float4,
"dec" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for grb
-- ----------------------------
DROP TABLE IF EXISTS "grb";
CREATE TABLE "grb" (
"grb_id" int8 DEFAULT nextval('grb_id_seq'::regclass) NOT NULL,
"grb_name" varchar(255) COLLATE "default",
"trigger_time" timestamp(6),
"trigger_type" varchar(255) COLLATE "default",
"trigger_name" varchar(255) COLLATE "default",
"trigger_ra" float4,
"trigger_dec" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for image_status_parameter
-- ----------------------------
DROP TABLE IF EXISTS "image_status_parameter";
CREATE TABLE "image_status_parameter" (
"isp_id" int8 DEFAULT nextval('isp_id_seq'::regclass) NOT NULL,
"time_obs_ut" timestamp(6),
"obj_num" int4,
"bg_bright" float4,
"fwhm" float4,
"s2n" float4,
"avg_limit" float4,
"extinc" float4,
"xshift" float4,
"yshift" float4,
"xrms" float4,
"yrms" float4,
"ot1_num" int4,
"var1_num" int4,
"ff_id" int8,
"mount_ra" float4,
"mount_dec" float4,
"proc_stage_id" int2,
"proc_time" float4,
"avg_ellipticity" float4,
"temperature_set" float4,
"temperature_actual" float4,
"dpm_id" int4,
"prc_num" int4,
"exposure_time" float4,
"img_center_ra" float4,
"img_center_dec" float4,
"proc_end_time" timestamp(6),
"send_success" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for image_status_parameter_his
-- ----------------------------
DROP TABLE IF EXISTS "image_status_parameter_his";
CREATE TABLE "image_status_parameter_his" (
"isp_id" int8 NOT NULL,
"time_obs_ut" timestamp(6),
"obj_num" int4,
"bg_bright" float4,
"fwhm" float4,
"s2n" float4,
"avg_limit" float4,
"extinc" float4,
"xshift" float4,
"yshift" float4,
"xrms" float4,
"yrms" float4,
"ot1_num" int4,
"var1_num" int4,
"ff_id" int8,
"mount_ra" float4,
"mount_dec" float4,
"proc_stage_id" int2,
"proc_time" float4,
"avg_ellipticity" float4,
"temperature_set" float4,
"temperature_actual" float4,
"dpm_id" int4,
"prc_num" int4,
"exposure_time" float4,
"img_center_ra" float4,
"img_center_dec" float4,
"proc_end_time" timestamp(6),
"send_success" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for match_table
-- ----------------------------
DROP TABLE IF EXISTS "match_table";
CREATE TABLE "match_table" (
"mt_id" int2 DEFAULT nextval('mt_id_seq'::regclass) NOT NULL,
"match_type_name" varchar(255) COLLATE "default",
"match_table_name" varchar(64) COLLATE "default",
"comments" varchar(1024) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for multimedia_resource
-- ----------------------------
DROP TABLE IF EXISTS "multimedia_resource";
CREATE TABLE "multimedia_resource" (
"mr_id" int4 DEFAULT nextval('mr_id_seq'::regclass) NOT NULL,
"en_name" varchar(255) COLLATE "default",
"ch_name" varchar(255) COLLATE "default",
"path" varchar(255) COLLATE "default",
"type" char(1) COLLATE "default",
"comment" varchar(1024) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for observation_sky
-- ----------------------------
DROP TABLE IF EXISTS "observation_sky";
CREATE TABLE "observation_sky" (
"sky_id" int2 DEFAULT nextval('sky_id_seq'::regclass) NOT NULL,
"sky_name" char(6) COLLATE "default",
"ra_max_s" float4,
"ra_min_s" float4,
"dec_max_s" float4,
"dec_min_s" float4,
"ra_max_n" float4,
"ra_min_n" float4,
"dec_max_n" float4,
"dec_min_n" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_comment
-- ----------------------------
DROP TABLE IF EXISTS "ot_comment";
CREATE TABLE "ot_comment" (
"oc_id" int8 DEFAULT nextval('oc_id_seq'::regclass) NOT NULL,
"ot_id" int8,
"ui_id" int4,
"comments" varchar(1024) COLLATE "default",
"parent_id" int8,
"gen_time" timestamp(6),
"agree_number" int4,
"disagree_number" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_level2
-- ----------------------------
DROP TABLE IF EXISTS "ot_level2";
CREATE TABLE "ot_level2" (
"name" char(14) COLLATE "default",
"ra" float4,
"dec" float4,
"found_time_utc" timestamp(6),
"ot_id" int8 DEFAULT nextval('ot_leve2_seq'::regclass) NOT NULL,
"identify" char(21) COLLATE "default",
"xtemp" float4,
"ytemp" float4,
"last_ff_number" int4,
"total" int4 DEFAULT 0,
"is_recognize" bool DEFAULT false,
"ot_type" int2 DEFAULT 0,
"comments" varchar(1024) COLLATE "default",
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"all_file_cutted" bool DEFAULT false,
"first_ff_number" int4 DEFAULT 0,
"cutted_ff_number" int4 DEFAULT 0,
"is_match" int2 DEFAULT 0,
"first_n_mark" bool DEFAULT false,
"sky_id" int2,
"data_produce_method" char(1) COLLATE "default",
"fo_count" int2 DEFAULT 0,
"mag" float4,
"cvs_match" int2 DEFAULT 0,
"rc3_match" int2 DEFAULT 0,
"minor_planet_match" int2 DEFAULT 0,
"ot2_his_match" int2 DEFAULT 0,
"other_match" int2 DEFAULT 0,
"usno_match" int2 DEFAULT 0,
"look_back_result" int2 DEFAULT 0,
"follow_up_result" int2 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_level2_his
-- ----------------------------
DROP TABLE IF EXISTS "ot_level2_his";
CREATE TABLE "ot_level2_his" (
"name" char(14) COLLATE "default",
"ra" float4,
"dec" float4,
"found_time_utc" timestamp(6),
"ot_id" int8 NOT NULL,
"identify" char(21) COLLATE "default",
"xtemp" float4,
"ytemp" float4,
"last_ff_number" int4,
"total" int4 DEFAULT 0,
"is_recognize" bool DEFAULT false,
"ot_type" int2 DEFAULT 1,
"comments" varchar(1024) COLLATE "default",
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"all_file_cutted" bool DEFAULT false,
"first_ff_number" int4 DEFAULT 0,
"cutted_ff_number" int4 DEFAULT 0,
"is_match" int2 DEFAULT 0,
"first_n_mark" bool,
"sky_id" int2,
"data_produce_method" char(1) COLLATE "default",
"fo_count" int2 DEFAULT 0,
"mag" float4,
"cvs_match" int2 DEFAULT 0,
"rc3_match" int2 DEFAULT 0,
"minor_planet_match" int2 DEFAULT 0,
"ot2_his_match" int2 DEFAULT 0,
"other_match" int2 DEFAULT 0,
"usno_match" int2 DEFAULT 0,
"look_back_result" int2,
"follow_up_result" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_level2_match
-- ----------------------------
DROP TABLE IF EXISTS "ot_level2_match";
CREATE TABLE "ot_level2_match" (
"olm_id" int8 DEFAULT nextval('ot_level2_match_id_seq'::regclass) NOT NULL,
"ot_id" int8,
"mt_id" int2,
"match_id" int8,
"comments" varchar(1024) COLLATE "default",
"ra" float4,
"dec" float4,
"mag" float4 DEFAULT 0,
"distance" float4 DEFAULT 0,
"d25" float4 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_level3
-- ----------------------------
DROP TABLE IF EXISTS "ot_level3";
CREATE TABLE "ot_level3" (
"name" char(12) COLLATE "default",
"ra" float4,
"dec" float4,
"found_time_utc" timestamp(6),
"ot_id" int8 DEFAULT nextval('ot_leve3_seq'::regclass) NOT NULL,
"identify" char(21) COLLATE "default",
"xtemp" float4,
"ytemp" float4,
"last_ff_number" int4,
"total" int4 DEFAULT 0,
"succ_occur_times" int2 DEFAULT 0,
"max_succ_occur_times" int2 DEFAULT 0,
"is_recognize" bool DEFAULT false,
"ot_type" int2 DEFAULT 1,
"comments" varchar(1024) COLLATE "default",
"dpm_name" char(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_number
-- ----------------------------
DROP TABLE IF EXISTS "ot_number";
CREATE TABLE "ot_number" (
"otn_id" int8 DEFAULT nextval('otn_id_seq'::regclass) NOT NULL,
"date" varchar(32) COLLATE "default",
"number" int4,
"ot_level" char(1) COLLATE "default" DEFAULT 1,
"var_number" int4,
"sub_number" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_observe_record
-- ----------------------------
DROP TABLE IF EXISTS "ot_observe_record";
CREATE TABLE "ot_observe_record" (
"ot_id" int8 DEFAULT 0,
"ff_id" int8 DEFAULT 0,
"ffc_id" int8 DEFAULT 0,
"oor_id" int8 DEFAULT nextval('oor_id_seq'::regclass) NOT NULL,
"ot_type_id" int2,
"ra_d" float4,
"dec_d" float4,
"x" float4,
"y" float4,
"x_temp" float4,
"y_temp" float4,
"date_ut" timestamp(6),
"flux" float4,
"flag" bool,
"flag_chb" float4,
"background" float4,
"threshold" float4,
"mag_aper" float4,
"magerr_aper" float4,
"ellipticity" float4,
"class_star" float4,
"ot_flag" bool,
"ff_number" int4,
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"request_cut" bool DEFAULT false,
"success_cut" bool DEFAULT false,
"sky_id" int2,
"distance" float4,
"deltamag" float4,
"data_produce_method" char(1) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_observe_record_his
-- ----------------------------
DROP TABLE IF EXISTS "ot_observe_record_his";
CREATE TABLE "ot_observe_record_his" (
"ot_id" int8 DEFAULT 0,
"ff_id" int8 DEFAULT 0,
"ffc_id" int8 DEFAULT 0,
"oor_id" int8 NOT NULL,
"ot_type_id" int2,
"ra_d" float4,
"dec_d" float4,
"x" float4,
"y" float4,
"x_temp" float4,
"y_temp" float4,
"date_ut" timestamp(6),
"flux" float4,
"flag" bool,
"flag_chb" float4,
"background" float4,
"threshold" float4,
"mag_aper" float4,
"magerr_aper" float4,
"ellipticity" float4,
"class_star" float4,
"ot_flag" bool,
"ff_number" int4,
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"request_cut" bool DEFAULT false,
"success_cut" bool DEFAULT false,
"sky_id" int2,
"distance" float4,
"deltamag" float4,
"data_produce_method" char(1) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_observe_record_tmp
-- ----------------------------
DROP TABLE IF EXISTS "ot_observe_record_tmp";
CREATE TABLE "ot_observe_record_tmp" (
"ot_id" int8,
"ff_id" int8,
"ra" float4,
"dec" float4,
"x" float4,
"y" float4,
"datetime" timestamp(6),
"mag" float4,
"mag_err" float4,
"ellipticity" float4,
"signal_noise" float4,
"fwhm" float4,
"flux" float4,
"flux_err" float4,
"cmp_star_id" int8,
"mch_star_id" int8,
"ffc_id" int8,
"oor_id" int8 DEFAULT nextval('oort_id_seq'::regclass) NOT NULL,
"ot_type_id" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_tmpl_true
-- ----------------------------
DROP TABLE IF EXISTS "ot_tmpl_true";
CREATE TABLE "ot_tmpl_true" (
"ot_id" int8 NOT NULL,
"name" char(14) COLLATE "default",
"ra" float4,
"dec" float4,
"mag" float4,
"healpix_id" int8 DEFAULT 0,
"data_produce_method" char(1) COLLATE "default",
"first_found_time_utc" timestamp(6),
"last_found_time_utc" timestamp(6),
"matched_total" int4 DEFAULT 0,
"is_valid" bool DEFAULT true,
"ott_id" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_tmpl_wrong
-- ----------------------------
DROP TABLE IF EXISTS "ot_tmpl_wrong";
CREATE TABLE "ot_tmpl_wrong" (
"ot_id" int8 NOT NULL,
"name" char(14) COLLATE "default",
"ra" float4,
"dec" float4,
"mag" float4,
"healpix_id" int8 DEFAULT 0,
"data_produce_method" char(1) COLLATE "default",
"first_found_time_utc" timestamp(6),
"last_found_time_utc" timestamp(6),
"matched_total" int4 DEFAULT 0,
"is_valid" bool DEFAULT true,
"ott_id" int2,
"ot_class" char(1) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ot_type
-- ----------------------------
DROP TABLE IF EXISTS "ot_type";
CREATE TABLE "ot_type" (
"ott_id" int2 DEFAULT nextval('ott_id_seq'::regclass) NOT NULL,
"ott_name" varchar(64) COLLATE "default",
"comment" varchar(1024) COLLATE "default",
"priority" int2,
"ot_class" char(1) COLLATE "default" DEFAULT 0
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "ot_type" IS 'ot_class的取值有5种：
0，未分类
1，真OT
2，动OT
3，错OT
4，假OT';

-- ----------------------------
-- Table structure for process_status
-- ----------------------------
DROP TABLE IF EXISTS "process_status";
CREATE TABLE "process_status" (
"ps_id" int2 DEFAULT nextval('ps_id_seq'::regclass) NOT NULL,
"ps_name" char(32) COLLATE "default",
"ps_comment" varchar(1024) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for star_know
-- ----------------------------
DROP TABLE IF EXISTS "star_know";
CREATE TABLE "star_know" (
"star_id" int8 DEFAULT nextval('star_id_seq'::regclass) NOT NULL,
"star_name" varchar COLLATE "default",
"ra" float4,
"dec" float4,
"mag" float4,
"vt_id" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for star_know_observe_record
-- ----------------------------
DROP TABLE IF EXISTS "star_know_observe_record";
CREATE TABLE "star_know_observe_record" (
"star_id" int8,
"ff_id" int8,
"ra" float4,
"dec" float4,
"x" float4,
"y" float4,
"datetime" timestamp(6),
"mag" float4,
"mag_err" float4,
"ellipticity" float4,
"signal_noise" float4,
"fwhm" float4,
"flux" float4,
"flux_err" float4,
"cmp_star_id" int8,
"mch_star_id" int8,
"skor_id" int8 DEFAULT nextval('skor_id_seq'::regclass) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for star_list_file
-- ----------------------------
DROP TABLE IF EXISTS "star_list_file";
CREATE TABLE "star_list_file" (
"slf_id" int8 DEFAULT nextval('slf_id_seq'::regclass) NOT NULL,
"dpm_id" int2,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for sync_file
-- ----------------------------
DROP TABLE IF EXISTS "sync_file";
CREATE TABLE "sync_file" (
"sf_id" int8 DEFAULT nextval('sf_id_seq'::regclass) NOT NULL,
"file_name" varchar(1024) COLLATE "default",
"path" varchar(1024) COLLATE "default",
"store_time" timestamp(6),
"is_sync" bool DEFAULT false,
"is_sync_success" bool DEFAULT false
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for system_status
-- ----------------------------
DROP TABLE IF EXISTS "system_status";
CREATE TABLE "system_status" (
"id" int4 DEFAULT nextval('ss_id_seq'::regclass) NOT NULL,
"date_str" char(6) COLLATE "default",
"total_storage_size" float4,
"used_storage_size" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for telescope
-- ----------------------------
DROP TABLE IF EXISTS "telescope";
CREATE TABLE "telescope" (
"tsp_id" int2 DEFAULT nextval('tsp_id_seq'::regclass) NOT NULL,
"name" varchar(255) COLLATE "default",
"ra" float4,
"dec" float4,
"diameter" int2,
"focal_ratio" float4,
"ccd_type" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for upload_file_record
-- ----------------------------
DROP TABLE IF EXISTS "upload_file_record";
CREATE TABLE "upload_file_record" (
"ufr_id" int8 DEFAULT nextval('ufr_id_seq'::regclass) NOT NULL,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"file_type" char(1) COLLATE "default",
"upload_success" bool,
"upload_date" timestamp(6),
"send_time" timestamp(6),
"process_done_time" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for upload_file_unstore
-- ----------------------------
DROP TABLE IF EXISTS "upload_file_unstore";
CREATE TABLE "upload_file_unstore" (
"ufu_id" int8 DEFAULT nextval('ufu_id_seq'::regclass) NOT NULL,
"store_path" varchar(255) COLLATE "default",
"file_name" varchar(255) COLLATE "default",
"file_type" char(1) COLLATE "default",
"upload_success" bool,
"upload_date" timestamp(6),
"send_time" timestamp(6),
"process_done_time" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for user_action
-- ----------------------------
DROP TABLE IF EXISTS "user_action";
CREATE TABLE "user_action" (
"ua_id" int4 DEFAULT nextval('ua_id_seq'::regclass) NOT NULL,
"ua_name" varchar(24) COLLATE "default",
"ua_comment" varchar(1024) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for user_action_log
-- ----------------------------
DROP TABLE IF EXISTS "user_action_log";
CREATE TABLE "user_action_log" (
"ual_id" int8 DEFAULT nextval('ual_id_seq'::regclass) NOT NULL,
"ui_id" int4,
"ua_id" int2,
"ual_time" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS "user_info";
CREATE TABLE "user_info" (
"ui_id" int4 DEFAULT nextval('ui_id_seq'::regclass) NOT NULL,
"name" varchar(255) COLLATE "default",
"address" varchar(255) COLLATE "default",
"register_date" timestamp(6),
"password" varchar(255) COLLATE "default",
"login_name" varchar(255) COLLATE "default",
"position" varchar(255) COLLATE "default",
"comments" varchar(1024) COLLATE "default",
"register_pass" bool,
"email" varchar(32) COLLATE "default",
"mobil_phone" varchar(32) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for var_star_record
-- ----------------------------
DROP TABLE IF EXISTS "var_star_record";
CREATE TABLE "var_star_record" (
"ff_id" int8 DEFAULT 0,
"ffc_id" int8 DEFAULT 0,
"ot_type_id" int2,
"ra_d" float4,
"dec_d" float4,
"x" float4,
"y" float4,
"x_temp" float4,
"y_temp" float4,
"date_ut" timestamp(6),
"flux" float4,
"flag" bool,
"flag_chb" float4,
"background" float4,
"threshold" float4,
"mag_aper" float4,
"magerr_aper" float4,
"ellipticity" float4,
"class_star" float4,
"ot_flag" bool,
"ff_number" int4,
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"request_cut" bool DEFAULT false,
"success_cut" bool DEFAULT false,
"vs_id" int8 DEFAULT 0,
"vsr_id" int8 DEFAULT nextval('vsr_id_seq'::regclass) NOT NULL,
"distance" float4,
"deltamag" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for var_star_record_his
-- ----------------------------
DROP TABLE IF EXISTS "var_star_record_his";
CREATE TABLE "var_star_record_his" (
"ff_id" int8 DEFAULT 0,
"ffc_id" int8 DEFAULT 0,
"ot_type_id" int2,
"ra_d" float4,
"dec_d" float4,
"x" float4,
"y" float4,
"x_temp" float4,
"y_temp" float4,
"date_ut" timestamp(6),
"flux" float4,
"flag" bool,
"flag_chb" float4,
"background" float4,
"threshold" float4,
"mag_aper" float4,
"magerr_aper" float4,
"ellipticity" float4,
"class_star" float4,
"ot_flag" bool,
"ff_number" int4,
"dpm_id" int4,
"date_str" char(6) COLLATE "default",
"request_cut" bool DEFAULT false,
"success_cut" bool DEFAULT false,
"vs_id" int8 DEFAULT 0,
"vsr_id" int8 DEFAULT nextval('vsr_id_seq'::regclass) NOT NULL,
"distance" float4,
"deltamag" float4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for variation_type
-- ----------------------------
DROP TABLE IF EXISTS "variation_type";
CREATE TABLE "variation_type" (
"vt_id" int8 DEFAULT nextval('vt_id_seq'::regclass) NOT NULL,
"vt_name" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
ALTER SEQUENCE "cf_id_seq" OWNED BY "config_file"."cf_id";
ALTER SEQUENCE "cpf_id_seq" OWNED BY "ccd_pix_filter"."cpf_id";
ALTER SEQUENCE "dpm_id_seq" OWNED BY "data_process_machine"."dpm_id";
ALTER SEQUENCE "env_id_seq" OWNED BY "environment"."env_id";
ALTER SEQUENCE "ff_id_seq" OWNED BY "fits_file"."ff_id";
ALTER SEQUENCE "ffc_id_seq" OWNED BY "fits_file_cut"."ffc_id";
ALTER SEQUENCE "gmb_id_seq" OWNED BY "gimbal"."gmb_id";
ALTER SEQUENCE "grb_id_seq" OWNED BY "grb"."grb_id";
ALTER SEQUENCE "mr_id_seq" OWNED BY "multimedia_resource"."mr_id";
ALTER SEQUENCE "mt_id_seq" OWNED BY "match_table"."mt_id";
ALTER SEQUENCE "skor_id_seq" OWNED BY "star_know_observe_record"."skor_id";
ALTER SEQUENCE "sky_id_seq" OWNED BY "observation_sky"."sky_id";
ALTER SEQUENCE "slf_id_seq" OWNED BY "star_list_file"."slf_id";
ALTER SEQUENCE "star_id_seq" OWNED BY "star_know"."star_id";
ALTER SEQUENCE "tsp_id_seq" OWNED BY "telescope"."tsp_id";
ALTER SEQUENCE "ui_id_seq" OWNED BY "user_info"."ui_id";
ALTER SEQUENCE "vt_id_seq" OWNED BY "variation_type"."vt_id";

-- ----------------------------
-- Primary Key structure for table ccd_pix_filter
-- ----------------------------
ALTER TABLE "ccd_pix_filter" ADD PRIMARY KEY ("cpf_id");

-- ----------------------------
-- Primary Key structure for table config_file
-- ----------------------------
ALTER TABLE "config_file" ADD PRIMARY KEY ("cf_id");

-- ----------------------------
-- Primary Key structure for table config_file_his
-- ----------------------------
ALTER TABLE "config_file_his" ADD PRIMARY KEY ("cf_id");

-- ----------------------------
-- Primary Key structure for table data_process_machine
-- ----------------------------
ALTER TABLE "data_process_machine" ADD PRIMARY KEY ("dpm_id");

-- ----------------------------
-- Primary Key structure for table environment
-- ----------------------------
ALTER TABLE "environment" ADD PRIMARY KEY ("env_id");

-- ----------------------------
-- Indexes structure for table fits_file
-- ----------------------------
CREATE INDEX "fits_file_ff_id_idx" ON "fits_file" USING btree (ff_id);
CREATE INDEX "fits_file_file_name_idx" ON "fits_file" USING btree (file_name);

-- ----------------------------
-- Primary Key structure for table fits_file
-- ----------------------------
ALTER TABLE "fits_file" ADD PRIMARY KEY ("ff_id");

-- ----------------------------
-- Indexes structure for table fits_file_cut
-- ----------------------------
CREATE INDEX "fits_file_cut_number_idx" ON "fits_file_cut" USING btree (number);

-- ----------------------------
-- Primary Key structure for table fits_file_cut
-- ----------------------------
ALTER TABLE "fits_file_cut" ADD PRIMARY KEY ("ffc_id");

-- ----------------------------
-- Primary Key structure for table fits_file_cut_his
-- ----------------------------
ALTER TABLE "fits_file_cut_his" ADD PRIMARY KEY ("ffc_id");

-- ----------------------------
-- Primary Key structure for table fits_file_cut_ref
-- ----------------------------
ALTER TABLE "fits_file_cut_ref" ADD PRIMARY KEY ("ffcr_id");

-- ----------------------------
-- Primary Key structure for table follow_up_fitsfile
-- ----------------------------
ALTER TABLE "follow_up_fitsfile" ADD PRIMARY KEY ("fuf_id");

-- ----------------------------
-- Primary Key structure for table follow_up_object
-- ----------------------------
ALTER TABLE "follow_up_object" ADD PRIMARY KEY ("fuo_id");

-- ----------------------------
-- Primary Key structure for table follow_up_object_his
-- ----------------------------
ALTER TABLE "follow_up_object_his" ADD PRIMARY KEY ("fuo_id");

-- ----------------------------
-- Primary Key structure for table follow_up_object_type
-- ----------------------------
ALTER TABLE "follow_up_object_type" ADD PRIMARY KEY ("fuo_type_id");

-- ----------------------------
-- Primary Key structure for table follow_up_observation
-- ----------------------------
ALTER TABLE "follow_up_observation" ADD PRIMARY KEY ("fo_id");

-- ----------------------------
-- Primary Key structure for table follow_up_record
-- ----------------------------
ALTER TABLE "follow_up_record" ADD PRIMARY KEY ("fr_id");

-- ----------------------------
-- Primary Key structure for table follow_up_record_his
-- ----------------------------
ALTER TABLE "follow_up_record_his" ADD PRIMARY KEY ("fr_id");

-- ----------------------------
-- Primary Key structure for table gimbal
-- ----------------------------
ALTER TABLE "gimbal" ADD PRIMARY KEY ("gmb_id");

-- ----------------------------
-- Primary Key structure for table grb
-- ----------------------------
ALTER TABLE "grb" ADD PRIMARY KEY ("grb_id");

-- ----------------------------
-- Primary Key structure for table image_status_parameter
-- ----------------------------
ALTER TABLE "image_status_parameter" ADD PRIMARY KEY ("isp_id");

-- ----------------------------
-- Primary Key structure for table image_status_parameter_his
-- ----------------------------
ALTER TABLE "image_status_parameter_his" ADD PRIMARY KEY ("isp_id");

-- ----------------------------
-- Primary Key structure for table match_table
-- ----------------------------
ALTER TABLE "match_table" ADD PRIMARY KEY ("mt_id");

-- ----------------------------
-- Primary Key structure for table multimedia_resource
-- ----------------------------
ALTER TABLE "multimedia_resource" ADD PRIMARY KEY ("mr_id");

-- ----------------------------
-- Primary Key structure for table observation_sky
-- ----------------------------
ALTER TABLE "observation_sky" ADD PRIMARY KEY ("sky_id");

-- ----------------------------
-- Primary Key structure for table ot_comment
-- ----------------------------
ALTER TABLE "ot_comment" ADD PRIMARY KEY ("oc_id");

-- ----------------------------
-- Indexes structure for table ot_level2
-- ----------------------------
CREATE INDEX "ot_level2_last_ff_number_idx" ON "ot_level2" USING btree (last_ff_number);

-- ----------------------------
-- Primary Key structure for table ot_level2
-- ----------------------------
ALTER TABLE "ot_level2" ADD PRIMARY KEY ("ot_id");

-- ----------------------------
-- Primary Key structure for table ot_level2_his
-- ----------------------------
ALTER TABLE "ot_level2_his" ADD PRIMARY KEY ("ot_id");

-- ----------------------------
-- Primary Key structure for table ot_level2_match
-- ----------------------------
ALTER TABLE "ot_level2_match" ADD PRIMARY KEY ("olm_id");

-- ----------------------------
-- Primary Key structure for table ot_level3
-- ----------------------------
ALTER TABLE "ot_level3" ADD PRIMARY KEY ("ot_id");

-- ----------------------------
-- Primary Key structure for table ot_number
-- ----------------------------
ALTER TABLE "ot_number" ADD PRIMARY KEY ("otn_id");

-- ----------------------------
-- Indexes structure for table ot_observe_record
-- ----------------------------
CREATE INDEX "ot_observe_record_ff_number_idx" ON "ot_observe_record" USING btree (ff_number);

-- ----------------------------
-- Primary Key structure for table ot_observe_record
-- ----------------------------
ALTER TABLE "ot_observe_record" ADD PRIMARY KEY ("oor_id");

-- ----------------------------
-- Primary Key structure for table ot_observe_record_his
-- ----------------------------
ALTER TABLE "ot_observe_record_his" ADD PRIMARY KEY ("oor_id");

-- ----------------------------
-- Primary Key structure for table ot_observe_record_tmp
-- ----------------------------
ALTER TABLE "ot_observe_record_tmp" ADD PRIMARY KEY ("oor_id");

-- ----------------------------
-- Primary Key structure for table ot_tmpl_true
-- ----------------------------
ALTER TABLE "ot_tmpl_true" ADD PRIMARY KEY ("ot_id");

-- ----------------------------
-- Primary Key structure for table ot_tmpl_wrong
-- ----------------------------
ALTER TABLE "ot_tmpl_wrong" ADD PRIMARY KEY ("ot_id");

-- ----------------------------
-- Primary Key structure for table ot_type
-- ----------------------------
ALTER TABLE "ot_type" ADD PRIMARY KEY ("ott_id");

-- ----------------------------
-- Primary Key structure for table process_status
-- ----------------------------
ALTER TABLE "process_status" ADD PRIMARY KEY ("ps_id");

-- ----------------------------
-- Primary Key structure for table star_know
-- ----------------------------
ALTER TABLE "star_know" ADD PRIMARY KEY ("star_id");

-- ----------------------------
-- Primary Key structure for table star_know_observe_record
-- ----------------------------
ALTER TABLE "star_know_observe_record" ADD PRIMARY KEY ("skor_id");

-- ----------------------------
-- Primary Key structure for table star_list_file
-- ----------------------------
ALTER TABLE "star_list_file" ADD PRIMARY KEY ("slf_id");

-- ----------------------------
-- Primary Key structure for table sync_file
-- ----------------------------
ALTER TABLE "sync_file" ADD PRIMARY KEY ("sf_id");

-- ----------------------------
-- Primary Key structure for table system_status
-- ----------------------------
ALTER TABLE "system_status" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table telescope
-- ----------------------------
ALTER TABLE "telescope" ADD PRIMARY KEY ("tsp_id");

-- ----------------------------
-- Primary Key structure for table upload_file_record
-- ----------------------------
ALTER TABLE "upload_file_record" ADD PRIMARY KEY ("ufr_id");

-- ----------------------------
-- Uniques structure for table upload_file_unstore
-- ----------------------------
ALTER TABLE "upload_file_unstore" ADD UNIQUE ("file_name");

-- ----------------------------
-- Primary Key structure for table upload_file_unstore
-- ----------------------------
ALTER TABLE "upload_file_unstore" ADD PRIMARY KEY ("ufu_id");

-- ----------------------------
-- Primary Key structure for table user_action
-- ----------------------------
ALTER TABLE "user_action" ADD PRIMARY KEY ("ua_id");

-- ----------------------------
-- Primary Key structure for table user_action_log
-- ----------------------------
ALTER TABLE "user_action_log" ADD PRIMARY KEY ("ual_id");

-- ----------------------------
-- Primary Key structure for table user_info
-- ----------------------------
ALTER TABLE "user_info" ADD PRIMARY KEY ("ui_id");

-- ----------------------------
-- Indexes structure for table var_star_record
-- ----------------------------
CREATE INDEX "var_star_record_ff_number_idx" ON "var_star_record" USING btree (ff_number);

-- ----------------------------
-- Primary Key structure for table var_star_record
-- ----------------------------
ALTER TABLE "var_star_record" ADD PRIMARY KEY ("vsr_id");

-- ----------------------------
-- Primary Key structure for table var_star_record_his
-- ----------------------------
ALTER TABLE "var_star_record_his" ADD PRIMARY KEY ("vsr_id");

-- ----------------------------
-- Primary Key structure for table variation_type
-- ----------------------------
ALTER TABLE "variation_type" ADD PRIMARY KEY ("vt_id");
