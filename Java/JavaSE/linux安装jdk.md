### 手动解压JDK的压缩包，然后设置环境变量

### 通过yum源安装

- 查看yum库中都有哪些jdk版本(暂时只有openjdk)

	- yum search java|grep jdk

- 选择版本,进行安装

	- yum install java-1.7.0-openjdk

- 安装完之后，默认的安装目录是在: /usr/lib/jvm/java-1.7.0-openjdk-1.7.0.75.x86_64

- java -version验证

