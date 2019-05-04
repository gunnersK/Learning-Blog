# 一、自动注入

1. 在 Spring 配置文件中对象名和 ref=”id”id 名相同使用自动注入,可以不配置<property/>

2. 两种配置办法

   1. 在<bean>中通过 autowire=”” 配置,只对这个<bean>生效
   
   2. 在<beans>中通过 default-autowire=””配置,表当当前文件中所有<bean>都是全局配置内容
   
3. autowire的可取值

   1. default 默认值,根据全局 default-autowire=””值.默认全局和局部都没有配置情况下,相当于 no
   
   2. no 不自动注入
   
   3. byName 通过名称自动注入.在 Spring 容器中找类的 Id
   
   4. byType 根据类型注入
   
      如果要用byType，就不可以出现两个相同类型的<bean>
      
   5. constructor 根据构造方法注入
   
      1. 提供对应参数的构造方法(构造方法参数中包含注入对象那个)
      
      2. 底层使用 byName, 构造方法参数名和其他<bean>的 id相同
      
# 二、Spring 中加载 properties 文件      

   1. 只要部署到服务器上的时候需要改变的值，例如ip地址，都需要放到properties文件中，叫做软编码

   2. 在 spring 配置文件中先引入 xmlns:context的schema，添加
   
      ```
         <context:property-placeholder location="classpath:db.properties"/>
      ```
      
      如果需要记载多个配置文件逗号分割
      
   3. 在被 Spring管理的类中通过@Value(“${key}”)取出 properties中内容   
   
      1. 添加注解扫描
      
         ```
            <context:component-scan base-package="com.bjsxt.service.impl"></context:component-scan>
         ```
         
      2. 在类中添加@Value(“${key}”)
      
         1. key 和变量名可以不相同
         
         2.  变量类型任意,只要保证 key 对应的 value 能转换成这个类型就可以
         
            ```
               @Value("${my.demo}")
                private String test;
            ```
                
# 三、scope 属性 

   1. 暂缺
   
# 四、单例设计模式   

   1. 暂缺

# 五、声明式事务

   1. 概念
      
      1. 编程式事务：由程序员编程事务控制代码
      
      2. 声明式事务：事务控制代码已经由 spring 写好，程序员只需要声明出哪些方法需要进行事务控制和如何进行事务控制
      
   2. 声明式事务都是针对于 ServiceImpl 类下方法的
      
   3. 事务管理器基于通知(advice)的
      
   4. 配置applicationContext.xml文件
   
      ```
         <?xml version="1.0" encoding="UTF-8"?>
          <beans xmlns="http://www.springframework.org/schema/beans"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:aop="http://www.springframework.org/schema/aop"
              xmlns:tx="http://www.springframework.org/schema/tx"
              xmlns:context="http://www.springframework.org/schema/context"
              xsi:schemaLocation="http://www.springframework.org/schema/beans
                  http://www.springframework.org/schema/beans/spring-beans.xsd
                  http://www.springframework.org/schema/aop
                  http://www.springframework.org/schema/aop/spring-aop.xsd
                  http://www.springframework.org/schema/tx
                  http://www.springframework.org/schema/tx/spring-tx.xsd
                  http://www.springframework.org/schema/context
                  http://www.springframework.org/schema/context/spring-context.xsd">

            <!-- 数据源 -->
                <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
                  <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
                  <property name="url" value="jdbc:mysql://localhost:3306/ssm"></property>
                  <property name="username" value="root"></property>
                  <property name="password" value="smallming"></property>
                </bean>
                <!-- SqlSessinFactory对象 -->
                <bean id="factory" class="org.mybatis.spring.SqlSessionFactoryBean">
                  <property name="dataSource" ref="dataSource"></property>
                  <property name="typeAliasesPackage" value="com.bjsxt.pojo"></property>
                </bean>
                <!-- 扫描器 -->
                <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
                  <property name="basePackage" value="com.bjsxt.mapper"></property>
                  <property name="sqlSessionFactory" ref="factory"></property>
                </bean>



                  <!-- 相当于切点的通知类Advice，这个类在spring-jdbc.jar中 -->
                  <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">

                    <!-- 关联数据源 -->
                    <property name="dataSource" ref="dataSource"></property>
                  </bean>



                  <!-- 配置声明式事务 -->
                  <!-- 要把txManager这个bean赋给配置声明式事务 -->
                  <tx:advice id="txAdvice" transaction-manager="txManager">
                    <tx:attributes>

                      <!-- 在这里面定义哪些方法需要有事务控制，可以用通配符 -->
                      <tx:method name="ins*"/>
                      <tx:method name="del*"/>
                      <tx:method name="upd*"/>
                      <tx:method name="*"/>
                    </tx:attributes>
                  </tx:advice>



                  <aop:config>

                    <!-- 因为在tx:advice配置声明式事务里面可以用通配符表示需要事务控制的方法，所以这里可以把切点的范围写大一点，不必太精确 -->
                    <aop:pointcut expression="execution(* cn.gunners.service.impl.*.*(..))" id="mypoint"/>

                    <!-- 事务管理器基于通知(advice)的，这个通知就是txManager这个bean，本来直接把它赋给切点就行了，但是现在要先把txManager赋给 -->
                    <!-- <tx:advice id="txAdvice" transaction-manager="txManager">，再把txAdvice赋给切点，就是拐一个弯 -->
                    <aop:advisor advice-ref="txAdvice" pointcut-ref="mypoint"/>
                  </aop:config>

          </beans>
      ```
      
      
      
      
      
      
      
      
      
      
      
      
      
      
       
      
      
      
      
