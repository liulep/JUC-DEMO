package ReadWriteLock;

/**
 * 读写缓存锁接口
 * @param <K>
 * @param <V>
 */
public interface ReadWriteCache<K, V> {

    /**
     * 向缓存中写数据
     */
    void put(K key, V value);

    /**
     * 向缓存中读数据
     */
    V get(K key);
}
