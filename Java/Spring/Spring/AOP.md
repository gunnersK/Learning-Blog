- https://cloud.tencent.com/developer/article/1397691

- 满足 pointcut 规则的 joinpoint 会被添加相应的 advice 操作

- 切面（Aspect）

	- 切面是一个横切关注点的模块化，一个切面能够包含同一个类型的不同增强方法，比如说事务处理和日志处理可以理解为两个切面。切面由切入点和通知组成，它既包含了横切逻辑的定义，也包括了切入点的定义。 Spring AOP就是负责实施切面的框架，它将切面所定义的横切逻辑织入到切面所指定的连接点中。

- 连接点（JoinPoint）

	- 简单来说，连接点就是被拦截到的程序执行点，因为Spring只支持方法类型的连接点，所以在Spring中连接点就是被拦截到的方法。

- 切入点（PointCut）

	- 切入点是对连接点进行拦截的条件定义。切入点表达式如何和连接点匹配是AOP的核心，Spring缺省使用AspectJ切入点语法。
	
	- 一般认为，所有的方法都可以认为是连接点，但是我们并不希望在所有的方法上都添加通知，而切入点的作用就是提供一组规则(使用 AspectJ pointcut expression language 来描述) 来匹配连接点，给满足规则的连接点添加通知。

- 通知（Advice）

	- 通知是指拦截到连接点之后要执行的代码，包括了“around”、“before”和“after”等不同类型的通知。Spring AOP框架以拦截器来实现通知模型，并维护一个以连接点为中心的拦截器链。 	

- 代码

	```
		@Component
        @Aspect
        @Slf4j
        public class SessionAspect {

            @Autowired
            private RedissonClient redissonClient;

            //com.gunners.epes.controller包下任意类的任意方法(..表示0或多个参数)
        //    @Pointcut("execution(* com.gunners.epes.controller.*.*(..))")
            @Pointcut("execution(* javax.servlet.http.HttpServlet.service(..))")
            public void aspect(){}

            /**
             * session续命处理
             * @param joinpoint
             */
            @Before("aspect()")
            public void handleSession(JoinPoint joinpoint){
                Object[] args = joinpoint.getArgs();
        //        MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
        //        System.out.println(StrUtil.format("class name: {}", joinpoint.getSignature().getDeclaringType()));
        //        System.out.println(StrUtil.format("method name: {}", joinpoint.getSignature()));
        //        String[] argList = methodSignature.getParameterNames();
                for(Object arg : args){
                    if(arg instanceof ServletRequest){
                        HttpServletRequest request = (HttpServletRequest) arg;
                        String sessionID = request.getSession().getId();
                        RMap map = redissonClient.getMap(sessionID);
                        if(map.remainTimeToLive() > 0){
                            map.expire(30, TimeUnit.MINUTES);
                        }
                        log.info(StrUtil.format("{} remain: {}", request.getSession().getId(), map.remainTimeToLive()));
                    }
                }
            }
      }
  ```