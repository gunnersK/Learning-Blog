# 一、ReentrantLock

   1. lock方法
   
      1. lock.lock()方法尝试获得一把锁，如果这把锁被别的线程占用了，他就一直阻塞在那里等那把锁，直到别的线程释放
      
      2. 在想要释放锁时，lock一般要在finally里面手动释放锁
      
      3. 代码
         
          ```
             public class ReentrantLock1 {
	
               Lock lock = new ReentrantLock();

               void m1(){
                    try{
                         lock.lock(); //等价于synchronized(this)
                         for(int i = 0; i < 5; i++){
                              TimeUnit.SECONDS.sleep(1);
                              System.out.println(i);
                         }
                    } catch(InterruptedException e){
                         e.printStackTrace();
                    } finally {
                         lock.unlock();
                    }
               }

               void m2(){
                     lock.lock();  //如果没得到锁，线程就会阻塞在这里
                     System.out.println("m2....");
                     lock.unlock();
               }

               public static void main(String[] args) {
                    ReentrantLock1 r1 = new ReentrantLock1();
                    new Thread(new Runnable() {
                         public void run() {
                              r1.m1();
                         }
                    }).start();

                    try {
                         TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                         e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                         public void run() {
                              r1.m2();
                         }
                    }).start();
               }

          }
          ```
