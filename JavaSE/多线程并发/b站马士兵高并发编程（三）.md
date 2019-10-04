# 一、SingleTon

# 二、并发容器

   1. ConcurrentHashMap
      1. HashMap线程不安全，HashTable线程安全。TreeMap默认排好序。
         ConcurrentSkipListMap高并发且排序 https://blog.csdn.net/sunxianghuang/article/details/52221913
         
      2. map总结
         1. 没有并发：hashmap、treemap(排序)、linkedhashmap
         
         2. 小量并发：hashtable
         
         3. 高并发：concurrenthashmap
         
         4. 高并发+排序：concurrentskiplistmap
         
2. CopyOnWriteList(写时复制) 写慢读快  适合写的很少，读的特别特别多。赋值一份出来再新增元素  读不用加锁

3. SynchronizedList(Collection)

4. Queue（队列在并发容器里最重要的，用的最多的）

   1. 单向队列Queue，双端队列Dueue
   
   2. Queue方法
   
      1. poll()：拿出来删除
            
      2. peek()：拿出来不删除 
      
      3. add()：放进去，但在一些有容量限制的Queue实现里面这个方法会抛异常
      
      4. offer()：放进去，不会抛异常，会返回boolean
      
         offer()还有一种是按时间段阻塞的，看下面ArrayBlockingQueue的代码
   
   2. 高并发时用两种队列
   
      1. ConcurrentLinkedQueue   加锁式队列
      
      2. BlockingQueue  阻塞式队列
      
         1. 阻塞式队列新增的方法，可以自动实现阻塞式的生产者消费者模式
         
            1. put()--表示如果队列满了，线程会自动阻塞等待
            
            2. take()--如果队列空了，线程会自动阻塞等待
      
         1. LinkedBlockingQueue 
         
            1. 无界队列，放多少个都行，一直到OOM
            
            2. 代码：
            
               ```
                  static BlockingQueue<String> strs = new LinkedBlockingQueue<>();
	
                  static Random r = new Random();

                  public static void main(String[] args) {
                     new Thread(()->{
                        for(int i = 0; i < 100; i++){
                           try {
                              strs.put("a" + i);   //满了就会等待
                              TimeUnit.MICROSECONDS.sleep(r.nextInt(1000)); 
                           } catch (Exception e) {
                              e.printStackTrace();
                           }
                        }
                     }).start();

                     for(int i = 0; i < 5; i++){
                        new Thread(()->{
                           while(true){
                              try {
                                 System.out.println(Thread.currentThread().getName()+strs.take()); //空了就会等待
                              } catch (Exception e) {
                                 e.printStackTrace();
                              }
                           }
                        }).start();
                     }
                  }
               ```
         
         2. ArrayBlockingQueue 
         
            1. 有界队列
            
            2. 代码：
            
               ```
                  static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);
	
	               static Random r = new Random();
                  
                  public static void main(String[] args) throws InterruptedException {
                     for(int i = 0; i < 10; i++){
                        strs.put("a");
                     }

               //		strs.put("a");  满了就等待，线程阻塞
               //		strs.add("a");  满了报异常
               //		System.out.println(strs.offer("a"));  满了就退出，也不加进去
                     		System.out.println(strs.offer("a", 1, TimeUnit.SECONDS));  //在规定时间内，满了就退出

                  }
               ```
