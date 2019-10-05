# 一、 数据库架构

1. 存储模块（文件系统）
   
2. 程序模块（管理存储的数据）

3. 影响程序运行的瓶颈是IO

# 二、索引

1. 什么信息能成为索引

   1. 能把信息限定在一定范围内的字段
   
      1. 主键、唯一键、普通键等，只要能让数据具备一定区分性的字段
      
2. 实现索引的数据结构

   * 二叉查找树、B树、主流B+树、Hash及   mysql不支持BitMap
   
3. 密集索引和稀疏索引区别

4. 如何定位并优化慢查询Sql

   1. 根据慢日志定位慢查询sql
   
   2. 使用explain等工具分析sql
   
   3. 修改sql或者尽量让sql走索引
   
# 三、锁   

1. MyISAM与InnoDB关于锁方面的区别

   1. MyISAM默认用表级锁，不支持行级锁；InnoDB默认用行级锁，也支持表级锁
   
   2. MyISAM引擎
   
      1. 当表进行select的时候，会自动给整张表上一个表级的读锁，并且阻塞其他session对表的更新，即当读锁未被释放，另一个session要对表加写锁，就会被阻塞
      
      2. 当表进行增删改的时候，会自动给整张表上一个表级的写锁
      
      3. 读锁（共享锁）
      
         1. 先上读锁的同时，另一个session也可以对表加读锁，即上共享锁时，依然支持上共享锁，在一个session进行范围查询的时候，另一个session依然能对表进行读操作
         
         2. 但是当一个session给表上了读锁，另一个session要进行写操作就会被阻塞了，尽管要写的行不是正在读的那些行，因为他锁住的是整张表，而不是正在读的行
         
         3. select也可以像写锁一样上一个排它锁：select * from table_name **for update**，这样一个session上读锁时，別的session就上不了读锁
         
      4. 写锁（排它锁）
      
         1. 一个session上写锁的时候，另一个session不能进行读写操作，不能上读写锁，会被阻塞
      
      5. 显式给表加锁
      
         1. lock tables table_name read/write
         
         2. unlock tables
         
      6. 总结
      
         1. 共享锁：上共享锁之后，依然支持上共享锁，不支持上排它锁
         
         2. 排它锁：上排它锁之后，不支持上其他所
   

2. 数据库事务四大特性(ACID)

   1. 原子性（Atomic）：事务包含的所有操作要么全执行，要么全失败回滚
   
   2. 一致性（Consistency）：事务应确保数据库的状态从一个一致的状态变为另一个一致的状态。AB账户加起来2000，不管A转账多少给B，事务结束后AB加起来还是2000
   
      * 一致状态的含义：数据库的数据应满足完整性约束
      
   3. **隔离性（Isolation）：** 多个事务并发执行时，一个事务的执行不应该影响其他事务的执行
   
   4. 持久性（Durability）：一个事务一旦提交，他对数据库的修改应该永久保存在数据库中

3. 事务隔离级别以及各级别下的并发访问问题

   1. 事务并发访问引起的问题
   
      1. 更新丢失：mysql所有事务隔离级别在数据库层面上均可避免
      
      2. 脏读：一个事务读到另一个事务未提交的更新数据，可以在READ-COMMITTED事务隔离级别以上避免
      
         1. 查看当前隔离级别：select @@tx_isolation
         
         2. 成因：把两个session都设置为最低的事务隔离级别：set session transaction isolation level read uncommitted，都开启事务：start transaction，session1把字段从1更新为2但不提交，并查询，session2查询，结果和session1查询的一样都是2，这时session1回滚，字段变回原来的1，但是session2仍按字段是2的值去更新并提交，这就发生了脏读
         
         3. 解决：把两个session都设置为set session transaction isolation level read committed，都开启事务：start transaction，session1更新字段为2但不提交，并查询，session2查询，结果字段不是session1更新后的2，而是原来的1，这时session1回滚，字段变回原来的2，这时就算session2仍按字段是1的值去更新并提交，也不是脏读了
         
      3. 不可重复读：事务A多次读取同一数据，事务B在事务A读取数据的过程中，对数据进行了更新并提交，导致事务A多次读取同一数据的结果不一致，REPEATABLE-READ事务隔离级别以上可避免
      
         1. 成因：把两个session都设置为set session transaction isolation level read committed，都开启事务，从session1的角度看，先读一次字段，值是1，再切换到session2的角度，把字段更新为2并提交，再切换回session1，再读一次字段，发现值是2，和上次的数据不一致，这就发生了不可重复读
         
         2. 解决：把两个session都设置为set session transaction isolation level repeatable read(默认事务隔离级别)，都开启事务：start transaction，从session1的角度看，先读一次字段，值是1，再切换到session2的角度，把字段更新为2并提交，再切换回session1，再读一次字段，发现值还是1，即尽管session2做出修改并提交了之后，session1读到的值还是原来未提交的值，这就避免了不可重复读的情况。但是这个时候session1对字段做出修改，是在字段最新的值2之上进行修改的，这也不会导致数据不一致的情况

4. InnoDB可重复读隔离级别下如何避免幻读

5. RC、RR级别下的InnoDB的非阻塞读如何实现
