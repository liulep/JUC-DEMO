## 手动开发线程池

> 实现过程拆分

### 1.定义核心字段

```java
//默认阻塞线程大小
private static final int DEFALUT_WORKQUEUE_SIZE = 5;

//模拟实际的线程池，使用阻塞队列来实现生产者-消费者模式
private BlockingQueue<Runnable> workQueue;

//模拟实际的线程池，使用List集合保存线程池内部的工作线程
private List<WorkThread> workThreads = new ArrayList<>();
```

### 2.创建内部类

```java
/**
 * 主要的作用是消费workQueue中的任务，并执行，
 * 工作线程需要不断从workQueue中获取线程
 * 所以使用while(true)循环不断尝试从消费队列中任务
 */
class WorkThread extends Thread{
    @Override
    public void run() {
        //获取当前线程
        Thread currentThread = Thread.currentThread();
        //不断循环获取队列中的任务
        while (true){
            try {
                //检测线程是否中断
                if(currentThread.isInterrupted()){
                    break;
                }
                //当没有任务时，阻塞线程
                Runnable workTask = workQueue.take();
                workTask.run();
            }catch (InterruptedException e){
                //当发生终端异常时需要重新设置中断标识位
                //如果不重新中断标记位，将会退出循环
                currentThread.interrupt();
            }
        }
    }
}
```

### 3.创建线程池的构造方法

```java
/**
 * 在ThreadPool的构造方法中传入线程池的大小和阻塞队列
 */
public ThreadPool(int poolSize, BlockingQueue<Runnable> workQueue){
    this.workQueue = workQueue;
    //创建poolSize个工作线程并将其加入workThreads集合中
    IntStream.range(0, poolSize).forEach(i -> {
        WorkThread workThread = new WorkThread();
        workThread.start();
        this.workThreads.add(workThread);
    });
}

public ThreadPool(int poolSize){
    this(poolSize, new LinkedBlockingDeque<>(DEFALUT_WORKQUEUE_SIZE));
}
```

### 4.创建执行任务的方法

```java
//通过线程池执行任务
public void execute(Runnable task){
    try {
        this.workQueue.put(task);
    }catch (InterruptedException e){
        e.printStackTrace();
    }
}
```

### 5.创建关闭线程池的方法

```java
//关闭线程池
public void shutdown(){
    if(this.workThreads != null && this.workThreads.size() > 0){
        workThreads.stream().forEach(workThread -> {
            workThread.interrupt();
        });
    }
}
```

### 6.测试自定义的线程池

```java
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(5);
        IntStream.range(0, 20).forEach(i -> {
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "->>>>> Hello ThreadPool");
            });
        });
        threadPool.shutdown();
        Thread.sleep(1000);
    }
}
```

### 7.结果

```
Thread-2->>>>> Hello ThreadPool
Thread-0->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-1->>>>> Hello ThreadPool
Thread-4->>>>> Hello ThreadPool
Thread-4->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-3->>>>> Hello ThreadPool
Thread-0->>>>> Hello ThreadPool
Thread-0->>>>> Hello ThreadPool
Thread-2->>>>> Hello ThreadPool
Thread-2->>>>> Hello ThreadPool
Thread-4->>>>> Hello ThreadPool
Thread-1->>>>> Hello ThreadPool
# 每个线程都得到了复用
```

