- 在方法上用了@Transaction注解表示该方法被spring事务管理，可添加属性@Transactional(rollbackFor = Exception.class)，表示当抛出异常时进行事务回滚

- 但是如果用try捕捉异常，异常不能被springAOP捕捉，就不会回滚，但可以在catch块中TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()进行手动回滚，但是这个被事务管理的方法不能被普通方法调用，否则手动回滚不生效（**要搞懂为什么！！！**）

    