1. synchronized方法被一个线程执行时，其他线程可以执行非synchronized方法，不需要那把锁，不影响

2. 
   ```
      new Thread(new Runnable()){
        public void run(){
          ....
        }
      }
   ```
   
3. 脏读

   1. 写的时候加锁，读没有加锁，可能会读到在写的过程中还没有完成的数据
   
4. 一个同步方法可以调用另一个同步方法，一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到，对象的锁，也就是说synchronized获得的锁是可重入的，本身支持重入锁
   ```
      synchronized a(){
        b();
      }
      synchronized b(){}
   ```
   
   * 重入锁的另一个情形：子类同步方法可以调用父类同步方法
   
5. 写一个程序模拟死锁

   1. 线程a执行的方法需要锁定o1，再锁o2
   
   2. 线程b执行的方法需要锁定o2，再锁o1
   
   3. 线程a锁完o1线程b就开始执行，锁o2，就产生死锁了
   
6. 程序中出现异常，默认情况下锁会被释放，所以要非常小心处理同步业务逻辑中的异常，所以要try/catch做出对异常的处理

7. volatile

   1. 作用：使变量在线程之间可见
   
   2. 代码：
   
      ```
         public class T {
          /*volatile*/ boolean flag = true;

          void m(){
            while(flag){}
            System.out.println("end");
          }

          public static void main(String[] args) {
            final T t = new T();
            new Thread(new Runnable(){
              @Override
              public void run() {
                t.m();
              }
            }).start();
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            t.flag = false;
          }
        }
      ```
      * 解析：
         
         每个线程都有自己的一个内存区域，然后多个线程跟主内存进行交互，对象也放在主内存里面每个线程都有一个缓存区，线程操作主内存的对象中的变量时，是把变量从主内存拷贝一份放到自己的缓存区进行修改，改完了再写回去主内存，接下来看上面的代码：
      
         main方法里起一个线程执行m()方法，这个线程先把flag从主内存读到缓存区中，然后判断flag，因为这时候flag==true，所以是死循环，这种情况下cpu很忙，腾不出时间来从主内存获取最新的flag的值，所以当main线程把t.flag更新为false时，那个线程没有从主内存获取最新的flag，一直还是用那个最初的flag值去判断，就跳不出循环，线程就不能结束了
         
         1. 如果在while循环里面写一个System.out输出语句，这样在while循环执行期间，cpu可能会有一段空闲去主内存读取最新的flag值，就有可能读到被main线程修改过的flag，就有可能跳出循环
         
         2. 如果用volatile修饰变量就可以解决上面的问题
         
8. volatile和synchronized的区别
   
   1. synchronized既有可见性，又有原子性，而volatile只保证可见性，不能保证多个线程共同修改变量时所带来的不一致问题，不能代替synchronized
   
   2. synchronized效率比volatile低不少，所以只需要保证可见性的时候就不要用synchronized
   
   3. volatile不能保证原子性的例子：
   
      1. 两个线程操作volatile变量v，线程a拿到变量v给加到10，线程b拿到v的值就是10，然后给他加到20，然后写回去，在他写回去主内存之前，线程a已经把v加到30，也已经写到主内存了，这时线程b把v(值是20)写进主内存的时候就会把30覆盖掉，因为他写进去时是不会判断主内存中v的值还是不是最初他从主内存中拿v时候的值，所以说volatile只能保证可见性，不能保证原子性，这种情况就要使用synchronized了，先让线程a把v加完到30，线程b再操作 三个程序12-14




