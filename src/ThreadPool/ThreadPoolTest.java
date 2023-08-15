package ThreadPool;

import java.util.stream.IntStream;

/**
 * 测试自定义的线程池
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(5);
        IntStream.range(0, 20).forEach(i -> {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "->>>>> Hello ThreadPool.ThreadPool");
            });
        });
        threadPool.shutdown();
        Thread.sleep(1000);
    }
}
