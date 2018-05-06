/*==============================================================*/
/* wms1.4.0数据库修改                                           */
/*==============================================================*/

USE wms;
/*==============================================================*/
/* 新建物流运费城市表                                           */
/*==============================================================*/
CREATE TABLE t_logistics_freight_city
(
   id                   INT                            NOT NULL AUTO_INCREMENT,
   logistics_id         INT                            NULL COMMENT '承运商id',
   template_id          INT                            NULL COMMENT '运费模板id',
   prov_code            VARCHAR(20)                    NULL COMMENT '省份编码',
   prov_name            VARCHAR(20)                    NULL COMMENT '省份名称',
   city_code            VARCHAR(20)                    NULL COMMENT '城市编码',
   city_name            VARCHAR(20)                    NULL COMMENT '城市名称',
   ware_id              INT                            NULL COMMENT '仓库id',
   ware_name            VARCHAR(20)                    NULL COMMENT '仓库名称',
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* 新建物流运费模板表                                           */
/*==============================================================*/
CREATE TABLE t_logistics_freight_template
(
   id                   INT                            NOT NULL AUTO_INCREMENT,
   logistics_id         INT                            NULL COMMENT '承运商id',
   logistics_name       VARCHAR(100)                   NULL COMMENT '承运商名称',
   price_type           INT(2)                         NULL COMMENT '计价方式 1:按重量',
   NAME                 VARCHAR(2000)                  NULL COMMENT '模板名称',
   code                 VARCHAR(2000)                  NULL COMMENT '模板名称对应code',
   freight_type         INT(1)                         NULL COMMENT '是否默认运费 0:否 1:是',
   first_weight         DECIMAL(10,2)                  NULL COMMENT '首重量',
   first_price          DECIMAL(10,2)                  NULL COMMENT '首费',
   second_weight        DECIMAL(10,2)                  NULL COMMENT '续重量',
   second_price         DECIMAL(10,2)                  NULL COMMENT '续费',
   delivery_price       DECIMAL(10,2)                  NULL COMMENT '派送费',
   create_time          DATETIME                       NULL COMMENT '创建时间',
   update_time          DATETIME                       NULL COMMENT '修改时间',
   create_user_id       INT                            NULL COMMENT '创建人id',
   create_user_name     VARCHAR(20)                    NULL COMMENT '创建人姓名',
   update_user_id       INT                            NULL COMMENT '修改人id',
   update_user_name     VARCHAR(20)                    NULL COMMENT '修改人姓名',
   ware_id              INT                            NULL COMMENT '仓库id',
   ware_name            VARCHAR(20)                    NULL COMMENT '仓库名称',
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* 新建物流运费单表                                             */
/*==============================================================*/
drop table if exists t_logistics_freight_bill;
CREATE TABLE `t_logistics_freight_bill` (
   `id`  int(11) NOT NULL AUTO_INCREMENT ,
   `logistics_no`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运单号' ,
   `order_no`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出库单号' ,
   `logis_com_id`  int(11) NULL DEFAULT NULL COMMENT '物流公司id' ,
   `logis_com_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流公司名称' ,
   `code`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流公司code' ,
   `receive_address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址（全部）' ,
   `base_price`  decimal(12,2) NULL DEFAULT NULL COMMENT '基础价格' ,
   `extra_charges`  decimal(12,2) NULL DEFAULT NULL COMMENT '额外价格' ,
   `do_change`  int(11) NULL DEFAULT NULL COMMENT '是否修改价格' ,
   `remark`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
   `update_user_id`  int(11) NULL DEFAULT NULL COMMENT '修改人id' ,
   `update_user_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人名称' ,
   `update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
   `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
   `ware_id`  int(11) NULL DEFAULT NULL COMMENT '仓库id' ,
   `ware_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
   PRIMARY KEY (`id`),
   UNIQUE INDEX `logistics_no_index` (`logistics_no`) USING BTREE ,
   UNIQUE INDEX `order_no_index` (`order_no`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='物流运费单'
AUTO_INCREMENT=1
ROW_FORMAT=DYNAMIC;

/*==============================================================*/
/* 出库单表新加字段：拣货人名称，复检人员名称                   */
/*==============================================================*/
ALTER TABLE wms.t_stock_out_bill
ADD picking_user_name VARCHAR(30) DEFAULT NULL COMMENT '拣货人名称',
ADD recheck_user_name VARCHAR(30) DEFAULT NULL COMMENT '复检人员名称';

/*==============================================================*/
/* wms配送范围添加字段：省份名称，市级名称                      */
/*==============================================================*/
ALTER TABLE wms.t_delivery_range
ADD prov_name VARCHAR(20) DEFAULT NULL COMMENT '省份名称',
ADD city_name VARCHAR(20) DEFAULT NULL COMMENT '市级名称';

/*==============================================================*/
/* 物流公司表添加字段：配送范围名称                             */
/*==============================================================*/
ALTER TABLE wms.t_logistics_company
ADD delivery_name VARCHAR(2000) DEFAULT NULL COMMENT '配送范围名称',
ADD delivery_code VARCHAR(2000) DEFAULT NULL COMMENT '配送范围编码';

/*==============================================================*/
/* 发货单表添加字段：发货员名称                                 */
/*==============================================================*/
ALTER TABLE wms.t_delivery_bill
ADD delivery_user_name VARCHAR(30) DEFAULT NULL COMMENT '发货员名称';

/*==============================================================*/
/* 索引优化                                                     */
/*==============================================================*/
SET FOREIGN_KEY_CHECKS=0;
CREATE UNIQUE INDEX `location_unique_index` ON `t_location`(`ware_id`, `location_no`) USING BTREE ;
CREATE UNIQUE INDEX `bind_unique_index` ON `t_location_goods_bind`(`location_no`, `goods_code`, `ware_id`, `area_id`, `sale_type`) USING BTREE ;
ALTER TABLE `t_location_goods_bind` ROW_FORMAT=Compact;
ALTER TABLE `t_location_goods_bind_in` ROW_FORMAT=Compact;
ALTER TABLE `t_location_goods_bind_out` ROW_FORMAT=Compact;
ALTER TABLE `t_message_queue` MODIFY COLUMN `state`  int(1) NOT NULL COMMENT '数据处理状态 0待处理 1已处理 2处理出错9手动处理' AFTER `remark`;
CREATE UNIQUE INDEX `index_order_no` ON `t_order_logistics_info`(`order_no`) USING BTREE ;
CREATE UNIQUE INDEX `stock_unique_index` ON `t_stock`(`goods_code`, `ware_id`, `sale_type`) USING BTREE ;
SET FOREIGN_KEY_CHECKS=1;
