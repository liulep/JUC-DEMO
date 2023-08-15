package CASLock;

import java.util.stream.IntStream;

/**
 * 自定义自旋锁测试
 */
public class CasLockTest {

    //创建CasLock对象
    private CasLock lock = new MyCasLock();
    //成员变量count
    private long count = 0;

    /**
     * 自增count的方法
     */
    public void incrementCount(){
        try {
         lock.lock();
         count++;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取count的值
     */
    public long getCount(){
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        //创建CasLockTest对象
        CasLockTest casLockTest = new CasLockTest();

        Thread threadA = new Thread(() ->{
            IntStream.range(0, 1000).forEach(i -> {
                casLockTest.incrementCount();
            });
        });

        Thread threadB = new Thread(() ->{
            IntStream.range(0, 1000).forEach(i -> {
                casLockTest.incrementCount();
            });
        });

        threadA.start();
        threadB.start();

        threadB.join();
        threadA.join();

        System.out.println("count的最终结果为："+ casLockTest.getCount());
    }
}
