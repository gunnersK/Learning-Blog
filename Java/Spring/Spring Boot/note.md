- springboot项目启动命令

  - java -jar xx.jar

  - java -jar xx.jar --server.port=9090

  	- 命令行中连续的两个减号--就是对application.properties中的属性值进行赋值的标识。

  - 可以定义日志信息输出到指定文件

  - https://www.jianshu.com/p/43e4a5f50239

  - 后台启动

    ```shell
    nohup java -jar epes-0.0.1-SNAPSHOT.jar &
    ```

    

- Profile-多环境配置

	- 当应用程序需要部署到不同运行环境时，通常需要每次发布的时候都替换掉配置文件，使用profile可以解决这种麻烦

	- 在SpringBoot开发环境中，配置文件名称需要满足 application-{profile}.properties格式。其中，{profile}表示环境名称
	- 启动时制定环境
		```
		--spring.profiles.active=env
		```

- 单元测试指定环境运行

	```
	VM options: -Dspring.profiles.active=env
	```
	
	
	
- 定时任务

	- 要使用定时功能时，需要在启动类上面加上@EnableScheduling

