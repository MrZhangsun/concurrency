package site.zhangsun.concurrent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.zhangsun.concurrent.zookeeper.DistributedLocker;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/16 下午3:06
 */
@RestController
public class LockTest {

    private static List<Integer> data;
    static {
        data = new LinkedList<>();
        for (int i = 0; i < 100; i ++) {
            data.add(i);
        }
        System.out.println("loading resource...");
    }

    @GetMapping("/lock/{num}")
    public String getLock(@PathVariable(name = "num") int num) throws IOException {
        DistributedLocker locker = new DistributedLocker();
        locker.lock();
        for (int i = 0; i < num; i++)
            data.remove(i);
        locker.unlock();
        return data.toString();
    }


}
