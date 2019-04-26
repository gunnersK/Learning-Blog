# 一、Spring简介

1. Spring核心功能（必须弄明白，面试就问这三个）

   1. IoC/DI&ensp;&ensp;&ensp;控制反转/依赖注入

   2. AOP&ensp;&ensp;&ensp;面向切面编程

   3. 声明式事务

2. Spring 框架中重要概念

   1. 把Spring 当作一个大容器

   2. BeanFactory 接口是老版本

      1. 新版本中 ApplicationContext 接口，是 BeanFactory 子接口，BeanFactory 的功能在ApplicationContext 中都有

   3. 从 Spring3 开始把 Spring 框架的功能拆分成多个 jar，Spring2 及以前就一个 jar

# 二、IoC（控制反转）

1. IoC是什么：将程序员主动通过new实例化对象的事情转交给spring负责

2. IoC最大的作用：解耦

# 三、spring创建对象的三种方式

1. 通过构造方法创建

   1. 无参构造创建（默认情况）

   2. 有参构造创建，需要明确配置
   
      1. 指定参数（通过这三个参数精确定位到哪个构造方法的参数，以确定使用哪个构造方法）
         
         1. index  参数的索引，从0开始

         2. name  参数名

         3. type  数据类型           

      2. 给参数赋值

         1. ref  参数是实体类的用ref定义，表示引用另一个bean 

         2. value  基本数据类型或string用value

     tips：如果设定的条件匹配多个构造方法，则执行最后的构造方法

2. 实例工厂

   1. 先要有一个实例工厂类PeopleFactory

   ```
   public class PeopleFactory {
	//实例工厂方法
	public People newInstance(){
		return new People(1,"测试");
	}
   }
   ```

   2. 然后在applicationContext.xml 文件中配置工厂对象和需要创建的对象

   ```
    <bean id="factory" class="cn.gunners.pojo.PeopleFactory"></bean>
    <bean id="peo1" factory-bean="factory" factory-method="newInstance"></bean>
   ```

3. 静态工厂

   1. 不需要创建工厂，直接快速创建需要的对象

      1. 编写一个静态工厂(在方法上添加 static)

      ```
      public class PeopleFactory {
	  //静态工厂方法
		public static People newInstance(){
			return new People(1,"测试");
		}
      }
      ```

      2. 在 applicationContext.xml 中

      ```
      <bean id="peo2" class="cn.gunners.pojo.PeopleFactory" factory-method="newInstance"></bean>
      ```

# 四、给 Bean 的属性赋值(注入)

***set赋值(注入)走的是set方法***

```
public class People {
	private int id;
	private String name;
	private Set<String> sets;
	private List<String> list;
	private String[] strs;
	private Map<String, String> map;
	private Desk desk;
}
```

1. 基本数据类型及String

```
    <bean id="peo" class="cn.gunners.pojo.People">
   	<!-- 两种写法 -->
    	<property name="id" value="222"></property>
    	<property name="name">
    		<value>张三</value>
    	</property>
    </bean>
```

2. set集合

```
    <bean id="peo" class="cn.gunners.pojo.People">
          <property name="sets">
	    	<set>
	    		<value>1</value>
	    		<value>2</value>
	    		<value>3</value>
	    		<value>4</value>
	    	</set>
          </property>
    </bean>
```

3. list集合

```
    <bean id="peo" class="cn.gunners.pojo.People">
          <property name="list">
	    	<list>
	    		<value>1</value>
	    		<value>2</value>
	    		<value>3</value>
	    	</list>
	    </property>
    </bean>
	   <!-- 如果list中只有一个值，还可以这么写，以后list如果只有一个值经常这么写-->
		   <!-- <property name="list" value="1">
		   </property> -->
```

4. 数组

```
    <bean id="peo" class="cn.gunners.pojo.People">
          <property name="strs">
	    	<array>
		    	<value>1</value>
		    	<value>2</value>
		    	<value>3</value>
		</array>
	    </property>
    </bean>
	<!-- 如果数组中只有一个值，也可以跟list一样的写法-->
		<!-- <property name="strs" value="测试">
		</property> -->
```

5. map集合

```
    <bean id="peo" class="cn.gunners.pojo.People">
          <property name="map">
	    	<map>
		    	<entry key="a" value="b"></entry>
		    	<entry key="c" value="d"></entry>
		</map> 
	  </property>
    </bean>
```

6. properties类型

```
    <bean id="peo" class="cn.gunners.pojo.People">
          <property name="demo">
	    	<props>
		    	<prop key="key">value</prop>
		    	<prop key="key1">value1</prop>
		</props>
	  </property>
    </bean>
```

7. 实体类对象（也就是第五点：依赖注入）

```
        <bean id="desk" class="cn.gunners.pojo.Desk">
    	    <property name="id" value="1"></property>
    	    <property name="price" value="12"></property>
        </bean>
	<!-- 只需在文件中定义一个实体类的bean，与本bean的前后顺序无关 -->
	<property name="desk" ref="desk"></property>
```

# 五、DI（依赖注入）

1. 当一个类(A)中需要依赖另一个类(B)对象时,把 B 赋值给 A 的过程就叫做依赖注入

```
        <bean id="desk" class="cn.gunners.pojo.Desk">
    	    <property name="id" value="1"></property>
    	    <property name="price" value="12"></property>
        </bean>
	<!-- 只需在文件中定义一个实体类的bean，与本bean的前后顺序无关 -->
	<property name="desk" ref="desk"></property>
```

# 六、spring整合mybatis（简单整合）

1. applicationContext.xml文件：

```
        <bean id="dataSouce" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
		<property name="url" value="jdbc:mysql://193.112.78.108:3306/ssm"></property>
		<property name="username" value="root"></property>
		<property name="password" value="MYchen135136137"></property>
	</bean>

	<!-- 创建SqlSessionFactory对象 -->
	<bean id="factory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 数据库连接信息来源于dataSource -->
		<property name="dataSource" ref="dataSouce"></property>
	</bean>

	<!-- 扫描器：相当于mybatis.xml中mappers下的package标签，扫描cn.gunners.mapper包后会给对应接口创建对象 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 要扫描哪个包 -->
		<property name="basePackage" value="cn.gunners.mapper"></property>
		<!-- 和factory产生关系 -->
		<property name="sqlSessionFactory" ref="factory"></property>
	</bean>
	
        <!-- 管理AirportServiceImpl实现类，并为其mapper设值注入 -->
	<bean id="airportService" class="cn.gunners.service.impl.AirportServiceImpl">
		<property name="airportMapper" ref="airportMapper" ></property>
	</bean>
```
