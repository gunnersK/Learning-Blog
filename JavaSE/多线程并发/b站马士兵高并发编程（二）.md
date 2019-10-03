# 一、ReentrantLock

  1. ReentrantLock和synchronized的理解
  
     1. **为什么synchronized有一对大括号把代码括起来而lock没有？** 
     
        1. 必须再次强调一遍：synchronized是锁的对象，而不是锁代码，只是说synchronized把锁住对象之后要干的活丢到括号里面，所以会有他锁的是代码的错觉。那为什么要把代码丢到括号里呢？因为jvm在synchronized修饰的代码跑完之后会自动释放锁，所以需要知道他代码什么时候跑完，所以需要括号把代码括起来。
        
        2. 再看看lock，一样也是锁住对象，但是jvm不会像synchronized一样在代码跑完之后帮你释放锁，所以他必须手动释放锁，所以lock也就不需要像synchronized一样定义要跑的代码，就不用花括号包代码，如果硬要类比于synchronized的花括号里的代码，个人理解：在lock.lock和unlock之间执行的代码就相当于synchronized花括号中的代码。
        
        3. 在想要释放锁时，lock一般要在finally里面手动释放锁
     
     2. 这里关注的是锁，而不是代码，因为synchronized有修饰代码范围，所以学lock的时候很容易产生理解上的偏差，把重点跑到代码去，其实应该关注的重点是拿没拿到这把锁

     3. 默认synchronized都是不公平锁
