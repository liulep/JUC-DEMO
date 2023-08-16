package AQSLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义实现可重入锁
 */
public class ReentrantAQSLock implements Lock {

    private AQSSync sync = new AQSSync();

    @Override
    public void lock() {
        sync.tryAcquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    //创建一个内部类
    public class AQSSync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            //获取状态值
            int state = getState();
            //获取当前线程对象
            Thread currentThread = Thread.currentThread();
            //如果获取的值为0，说明进来的是第一个线程
            if(state == 0){
                //将当前状态改成传递进来的arg值
                if(compareAndSetState(0, arg)){
                    //设置当前线程获取到锁，并且是独占锁
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            }else if(getExclusiveOwnerThread() == currentThread){
                //当前线程获取到过该锁，进行重复入锁
                //将当前状态次数+1
                int nextc = state + arg;
                if(nextc < 0){
                    throw new Error("Maximum lock count exceeded");
                }
                //设置状态值
                setState(nextc);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if(Thread.currentThread() != getExclusiveOwnerThread()){
                //如果当前线程不是独占锁的线程，抛出异常
                throw new IllegalMonitorStateException();
            }
            //对状态减去相应的计数
            int state = getState() - arg;
            //标记是否完全释放锁成功
            boolean flag = false;
            //如果状态为0，说明锁被完全释放
            if(state == 0){
                flag = true;
                //将独占锁的线程设置为空，等待下一个线程独占
                setExclusiveOwnerThread(null);
            }
            //设置状态值
            setState(state);
            //返回标识
            return flag;
        }

        final ConditionObject newCondition(){
            return new ConditionObject();
        }
    }
}
