1. Runnable不是线程，它只是个接口，只是个任务执行单元。只是为了将线程的控制和业务逻辑分开
2. 守护线程的典型场景是心跳线程
3. JVM会为一个使用内部锁（synchronized）的对象维护两个集合，Entry Set和Wait Set，也有人翻译为锁池和等待池，意思基本一致。
   对于Entry Set：如果线程A已经持有了对象锁，此时如果有其他线程也想获得该对象锁的话，它只能进入Entry Set，并且处于线程的BLOCKED状态。
   对于Wait Set：如果线程A调用了wait()方法，那么线程A会释放该对象的锁，进入到Wait Set，并且处于线程的WAITING状态。
4. 只要一个任务获取了锁，除非主动让出锁，那么在它执行完退出锁之前，是不会让出锁的
5. 线程被notify之后，只是进入Runnable状态。notify线程不会让出锁，因此它并不会立即进入Running状态，甚至连锁都获取不到，必须重新获取锁。且醒来继续执行，不是从头执行
6. 并发编程中的重要概念：原子性，可见性，有序性
```
    Thread -1
    obj = createObj()   1.
    init = true         2.
    单线程执行时由于最终一致性不会出问题，多线程时，实际的执行顺序可能是先2后1，这时候就会出问题。可以用volatile解决此问题
    
    
    Thread -2
    while(!init){
        sleep();
    }
    useTheObj(obj);
```