package com.rmd.wms.constant;

import java.util.Date;

/**
 * 系统常量定义
 */
public class Constant {

    /**
     * 采购单状态
     * 1:等待,2:部分,3:完成
     */
    public static class PurchaseBillStatus {
        public static final int WAITTING = 1;
        public static final int PART = 2;
        public static final int FINISH = 3;
    }

    /**
     * 入库单类型
     * 1：采购单入库，2：售后服务单入库，3： 取消订单入库
     */
    public static class InStockBillType {
        public static final int PURCHASE_BILL = 1;
        public static final int SERVER_BILL = 2;
        public static final int CANCEL_BILL = 3;
    }

    /**
     * 拣货单状态
     * 0：异常，1：待拣货，2：拣货中，3：已完成，4：缺货
     */
    public static class PickingStatus {
        public static final int ERROR = 0;
        public static final int WATTING = 1;
        public static final int PICKING = 2;
        public static final int FINISH = 3;
        public static final int STOCKOUT = 4;
    }

    /**
     * 打包复检单状态
     * 0：复检异常（不用），1：等待复检，2：复检中，3：已完成
     */
    public static class RecheckStatus {
//        public static final int ERROR = 0;
        public static final int WATTING = 1;
        public static final int RECHECKING = 2;
        public static final int FINISH = 3;
    }

    /**
     * 出库单单节点状态
     * 当前节点状态，12101-拣货，12102-打包复检，12103-录入运单号，12104-交接发货
     */
    public static class StockOutBillStatus {
        public static final int PICKING = 12101;
        public static final int PACK_RECHECK = 12102;
        public static final int ENTER_THE_AWB = 12103;
        public static final int SHIPPING = 12104;
    }

    /**
     * 出库单订单类型
     * 1：普通订单，3：换货单，4：补货单
     */
    public static class StockOutBillOrderType {
        public static final int GENERAL = 1;
        public static final int EXCHANGE = 3;
        public static final int SUPPLY = 4;
    }

    /**
     * 单子类型参数
     */
    public static class BillTypeParam {
        public static final int PICKING = 1;
        public static final int RECHECK = 2;
    }

    /**
     * oms修改出库单状态参数
     * 状态为 0 解冻，1 冻结， 2 取消订单
     */
    public static class AlterSOBillStatusParam {
        public static final int UMFREEZE = 0;
        public static final int FREEZE = 1;
        public static final int CANTEL = 2;
    }

    /**
     * 是否状态
     */
    public static final int TYPE_STATUS_YES = 1;//是
    public static final int TYPE_STATUS_NO = 0;//否

    /**
     * 会员状态
     */
    public static class UserStatus {
        public static final int STATUS_YES = 1;//正常
        public static final int STATUS_NO = 0;//停用
    }

    //用户登录时间
    public static final int LOGIN_VALID_TIME = 30 * 24 * 60 * 60;// 30天

    /**
     * 单号前缀标识
     */
    public static class BillNoPreFlag {
        public static final String CG = "CG";
        public static final String RK = "RK";
        public static final String PD = "PD";
        public static final String FH = "FH";
    }

    // 单号值后缀
    public static final String BILL_NO_VAL = "_VAL";

    /**
     * 上架单状态
     */
    public static class GroundingBillStatus {
        public static final int WAITTING = 1;
        public static final int PART = 2;
        public static final int FINISH = 3;
    }

    /**
     * 移库状态
     * 1：待移库，2：已完成
     */
    public static class MovementStatus {
        public static final int WAITTING = 1;
        public static final int FINISH = 2;
    }

    /**
     * 移库类型
     * 1：拣货报损移库，2：普通报损移库，3：普通移库
     */
    public static class MovementType {
        public static final int PICKING_BREAKAGE = 1;
        public static final int GENERAL_BREAKAGE = 2;
        public static final int GENERAL_MOVEMENT = 3;
    }

    /**
     * 报损类型
     * 1：损坏报损，2：丢失报损
     */
    public static class MovementBreakType {
        public static final int BREAK_DOWN = 1;
        public static final int LOSE = 2;
    }

    // 分割线
    public static final String LINE = "\r\n==================================";

    // 缺货标识符
    public static final String OOS_FLAG = "-1";

    // 默认为3000年
    public static final Date DEFULT_VALIDITY_TIME = new Date(32503651200000L);

    /**
     * 消息列表中的类型
     * 001 ： 重新锁库， 002 ：更新订单状态， 003：重试锁库
     */
    public static class MessageQueueType {
        public static final String RELOCK_STOCK = "001";
        public static final String ALTER_ORDER_STATUS = "002";
        public static final String RELOCK = "003";
        public static final String PICKING_CANTEL = "004";
    }

    /**
     * 消息列表中的状态
     * State 0 未处理 1 处理成功 2 处理失败 3 重复处理失败 9 待手动处理
     */
    public static class MessageQueueState {
        public static final int WATTING = 0;
        public static final int SUCCESS = 1;
        public static final int FAILURE = 2;
        public static final int REFAILURE = 3;
        public static final int WAIT_CHECK = 9;
    }

    /**
     * 消息修改定的那状态操作
     */
    public static class MessageQueueAOSOper {
        public static final String ONE = "1"; //
        public static final String TWO = "2"; //
        public static final String THREE = "3";//
        public static final String FOUR = "4";//
        public static final String FIVE = "5";//
    }

    /**
     * 盘点单盘点次数
     */
    public static class CheckBillTimes {
        public static final int FIRST = 1;
        public static final int SECOND = 2;
    }

    /**
     * 盘点单状态
     * 1：等待,2：盘点中,3：盘点中断（部分盘点），4：已完成
     */
    public static class CheckBillStatus {
        public static final int WAITTING = 1;
        public static final int CHECKING = 2;
        public static final int CHECK_PART = 3;
        public static final int FINISH = 4;
    }

    /**
     * 盘点单类型
     * 类型，1：日常，2：大盘
     */
    public static class CheckBillType {
        public static final int USUAL_CHECK = 1;
        public static final int LARGE_CHECK = 2;
    }

    /**
     * 提报状态
     * -1：不可提交，0：未提交，1：已提交
     */
    public static class CheckInfoSubmitStatus {
        public static final int UN_SUBMIT = -1;
        public static final int NO_SUBMIT = 0;
        public static final int IS_SUBMIT = 1;
    }

    /**
     * 仓库状态
     * 0:禁用 1:启用 2:大盘中
     */
    public static class WarehouseStatus {
        public static final int DISABLE = 0;
        public static final int USABLE = 1;
        public static final int CHECKING = 2;
    }

    /**
     * 库区性质
     * 0：不可卖，1：可卖, 2：损坏
     */
    public static class WareAreaType {
        public static final int DISABLE_SALE = 0;
        public static final int USABLE_SALE = 1;
        public static final int DESTORY = 2;
    }

    // rf app标识符
    public static final String RF_APP_CODE = "27";
    // 系统名称
    public static final String SYSTEM_NAME = "WMS";
    // 大盘中标识符
    public static final int PACKING_FLAG = -1;

    /**
     * 货位状态
     * 0：不可卖，1：可卖, 2：损坏
     */
    public static class LocationType {
        public static final int DISABLE_SALE = 0;
        public static final int USABLE_SALE = 1;
        public static final int DESTORY = 2;
    }

    // 默认操作人名称
    public static final String DEFAULT_USER_NAME = "--";

}
