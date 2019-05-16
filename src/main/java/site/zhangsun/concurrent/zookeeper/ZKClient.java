package site.zhangsun.concurrent.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException, NoSuchAlgorithmException {
        ZKClient zkClient = new ZKClient();
        zkClient.connect();
       zkClient.setAcl();
        TimeUnit.SECONDS.sleep(5);
    }

    private void setAcl() throws NoSuchAlgorithmException, KeeperException, InterruptedException {
        List<ACL> acls = new ArrayList<>();
        Id c1 = new Id("digest", DigestAuthenticationProvider.generateDigest("murphy:123"));
        Id c2 = new Id("digest", DigestAuthenticationProvider.generateDigest("moly:123"));
        ACL acl1 = new ACL(ZooDefs.Perms.ADMIN, c1);
        ACL acl2 = new ACL(ZooDefs.Perms.ADMIN, c2);
        ACL acl3 = new ACL(ZooDefs.Perms.READ, c1);
        ACL acl4 = new ACL(ZooDefs.Perms.READ, c2);
        ACL acl5 = new ACL(ZooDefs.Perms.WRITE, c2);
        ACL acl6 = new ACL(ZooDefs.Perms.DELETE, c1);
        acls.add(acl1);
        acls.add(acl2);
        acls.add(acl3);
        acls.add(acl4);
        acls.add(acl5);
        acls.add(acl6);

        String s = zooKeeper.create("/aclNode", "aclData".getBytes(), acls, CreateMode.PERSISTENT);
        System.out.println("ACL Node: " + s);
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

    private void exist() throws KeeperException, InterruptedException {
        zooKeeper.register(new MyWatcher());
        Stat stat = zooKeeper.exists("/zookeeper", true);
        if (stat != null)
            System.out.println(stat.getVersion());
        else
            System.out.println("Node doesn't exist");
    }

    private void setWatcher() {
        zooKeeper.register(new MyWatcher());

    }

    class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            Event.EventType type = watchedEvent.getType();
            switch (type) {
                case None:
                    System.out.println("Event type :" + type );
                    break;
                case NodeDataChanged:
                    System.out.println("Event type :" + type );
                    break;
                case NodeCreated:
                    System.out.println("Event type :" + type );
                    break;
                case NodeDeleted:
                    System.out.println("Event type :" + type );
                    break;
                case NodeChildrenChanged:
                    System.out.println("Event type :" + type );
                    break;
                    default:
                        System.out.println("Event type :" + type );

            }
            log.info("Event type: {}, event: {}", watchedEvent.getType(), watchedEvent.getState());
        }
    }
}
