package site.zhangsun.concurrent.unsafe;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import site.zhangsun.concurrent.pojo.Person;

import java.util.concurrent.*;


/**
 * <p> Function: 在信号量上我们定义两种操作： acquire（获取） 和 release（释放）。当一个线程调用acquire操作时，
 * 它要么通过成功获取信号量（信号量减1），要么一直等下去，
 * 直到有线程释放信号量，或超时。release（释放）实际上会将信号量的值加1，然后唤醒等待的线程</p>
 *
 * @author : zhangsunjiankun 2018/6/2 上午10:56
 */
@Slf4j
@Data
public class GroupVariableTest {
    private final Person person = new Person();
    private static final int CLIENT_REQUEST_TOTAL = 50000;
    private static final int CURRENCY_NUM = 200;

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory threadFactory = new CustomizableThreadFactory();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CURRENCY_NUM, threadFactory);
        final Semaphore semaphore = new Semaphore(CURRENCY_NUM);
        final CountDownLatch latch = new CountDownLatch(CLIENT_REQUEST_TOTAL);

        for (int i = 0; i <= CLIENT_REQUEST_TOTAL; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    GroupVariableTest group = new GroupVariableTest();
                    Person person = group.getPerson();
                    int steps = person.getSteps();
                    person.setSteps(steps + 1);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("Error: {}", e.getMessage());
                }
            });
            latch.countDown();
        }
        latch.await();
        log.info("Steps of Person: {}", new Person().getSteps());
        executorService.shutdown();
    }

}
