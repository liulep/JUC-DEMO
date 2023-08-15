package CASLock;

/**
 * 自旋锁接口
 */
public interface CasLock {

    /**
     * 加锁
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();
}
