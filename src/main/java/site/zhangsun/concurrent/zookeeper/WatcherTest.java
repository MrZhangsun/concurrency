package site.zhangsun.concurrent.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/16 下午2:42
 */
public class WatcherTest {
    private ZooKeeper zooKeeper;

    /**
     * cluster address information
     */
    private static final String host = "localhost:2181,localhost:2182,localhost:2183";

    private String lockNode = "/lock";
    private String lockValue = "lockValue";

    public WatcherTest() throws IOException {
        Watcher lockWatcher = new LockWatcher();
        this.zooKeeper = new ZooKeeper(host, 2000, lockWatcher);
    }

    public void create() throws KeeperException, InterruptedException {
        String s = zooKeeper.create(lockNode, lockValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("临时节点创建成功。。。" + s);
        zooKeeper.getData(lockNode, new LockWatcher(), new Stat());
        System.out.println("add watcher...");
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        WatcherTest watcherTest = new WatcherTest();
        watcherTest.create();
        TimeUnit.SECONDS.sleep(100);
    }


    class LockWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("type: " + watchedEvent.getType());
            System.out.println("event: " + watchedEvent.getPath());
            System.out.println(watchedEvent.toString());
        }
    }
}
