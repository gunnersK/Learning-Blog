- 在方法上用了@Transactional注解表示该方法被spring事务管理，可添加属性@Transactional(rollbackFor = Exception.class)，表示当抛出异常时进行事务回滚

- 但是如果用try捕捉异常，异常不能被springAOP捕捉，就不会回滚，但可以在catch块中TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()进行手动回滚，但是这个被事务管理的方法不能被普通方法调用，否则手动回滚不生效（**要搞懂为什么！！！**）

- 事务失效情况

  - https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247486483&idx=2&sn=77be488e206186803531ea5d7164ec53&chksm=cea243d8f9d5cacecaa5c5daae4cde4c697b9b5b21f96dfc6cce428cfcb62b88b3970c26b9c2&token=816772476&lang=zh_CN&scene=21#wechat_redirect
  - 注解加在非public方法，因为spring会判断，非public方法不加事务
  - 同类调用
  - 手动捕获异常
  
    

