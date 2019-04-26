# 一、AOP

1. AOP: 中文名称面向切面编程

2. 定义：

3. 常用概念

   1. 切点：pointcut
   
   2. 前置通知: 在切点之前执行的功能. before advice
   
   3. 后置通知: 在切点之后执行的功能,after advice
   
   4. 异常通知：如果切点执行过程中出现异常,会触发异常通知.throws advice
   
   5. 切面：所有功能总称叫做切面
   
   6. 织入: 把切面嵌入到原有功能的过程叫做织入
   
   7. 作用（待完善）：aop都是拦service层方法
   
4. spring 提供了 2 种 AOP 实现方式

   1. Schema-based
   
      1. 每个通知都需要实现接口或类
      
      2. 配置 spring 配置文件时在<aop:config>配置
      
   2. AspectJ
   
      1. 每个通知不需要实现接口或类
      
      2. 配置 spring 配置文件是在<aop:config>的子标签<aop:aspect>中配置

# 二、Schema-based 实现步骤

   1. spring的applicationcontext文件头的xmlns属性是引入schema的
   
   2. 下面通提代码来说明前置、后置、异常、环绕通知的用法，以下是两个类的说明
    
      1. Demo类作用：用Demo类里的方法来作为切点（相当于service层的方法）
      
      2. Test类作用：调用Demo类中的方法来测试AOP功能

   3. 前置通知
   
      1. applicationContext.xml配置文件，applicationcontext文件头的xmlns属性是引入schema的
      
         1. 通配符的各种用法见下面的代码
      
         ```
            <?xml version="1.0" encoding="UTF-8"?>
            <beans xmlns="http://www.springframework.org/schema/beans"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:aop="http://www.springframework.org/schema/aop"
                xsi:schemaLocation="http://www.springframework.org/schema/beans
                    http://www.springframework.org/schema/beans/spring-beans.xsd
                    http://www.springframework.org/schema/aop
                    http://www.springframework.org/schema/aop/spring-aop.xsd">

                <bean id="mybefore" class="cn.gunners.advice.MyBeforeAdvice"></bean>
                <bean id="myafter" class="cn.gunners.advice.MyAfterAdvice"></bean>

              <!-- 配置切面 -->
                <aop:config>
                
                <!-- 配置切点 -->
                
                  <!-- 第一个星号表示声明出通配符 -->
                  <aop:pointcut expression="execution(* cn.gunners.test.Demo.demo2())" id="mypoint"/>
                  
                  <!-- 匹配cn.gunners.test.Demo类下所有无参方法 -->
                  <aop:pointcut expression="execution(* cn.gunners.test.Demo.*())" id="mypoint"/>
                  
                  <!-- 匹配cn.gunners.test.Demo任意类型参数的方法 -->
                  <aop:pointcut expression="execution(* cn.gunners.test.Demo.*(..))" id="mypoint"/>
                  
                  <!--匹配cn.gunners.test包下任意方法  -->
                  <aop:pointcut expression="execution(* cn.gunners.test.*.*(..))" id="mypoint"/>
                  
                  <!-- 配置通知 -->
                  <aop:advisor advice-ref="mybefore" pointcut-ref="mypoint"/>
                  <aop:advisor advice-ref="myafter" pointcut-ref="mypoint"/> 	
                </aop:config>

              <bean id="demo" class="cn.gunners.test.Demo"></bean>

         ```
   
      2. Demo类
      
         ```
            public class Demo{
            
            public void demo4(String name){
              System.out.println("demo4");
            }
          }
         ```
   
      3. Advice类
      
         1. 可以在前置通知类里获取方法的**参数**等各种信息，详见以下代码
      
         ```
            public class MyBeforeAdvice implements MethodBeforeAdvice {
            @Override
            public void before(Method arg0, Object[] arg1, Object arg2) 
                throws Throwable {

            //arg0：切点方法对象，Method对象
              System.out.println("ִ切点方法对象："+arg0+",方法名:"+arg0.getName());

            //arg1：切点方法参数
              if(arg1 != null && arg1.length > 0){
                System.out.println("切点方法参数："+arg1[0]);
              }else{
                System.out.println("切点没有参数");
              }

            //arg2：切点在哪个对象中
              System.out.println("对象："+arg2);

              System.out.println("ִ执行前置通知");
            }
          }
         ```
         
      4. Test类
         
         ```
            public class Test {
            public static void main(String[] args) {
              ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
              Demo demo = ac.getBean("demo",Demo.class);
              demo.demo4("传递的参数");
            }
          }
         ```
   
   4. 后置通知
   
      1. applicationContext.xml配置文件，下面的applicationcontext都省略文件头的xmlns属性
      
         看前置通知的xml文件
         
      2. Demo类
      
         ```
            public class Demo{
            
            public void demo4(String name){
              System.out.println("demo4");
            }
            public String demo5(String name){
              System.out.println("demo5");
              return "demo5的返回值";
            }
          }
         ```
         
      3. Advice类
      
         1. 除了可以获取和前置通知一样的切点方法的信息，还可以获取切点方法的**返回值**
      
         ```
            public class MyAfterAdvice implements AfterReturningAdvice {
              @Override
              public void afterReturning(Object arg0, Method arg1, Object[] arg2,
                  Object arg3) throws Throwable {
              //arg0：切点方法返回值
                System.out.println("arg0:"+arg0);

              //剩下的arg跟前置通知的一样	
                System.out.println("arg1:"+arg1);
                System.out.println("arg2:"+arg2);
                System.out.println("arg3:"+arg3);

                System.out.println("ִ执行后置通知");
              }
            }
         ```
         
      4. Test类
         
         ```
            public class Test {
            public static void main(String[] args) {
              ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
              Demo demo = ac.getBean("demo",Demo.class);
              demo.demo4("传递的参数");
              demo.demo5("demo5的参数");
            }
          }
         ```
   
   5. 异常通知
   
      1. applicationContext.xml配置文件 
      
         ```
            <?xml version="1.0" encoding="UTF-8"?>
            <beans xmlns="http://www.springframework.org/schema/beans"
                <!-- 省略文件头的xmlns属性 -->

              <!-- 异常通知 -->

                <bean id="mythrow" class="cn.gunners.advice.MyThrow"></bean>

                <aop:config>
                  <aop:pointcut expression="execution(* cn.gunners.test.Demo.demo1())" id="mypoint"/>
                  <aop:advisor advice-ref="mythrow" pointcut-ref="mypoint"/>
                </aop:config>

                <bean id="demo" class="cn.gunners.test.Demo"></bean>
            </beans>
         ```
         
      2. Demo类
         
         在这里面使切点方法出现异常，但是不要在里面try/catch，要使用Throw拦截异常就不要再切点方法里自己捕获异常
         
         ```
            public class Demo{  
              public void demo1() throws Exception{
                int i = 5/0;  //算术异常
                System.out.println("demo1");
              }
            }
         ```
         
      3. Advice类    要实现接口
      
         ```
            public class MyThrow implements ThrowsAdvice {
	
              //schema-base方式的异常必须实现接口，然后自己写方法（因为schema-base方式在配置切点的时候直接配置异常通知类就好，不用指定异常通知的具体方法，
              //所以只有实现特定的接口了他才知道要用哪个方法，而aspect方式配置切点有手动指定方法，所以不用实现特定接口，这里要区别开来），且必须叫afterThrowing，1个或4个参数
              //异常类型要和切点报的异常类型一致，例如下面的ArithmeticException，这个异常方法只能应用在报ArithmeticException异常的切点

              public void afterThrowing(ArithmeticException ex) throws Throwable {
                   System.out.println("执行异常通过-schema-base方式");
                }

              public void afterThrowing(Method m, Object[] args, Object target, Exception ex) {
                System.out.println("执行异常通知");
                }
            }
         ```
         
      4. Test类
      
         ```
            public class Test {
              public static void main(String[] args) {
                ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
                Demo demo = ac.getBean("demo",Demo.class);
                try {
                  demo.demo1();
                } catch (Exception e) {
            //			e.printStackTrace();
                }
              }
            }
         ```
      
   
   6. 环绕通知
   
      1. applicationContext.xml配置文件 
      
         ```
            <?xml version="1.0" encoding="UTF-8"?>
            <beans xmlns="http://www.springframework.org/schema/beans"
                <!-- 省略文件头的xmlns属性 -->

              <!-- 环绕通知 -->
	
                <bean id="myarround" class="cn.gunners.advice.MyArround"></bean>

                <aop:config>
                  <aop:pointcut expression="execution(* cn.gunners.test.Demo.demo1())" id="mypoint"/>
                  <aop:advisor advice-ref="myarround" pointcut-ref="mypoint"/>
                </aop:config>

                <bean id="demo" class="cn.gunners.test.Demo"></bean>
            </beans>
         ```
         
      2. Demo类
         
         ```
            public class Demo{  
              public void demo1() throws Exception{
                System.out.println("demo1");
              }
            }
         ```
         
      3. Advice类  
      
         ```
            public class MyArround implements MethodInterceptor{
	
              //环绕通知就是前置后置通知放一起

              public Object invoke(MethodInvocation arg0) throws Throwable {
                System.out.println("环绕-前置");
                Object result = arg0.proceed();//放行，调用切点方式
                System.out.println("环绕-后置");
                return result;
              }

            }
         ```
         
      4. Test类
      
         调用切点方法就得了
   
# 三、AspectJ 实现步骤

   1. 前置通知
   
   2. 后置通知
   
   3. 异常通知
   
      1. applicationContext.xml配置文件
      
         ```
            <?xml version="1.0" encoding="UTF-8"?>
            <beans xmlns="http://www.springframework.org/schema/beans"
               <!-- 省略文件头的xmlns属性 -->

                <!-- 先声明异常类MyThrowAdvice -->
                <bean id="mythrow" class="cn.gunners.advice.MyThrowAdvice"></bean>

                <!-- 配置切面，下面必须明确指定异常通知类和异常方法 -->
                <aop:config>

                  <!-- 配置aspect异常，引用上面定义的id为mythrow的bean对应的异常通知类 -->
                  <aop:aspect ref="mythrow">

                    <!-- 配置切点，即作用于哪个切点的异常 -->
                    <aop:pointcut expression="execution(* cn.gunners.test.Demo.demo1())" id="mypoint"/>

                    <!-- 配置异常，method是上面引入的异常通知类的myexception方法 -->
                      <!-- <aop:after-throwing method="myexception" pointcut-ref="mypoint"/>	-->
                      <!-- throwing是异常对象名，必须和异常通知类中方法的参数名(异常对象名)相同（也可以不在方法中声明异常对象参数） -->
                      <aop:after-throwing method="myexception" pointcut-ref="mypoint" throwing="e"/>	
                  </aop:aspect>
                </aop:config>

              <bean id="demo" class="cn.gunners.test.Demo"></bean>
            </beans>

         ```
         
      2. Demo类
         
         在这里面使切点方法出现异常
         
         ```
            public class Demo{  
              public void demo1() throws Exception{
                int i = 5/0;  //算术异常
                System.out.println("demo1");
              }
            }
         ```
         
      3. Advice类    不用实现接口
      
         ```
            public class MyThrowAdvice {
	
              //异常通知类的方法的参数名相同必须和xml文件中切点异常的throwing异常对象名一致（也可以不在方法中声明异常对象参数）
              public void myexception(Exception e){
                System.out.println("执行异常通知"+"异常message："+e.getMessage());
              }
            }
         ```
         
      4. Test类
      
         ```
            public class Test {
              public static void main(String[] args) {
                ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
                Demo demo = ac.getBean("demo",Demo.class);
                try {
                  demo.demo1();
                } catch (Exception e) {
            //			e.printStackTrace();
                }
            }
         ```
   
   4. 环绕通知
