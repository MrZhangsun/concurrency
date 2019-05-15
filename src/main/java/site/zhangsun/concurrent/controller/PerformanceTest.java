package site.zhangsun.concurrent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/7 下午9:02
 */
@RestController
public class PerformanceTest {

    @GetMapping("/jmeter")
    public Object performanceTest() {
        Map<Integer, Object> map = new HashMap<>(10);

        for (int i = 0; i < 100; i++) {
            map.put(i, UUID.randomUUID().toString());
        }
        return map;
    }
}
