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
