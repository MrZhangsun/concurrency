package site.zhangsun.concurrent.zookeeper;

import org.apache.zookeeper.AsyncCallback;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/16 上午11:08
 */
public class CreateCallBack implements AsyncCallback.StringCallback {
    @Override
    public void processResult(int i, String path, Object ctx, String name) {
        System.out.println("create node: " + path);
        System.out.println("data: " + ctx);
    }
}
