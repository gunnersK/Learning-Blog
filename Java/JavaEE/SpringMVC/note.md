- applicationContext.xml已经配置了注解扫描，为什么springmvc.xml还要加注解驱动？
   
    因为前者的注解扫描不针对controller层，所以当controller层有注解的时候要在springmvc.xml配置注解驱动，他是针对controller层的注解驱动
   
- 为什么要使用<mvc:resources mapping="/js/\**" location="/js/" />配置对静态资源的访问？

    - 在web.xml中，配置的DispatcherServlet的<url-pattern>/</url-pattern>拦截的是所有资源，包括静态资源，如果不配置resources直接访问静态资源的话他会跑到controller去，所以要在springmvc.xml里面配置这个，就能直接访问到静态资源了
   
- InternalResourceViewResolver视图解析器自动加前后缀。是什么情况下才会自动加？

- springmvc的xml文件中用<context:component-scan base-package="com.yjb"/>扫controller包时，不用加通配符，直接写包名就好，
  例如com.yjb.*或com.yjb.*Controller之类的
  
- 通过注解对请求参数进行验证：

    - 验证类注解
    
        - javax.validation.constraints包下有一些自带的注解，可对其修饰的变量进行验证，常见的有：
        
            - @Null -> 验证对象是否为null
            
            - @NotNull -> 验证对象是否不为null, 无法查检长度为0的字符串
            
            - @NotBlank -> 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格
            
            - @NotEmpty -> 检查约束元素是否为NULL或者是EMPTY
            
            ....等等（ https://www.jianshu.com/p/71f70766d165 ）
            
        - 也可以自定义注解类，自定义验证逻辑，注解类需要绑定指定的实现javax.validation包下ConstraintValidator接口的类，验证逻辑就在重写的isValid方法中写       
        
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
    
        - 通过@Validated注解去修饰带有验证类注解的类的参数，配合BindingResult，对请求参数进行验证
        
        - BindingResult要紧跟在被@Validated注解的参数后面，他们是一一对应的，如果有多个@Validated，那么每个@Validated后面跟着的BindingResult就是这个@Validated的结果，顺序不能乱
    
        ```
            public Response create(@Validated({CreatedGroup.class}) @RequestBody AffPayoutReq req,BindingResult result)
        ```

        ```
            public class AffPayoutReqBase {

                @NotBlank   //自带注解
                @Numeric    //自定义注解，需要自己实现验证逻辑
                private String offer_id;
        ```
        
            
        
                
                
                
                
 
