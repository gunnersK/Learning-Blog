1. applicationContext.xml已经配置了注解扫描，为什么springmvc.xml还要加注解驱动？
   
   因为前者的注解扫描不针对controller层，所以当controller层有注解的时候要在springmvc.xml配置注解驱动，他是针对controller层的注解驱动
   
2. 为什么要使用<mvc:resources mapping="/js/**" location="/js/" />配置对静态资源的访问？

   因为在web.xml中，配置的DispatcherServlet的<url-pattern>/</url-pattern>拦截的是所有资源，包括静态资源，如果不配置resources直接访问静态资源的话他会跑到controller去，所以要在springmvc.xml里面配置这个，就能直接访问到静态资源了
   
3. InternalResourceViewResolver视图解析器自动加前后缀。是什么情况下才会自动加？
