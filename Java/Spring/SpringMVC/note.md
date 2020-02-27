- ### 关于三层架构和MVC模型

	- 三层架构：一般B/S架构系统会分为表现层、业务层、持久层

		- 表现层：即web层，接收http请求，完成http响应，依赖业务层，一般采用MVC模型设计

		- 业务层： 即service层，负责逻辑处理，事务应该放在业务层来处理

		- 持久层：即dao层，和数据库交互

	- MVC模型：model模型，view视图，controller控制器

		- model：就是javabean，封装数据

		- view：jsp，用来展示数据，view是依据model创建的

		- controller：处理用户交互，程序逻辑。根据用户的请求把数据填充到model，传给业务层；或者将业务层返回的数据模型创建view返回给用户

- ### SpringMVC组件和执行流程

	- 几个关键的组件

		- DispatcherServlet - 前端控制器

		- HandlerMapping - 处理器映射器

		- HandlerAdapter - 处理器适配器

		- Handler(Controller) - 处理器（需要工程师开发）

		- ViewResolver - 视图解析器

		- View - 视图（需要工程师开发）

	- 大致执行流程

		- DispatcherServlet接收请求，调用HandlerMapping

		- HandlerMapping根据请求的url查找到具体的Handler，返回给DispatcherServlet，可以根据xml配置、注解进行查找

		- DispatcherServlet调用HandlerAdapter去调用（执行）具体的Handler

		- Handler执行完返回ModelAndView，HandlerAdapter将ModelAndView返回给DispatcherServlet

		- DispatcherServlet将ModelAndView传给ViewResolver

		- ViewReslover解析后返回具体View给DispatcherServlet

		- DispatcherServlet根据View进行渲染视图（即将模型数据填充至视图中） ，响应用户

	- SpringMVC工作机制

		- 在容器初始化时会建立所有url和controller的对应关系,保存到Map<url,controller>中.tomcat启动时会通知spring初始化容器(加载bean的定义信息和初始化所有单例bean),然后springmvc会遍历容器中的bean,获取每一个controller中的所有方法访问的url,然后将url和controller保存到一个Map中;
		这样就可以根据request快速定位到controller,因为最终处理request的是controller中的方法,Map中只保留了url和controller中的对应关系,所以要根据request的url进一步确认controller中的method,这一步工作的原理就是拼接controller的url(controller上@RequestMapping的值)和方法的url(method上@RequestMapping的值),与request的url进行匹配,找到匹配的那个方法;
		确定处理请求的method后,接下来的任务就是参数绑定,把request中参数绑定到方法的形式参数上,这一步是整个请求处理过程中最复杂的一个步骤。springmvc提供了两种request参数与方法形参的绑定方法:
		① 通过注解进行绑定,@RequestParam
		②通过参数名称进行绑定.使用注解进行绑定,我们只要在方法参数前面声明@RequestParam(“a”),就可以将request中参数a的值绑定到方法的该参数上.使用参数名称进行绑定的前提是必须要获取方法中参数的名称,Java反射只提供了获取方法的参数的类型,并没有提供获取参数名称的方法.springmvc解决这个问题的方法是用asm框架读取字节码文件,来获取方法的参数名称.asm框架是一个字节码操作框架,关于asm更多介绍可以参考它的官网.个人建议,使用注解来完成参数绑定,这样就可以省去asm框架的读取字节码的操作
	
- ### 关于@RequestParam和@RequestBody

	- @RequestParam接收的参数是来自requestHeader中，即请求头。通常用于GET请求

	- @RequestBody接收的参数是来自requestBody中，即请求体一般用于处理非 Content-Type: application/x-www-form-urlencoded编码格式的数据，比如：application/json、application/xml，通常用于接收POST、DELETE等类型的请求数据，解析请求体中的json数据，可用于处理批量数据

- asm框架是一个字节码操作框架

- applicationContext.xml已经配置了注解扫描，为什么springmvc.xml还要加注解驱动？
  
    因为前者的注解扫描不针对controller层，所以当controller层有注解的时候要在springmvc.xml配置注解驱动，他是针对controller层的注解驱动
   
- 为什么要使用<mvc:resources mapping="/js/\**" location="/js/" />配置对静态资源的访问？

    - 在web.xml中，配置的DispatcherServlet的<url-pattern>/</url-pattern>拦截的是所有资源，包括静态资源，如果不配置resources直接访问静态资源的话他会跑到controller去，所以要在springmvc.xml里面配置这个，就能直接访问到静态资源了
   
- InternalResourceViewResolver视图解析器自动加前后缀。是什么情况下才会自动加？

- springmvc的xml文件中用<context:component-scan base-package="com.yjb"/>扫controller包时，不用加通配符，直接写包名就好，
  例如com.yjb.*或com.yjb.*Controller之类的
  
- 通过注解对请求参数进行验证：

    - 验证类注解
    
        - javax.validation.constraints包下自带的注解，可对其修饰的变量进行验证，常见的有：
        
            - @Null -> 验证对象是否为null
            
            - @NotNull -> 验证对象是否不为null, 无法查检长度为0的字符串
            
            - @NotBlank -> 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格
            
            - @NotEmpty -> 检查约束元素是否为NULL或者是EMPTY
            
            ....等等（ https://www.jianshu.com/p/71f70766d165 ）
            
        - 自定义验证类注解类
        
            - 自定义验证逻辑，注解类需要绑定指定的实现javax.validation包下ConstraintValidator接口的类，验证逻辑就在重写的isValid方法中写       
        
            - 自定义注解类

                ```
                    @Target({ElementType.FIELD, ElementType.METHOD})
                    @Retention(RetentionPolicy.RUNTIME)
                    @Constraint(validatedBy={NumericValidator.class})
                    public @interface Numeric{

                     String message() default "{com.offerslook.validator.constraints.Numeric.message}";

                     Class<?>[] groups()  default { };

                      Class<? extends Payload>[] payload() default { };

                    }
                ```
                
                - 可以在message()方法定义验证未通过时默认的message，也可以在实体类属性用到注解的时候再定义message
    
            - 实现ConstraintValidator接口的类
    
                ```
                    public class NumericValidator implements ConstraintValidator<Numeric, String> {
                        public void initialize(Numeric numeric) {
                        }
    
                        //在这里写自定义验证逻辑
                        public boolean isValid(String value, ConstraintValidatorContext context) {
                            if (null == value){
                                return true;
                            }
                            return StringUtils.isNumeric(value);
                        }

                    }
                ```
        
    - @Validated & BindingResult
    
        - 通过@Validated注解去修饰带有验证类注解的实体类的参数，配合BindingResult，对请求参数进行验证
        
        - BindingResult要紧跟在被@Validated注解的参数后面，他们是一一对应的，如果有多个@Validated，那么每个@Validated后面跟着的BindingResult就是这个@Validated的结果，顺序不能乱
        
        - result.hasErrors()判断是否验证通过，如果未通过，返回在实体类属性注解设置的自定义message，没有就返回验证注解类默认的message
    
        ```
            public Response create(@Validated({CreatedGroup.class}) @RequestBody AffPayoutReq req,BindingResult result)
        ```
    
        ```
            public class AffPayoutReqBase {
    
                @NotBlank   //自带注解
                @Numeric(message="....")    //自定义注解，需要自己实现验证逻辑，可以自定义message
                private String offer_id;
        ```


​           

