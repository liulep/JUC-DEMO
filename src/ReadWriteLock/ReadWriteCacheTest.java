package ReadWriteLock;

import java.util.stream.IntStream;

/**
 * 测试自定义的读写缓存锁
 */
public class ReadWriteCacheTest {
    public static void main(String[] args) {
        ReadWriteCache<String, Object> readWriteCache = new ConcurrentReadWriteCache<String,Object>();

        //测试写锁
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                String key = "name_".concat(String.valueOf(i));
                String value = "liulep_".concat(String.valueOf(i));
                readWriteCache.put(key, value);
            }).start();;
        });

        //测试读锁
        IntStream.range(0, 6).forEach(i -> {
            new Thread(() -> {
                String key = "name_".concat(String.valueOf(i));
                Object o = readWriteCache.get(key);
                System.out.println("Key:"+key+" , Value:"+o);
            }).start();
        });
    }
}
