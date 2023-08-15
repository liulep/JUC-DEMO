package ReadWriteLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存读写锁的实现类
 */
public class ConcurrentReadWriteCache<K, V> implements ReadWriteCache<K, V> {

    /**
     * 缓存中存储数据的map
     */
    private volatile Map<K, V> map = new HashMap<>();

    /**
     * 读写锁
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    //读锁
    private final Lock readLock = lock.readLock();

    //写锁
    private final Lock writeLock = lock.writeLock();


    /**
     * 向缓存中写入数据
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName()+" 开始写数据");
            map.put(key, value);
        }finally {
            System.out.println(Thread.currentThread().getName()+" 结束写数据");
            writeLock.unlock();
        }
    }

    /**
     * 向缓存中读取数据
     * @param key
     */
    @Override
    public V get(K key) {
        V value = null;
        try {
            readLock.lock();
            System.out.println(Thread.currentThread().getName()+" 开始读取数据");
            value = map.get(key);
        }finally {
            System.out.println(Thread.currentThread().getName()+" 结束读取数据");
            readLock.unlock();
        }
        //如果读取的数据不为空
        if (value != null){
            return value;
        }
        //如果读取的数据为空
        try {
            //获取写锁
            writeLock.lock();
            System.out.println(Thread.currentThread().getName()+" 开始从数据库中读取数据并写入缓存中");
            //模拟从数据库从读取数据
            value = getValueFromDB();
            //将数据写入缓存中
            map.put(key, value);
        }finally {
            System.out.println(Thread.currentThread().getName()+" 结束从数据库中读取数据并写入缓存中");
            writeLock.unlock();
        }
        return value;
    }

    /**
     * 模拟从数据库从读取数据
     */
    public V getValueFromDB(){
        return (V) "liulep";
    }
}
