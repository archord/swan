/*
Navicat PGSQL Data Transfer

Source Server         : vm-postgres-u14.04
Source Server Version : 90315
Source Host           : 192.168.56.101:5432
Source Database       : meteor-classify
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90315
File Encoding         : 65001

Date: 2018-04-13 18:41:39
*/


-- ----------------------------
-- Sequence structure for ds_id_seq
-- ----------------------------
DROP SEQUENCE "ds_id_seq";
CREATE SEQUENCE "ds_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
SELECT setval('"public"."ds_id_seq"', 1, true);

-- ----------------------------
-- Sequence structure for img_id_seq
-- ----------------------------
DROP SEQUENCE "img_id_seq";
CREATE SEQUENCE "img_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
SELECT setval('"public"."img_id_seq"', 1, true);

-- ----------------------------
-- Table structure for data_set
-- ----------------------------
DROP TABLE IF EXISTS "data_set";
CREATE TABLE "data_set" (
"ds_id" int4 DEFAULT nextval('ds_id_seq'::regclass) NOT NULL,
"ds_date" varchar(6) COLLATE "default",
"ds_camera" varchar COLLATE "default",
"ds_dir_name" varchar COLLATE "default",
"ds_sky" varchar COLLATE "default",
"ds_path" varchar COLLATE "default",
"ds_img_num" int4,
"dv_id" int4,
"ds_default_type" int4 DEFAULT 0
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of data_set
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for data_version
-- ----------------------------
DROP TABLE IF EXISTS "data_version";
CREATE TABLE "data_version" (
"dv_id" int4 NOT NULL,
"dv_name" varchar(255) COLLATE "default",
"dv_path" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of data_version
-- ----------------------------
BEGIN;
INSERT INTO "data_version" VALUES ('1', 'version1', '/data/gwac_data/meteorXY/result');
INSERT INTO "data_version" VALUES ('2', 'version2', '/data/gwac_data/meteorXY/result2');
INSERT INTO "data_version" VALUES ('3', 'version3', '/data/gwac_data/meteorXY/result3');
COMMIT;

-- ----------------------------
-- Table structure for image_record
-- ----------------------------
DROP TABLE IF EXISTS "image_record";
CREATE TABLE "image_record" (
"img_id" int8 DEFAULT nextval('img_id_seq'::regclass) NOT NULL,
"img_name" varchar(255) COLLATE "default",
"img_type" int4 DEFAULT (-1),
"ds_id" int4,
"img_sky" char(6) COLLATE "default",
"img_type2" varchar COLLATE "default" DEFAULT (-1),
"img_type3" varchar COLLATE "default" DEFAULT (-1)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of image_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table data_set
-- ----------------------------
ALTER TABLE "data_set" ADD PRIMARY KEY ("ds_id");

-- ----------------------------
-- Primary Key structure for table data_version
-- ----------------------------
ALTER TABLE "data_version" ADD PRIMARY KEY ("dv_id");

-- ----------------------------
-- Primary Key structure for table image_record
-- ----------------------------
ALTER TABLE "image_record" ADD PRIMARY KEY ("img_id");
