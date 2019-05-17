package site.zhangsun.concurrent.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Function: 使用ZK实现分布式锁
 *
 * @author zhangsunjiankun - 2019/5/16 下午1:19
 */
@Slf4j
public class DistributedLocker implements Lock {

    public DistributedLocker() throws IOException {
        this.connect();
        this.lockLatch = new CountDownLatch(1);
    }

    /**
     * 线程计数器用于阻塞线程
     */
    private CountDownLatch lockLatch;

    /**
     * Zookeeper 连接
     */
    private ZooKeeper zooKeeper;

    /**
     * ZK node name as the locker
     */
    private String lockNode = "/locks";

    /**
     * ZK node value
     */
    private String lockValue = "zookeeper-locker";

    /**
     * ZK cluster
     */
    private String cluster =  "localhost:2181,localhost:2182,localhost:2183";

    private void connect() throws IOException {
        this.zooKeeper = new ZooKeeper(cluster, 2000, new LockerWatcher());
    }

    @Override
    public void lock() {
        while (true) {
            try {
                // 尝试创建节点，如果创建成功，则获取锁
                zooKeeper.create(lockNode, lockValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                // 向节点注册监听
                //zooKeeper.getData(lockNode, new LockerWatcher(), new Stat());
                log.info("获取锁成功！session id: {}, current thread: {}", zooKeeper.getSessionId(), Thread.currentThread().getId());
                return;
            } catch (Exception e) {
                log.info("获取锁失败！session id: {}, current thread: {}", zooKeeper.getSessionId(), Thread.currentThread().getId());
                try {
                    if (lockLatch.getCount() <= 0)
                        lockLatch = new CountDownLatch(1);
                    lockLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            Stat exists = zooKeeper.exists(lockNode, new LockerWatcher());
            if (exists != null) {
                int version = exists.getVersion();
                zooKeeper.delete(lockNode, version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * ZK Watcher
     */
    class LockerWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            Event.EventType type = watchedEvent.getType();
            switch (type) {
                case NodeDeleted:
                    lockLatch.countDown();
                    log.info("锁被释放！session id: {}, current thread: {}", zooKeeper.getSessionId(), Thread.currentThread().getId());
                    break;
                default:
            }
        }
    }
}
