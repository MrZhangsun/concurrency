package site.zhangsun.concurrent.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;


/**
 * <p> Function: 在信号量上我们定义两种操作： acquire（获取） 和 release（释放）。当一个线程调用acquire操作时，
 * 它要么通过成功获取信号量（信号量减1），要么一直等下去，
 * 直到有线程释放信号量，或超时。release（释放）实际上会将信号量的值加1，然后唤醒等待的线程</p>
 *
 * @author : zhangsunjiankun 2018/6/2 上午10:56
 */
@Slf4j
public class StringConcat {
    private static StringBuilder builder = new StringBuilder();
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
                    builder.append("a");
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("Exception: {}", e.getMessage());
                }
            });
            latch.countDown();
        }
        latch.await();
        executorService.shutdown();
        log.info("Length of String: {}", builder.toString().length());
    }

}
