- tomcat是一种web服务器，也可以称作运行在服务器（物理意义上的计算机）上的一种软件包，用来装载javaweb程序，可以称它为web容器，你的jsp/servlet程序需要运行在Web容器上
- web容器可叫Servlet容器，用来和servlet进行交互，管理servlet生命周期，把url映射到特定servlet，综合来看，Servlet容器就是用来运行你的Servlet和维护它的生命周期的运行环境。
- servlet是能够根据请求动态生成响应内容的组件，实质上是javax.servlet包下的接口
- web容器启动时，会一次性加载所有应用，并为每个应用创建ServletContext，而一个Servlet对象可以同时为多个线程（请求）服务
- Servlet容器包含在web服务器中，web服务器监听特定端口的请求，当接收到一个请求时，Servlet容器会创建一个HttpServletRequest和HttpServletResponse对象
- 客户端第一次访问应用，Servlet容器会创建session，生成一个long类型的JSESSIONID，并保存在服务器内存中，然后Servlet容器会在http响应里设置一个值是JSESSIONID的cookie
- 客户端在这之后的请求都要把这个cookie带上，Servlet容器会用JSESSIONID检测请求头里面的cookie，并用该cookie的值去服务器内存里获取相关的session
- https://blog.csdn.net/qq_36066370/article/details/59542413https://blog.csdn.net/qq_36066370/article/details/59542413
- nginx和tomcat的的区别

	- nginx是http服务器，只能接受静态资源的访问

	- tomcat是应用程序服务器，也可以认为是http服务器，可以动态生成资源内容，也可以接受静态资源的访问

	- 通常都是nginx和tomcat配合一起使用：

		- 动静态（前后端）资源分离：通过nginx反向代理的功能，将动态资源的请求交给tomcat处理，静态资源的请求由nginx自己返回相应，减轻tomcat压力
- 负载均衡：一个tomcat不足以处理，可以启动多个tomcat进行水平扩展，通过nginx负载均衡的功能将请求通过算法分发给不同的tomcat处理
- 过滤器和拦截器的区别
  - 过滤器是servlet规范定义的，是servlet容器支持的，只能用于web程序中，只能在servlet前后起作用
  - 拦截器属于spring框架的一个组件，在spring容器内，既可以用在web程序，也可以用在Application、Swing程序中，能够深入到方法前后、异常抛出前后，具有更好的弹性，在spring框架的程序中，要优先使用拦截器