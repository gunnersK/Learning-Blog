# 一、ReentrantLock

  1. ReentrantLock和synchronized的理解
  
     1. **为什么synchronized有一对大括号把代码括起来而lock没有？** 必须再次强调一遍：synchronized是锁的对象，而不是锁代码，因为synchronized把代码包起来，所以会有他锁的是代码的错觉，只是说jvm在synchronized修饰的代码跑完之后会自动释放锁。再看看lock，一样也是锁住对象，但是他必须手动释放锁，jvm不会像synchronized一样在代码跑完之后帮你释放锁，所以lock也就不存在像synchronized一样有一对括号把代码包起来，lock一般要在finally里面手动释放锁
     
     2. 这里关注的是锁，而不是代码，因为synchronized有修饰代码范围，所以学lock的时候很容易产生理解上的偏差，把重点跑到代码去，其实应该关注的重点是锁

     3. 默认synchronized都是不公平锁
