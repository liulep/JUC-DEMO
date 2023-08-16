package AQSLock;

import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

/**
 * 测试自定义的可重入锁
 */
public class ReentrantAQSLockTest {

    /**
     * 成员变量
     */
    private int count;

    /**
     * 可重入锁
     */
    private Lock lock = new ReentrantAQSLock();

    /**
     * 自增count
     */
    public void incrementCount(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" 第一次获取到锁");
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" 第二次获取到锁");
            count ++;
        }finally {
            System.out.println(Thread.currentThread().getName()+" 第一次释放锁");
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+" 第二次释放锁");
            lock.unlock();
        }
    }

    //获取count值
    public int getCount(){
        return count;
    }

    public static void main(String[] args) {
        ReentrantAQSLockTest reentrantAQSLockTest = new ReentrantAQSLockTest();
        IntStream.range(0, 5).forEach(i -> {
            Thread thread = new Thread(() -> {
                reentrantAQSLockTest.incrementCount();
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(Thread.currentThread().getName()+" 最终的结果为："+reentrantAQSLockTest.getCount());
    }
}
