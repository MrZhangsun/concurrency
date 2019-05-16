package site.zhangsun.concurrent.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/15 下午10:11
 */
@Slf4j
public class ZKClient {

    private ZooKeeper zooKeeper;

    /**
     * cluster address information
     */
    private static final String host = "localhost:2181,localhost:2182,localhost:2183";

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ZKClient zkClient = new ZKClient();
        zkClient.connect();
        zkClient.deleteNode();
        TimeUnit.SECONDS.sleep(5);
    }

    public void connect() throws IOException, InterruptedException {
        this.zooKeeper = new ZooKeeper(host, 2000, new MyWatcher());
        ZooKeeper.States state = this.zooKeeper.getState();

        long sessionId = zooKeeper.getSessionId();
        log.info("连接状态： {}, session id: {}", state, sessionId);
    }

    /**
     * 同步创建节点
     *
     * @param nodeName
     * @param data
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void createNode(String nodeName, byte[] data) throws KeeperException, InterruptedException {
        String s = zooKeeper.create(nodeName, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Response: " + s);
    }

    /**
     * 异步创建节点
     *
     */
    private void createNode() {
        String ctx = "{'name':'jack'}";
        zooKeeper.create("/jack", "jack".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, new CreateCallBack(), ctx);

    }

    private void getNode() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/zookeeper", new MyWatcher());
        System.out.println("**********");
        System.out.println(children);
        System.out.println("**********");
    }

    private void getData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/zookeeper", new MyWatcher(), new Stat());
        System.out.println(new String(data, StandardCharsets.UTF_8));
    }

    /**
     * 同步设置节点
     *
     * @param path
     * @param data
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void setNode(String path, String data) {
        try {
            Stat stat = zooKeeper.setData(path, data.getBytes(), 1);
            System.out.println(stat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            System.out.println(e.getMessage());
        }

    }

    private void setNode(String path, String data, StatCallBack callBack) {
        zooKeeper.setData(path, data.getBytes(), 2, callBack, "");
    }

    private void deleteNode() throws KeeperException, InterruptedException {
        zooKeeper.delete("/person", 0);
    }

    class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            log.info("Event type: {}, event: {}", watchedEvent.getType(), watchedEvent.getState());
        }
    }
}
