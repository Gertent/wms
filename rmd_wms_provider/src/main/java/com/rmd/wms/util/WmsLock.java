package com.rmd.wms.util;

import com.rmd.bms.lock.DistributedLock;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.enums.WmsLockBusiness;
import com.rmd.wms.enums.WmsLockName;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * wms分布式锁
 *
 * @author : liu
 * @Date : 2017/5/15
 */
public class WmsLock extends DistributedLock {

    private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    // 需要自己初始化自己的zk
    private static ZooKeeper zk;

    static{
        // 连接到ZK服务，多个可以用逗号分割写
        try {
            final String lockAddress = PropertiesUtil.getStringByKey("wms.lock.address", "resources.properties");
            logger.info(Constant.LINE + "lockAddress: " + lockAddress);
            final CountDownLatch connCountDownLatch = new CountDownLatch(1);
            zk = new ZooKeeper(lockAddress, SESSION_TIMEOUT, new Watcher(){
                public void process(WatchedEvent event) {
                    if(event.getState()== Event.KeeperState.SyncConnected){
                        connCountDownLatch.countDown();
                    }
                }
            });
            connCountDownLatch.await();
        } catch (Exception e) {
            logger.error("初始化zk连接时出错", e);
        }
    }

    @Override
    protected void init() {
        setAppName("WMS");
        setZk(zk);
    }

    /**
     * 在此定义业务名称、锁名称常量，或使用枚举
     */
    public WmsLock(WmsLockBusiness businessName, WmsLockName lockName){
        super(businessName.getName(), lockName.getName());
        logger.info(Constant.LINE + "分布式锁初始化完成，businessName：" + businessName.getName() + "，lockName：" + lockName.getName());
    }

    public static void main(String[] args) throws Exception {
        WmsLock wmsLock = new WmsLock(WmsLockBusiness.PURCHASE_BILL_SERVICE, WmsLockName.PUR_IN_STOCK);
        wmsLock.acquire();
    }

}
