package CASLock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁实现类
 */
public class MyCasLock implements CasLock{

    //创建AtomicReference类型的成员变量
    private AtomicReference<Thread> threadOwner = new AtomicReference<>();

    @Override
    public void lock() {
        //获取当前线程对象
        Thread currentThread = Thread.currentThread();
        //开始自旋操作
        for(;;){
            //如果自旋修改当前线程对象成功，则退出自旋
            if(threadOwner.compareAndSet(null, currentThread)){
                break;
            }
        }
    }

    @Override
    public void unlock() {
        //获取当前线程的对象
        Thread currentThread = Thread.currentThread();
        //通过CAS方式将当前线程的对象修改为null
        threadOwner.compareAndSet(currentThread, null);
    }
}
