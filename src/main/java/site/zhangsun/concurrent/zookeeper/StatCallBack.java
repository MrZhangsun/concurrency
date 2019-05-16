package site.zhangsun.concurrent.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/16 上午11:26
 */
public class StatCallBack implements AsyncCallback.StatCallback {
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        System.out.println("version: " + i);
        System.out.println("path: " + s);
        System.out.println("data: " + o);
        System.out.println("stat: " + stat);
    }
}
