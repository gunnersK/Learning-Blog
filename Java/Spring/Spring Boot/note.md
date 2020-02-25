- springboot项目启动命令

	- java -jar xx.jar

	- java -jar xx.jar --server.port=9090

		- 命令行中连续的两个减号--就是对application.properties中的属性值进行赋值的标识。

	- https://www.jianshu.com/p/43e4a5f50239

- Profile-多环境配置

	- 当应用程序需要部署到不同运行环境时，通常需要每次发布的时候都替换掉配置文件，使用profile可以解决这种麻烦

- 定时任务

	- 要使用定时功能时，需要在启动类上面加上@EnableScheduling
