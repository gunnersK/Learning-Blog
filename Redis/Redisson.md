- 集合数据分片

	- 集群模式下，Redisson为单个Redis集合类型提供了自动分片的功能。Redisson通过自身的分片算法，将一个大集合拆分为若干个片段（默认231个，分片数量范围是3 - 16834），然后将拆分后的片段均匀的分布到集群里各个节点里，保证每个节点分配到的片段数量大体相同。支持的数据结构类型包括Set和Map

- 锁

	- 线程1用"lock"这个key调用redissonClient.getLock方法获得对象lock1，然后lock1.tryLock()上锁；线程2同样用"lock"这个key调用getLock方法获得对象lock2，这时候调用lock2.tryLock()就不能上锁了，也不能调用lock2.unlock()解锁，需要在线程1中lock1.unlock()才能进行上锁的操作

	```
		public void testLock() throws Exception{
            RLock lock1 = redissonClient.getLock("lock");
            lock1.tryLock();

            new Thread(()-> {
                RLock lock2 = redissonClient.getLock("lock");
                log.info("lock2.isLocked()=="+lock2.isLocked()); //true
                log.info("lock2.toString()=="+lock2.toString());
                log.info("lock2.tryLock()=="+lock2.tryLock());  //false
                try {
                    Thread.sleep(5000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                log.info("lock2.isLocked()=="+lock2.isLocked()); //false
                log.info("lock2.toString()=="+lock2.toString());
                log.info("lock2.tryLock()=="+lock2.tryLock());  //true
                lock2.unlock();
                log.info("lock2.isLocked()=="+lock2.isLocked()); //false
            }).start();

            Thread.sleep(2000);
            lock1.unlock();
        }
	```