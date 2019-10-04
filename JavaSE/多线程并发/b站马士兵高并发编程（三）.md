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

4. ConcurrentQueue（在并发容器里最重要的，用的最多的）
