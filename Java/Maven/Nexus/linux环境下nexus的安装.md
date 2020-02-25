# 一、nexus安装与介绍

   1. 最好新建一个文件夹(建议命名为nexus)，把nexus压缩包的东西解压到这个文件夹底下

   2. 解压出来有两个文件夹：nexus-2.11.4-01和sonatype-work
   
      1. nexus-2.11.4-01：这个文件夹里面有个nexus文件夹，里面是一个java编写的web项目，是本地私服对开发人员提供的一个网页面板
      
      2. sonatype-work：这个文件夹是工作目录，其实就是仓库。私服从中央仓库下载的东西就放在这个文件夹maven项目就从这个文件夹里下载东西

# 二、配置nexus

   1. 修改防火墙，开放nexus私服端口访问
   
      1. 查看防火墙，可以看到只开放了22端口   service iptables status   

      2. 因为nexus要用8081端口，所以还要给他在iptables文件里添加8081端口
      
         1. 编辑iptables文件：vi /etc/sysconfig/iptables
         
         2. 添加8081端口：复制粘贴这一行，把22改成8081即可
         
            -A INPUT -m state --state NEW -m tcp -p tcp --dport 22(8081) -j ACCEPT

      3. 然后重启防火墙：service iptables restart

      4. 再次查看防火墙：service iptable status  可以看到多了个8081端口
         
         然后就把防火墙关掉，自己的虚拟机都关掉防火墙  service iptables stop



# 三、启动nexus

   1. 切换到/usr/local/nexus/nexus-2.11.4-01/bin目录

   2. 启动nexus：./nexus start

   3. 查看nexus状态：./nexus status  可查看其pid号(进程号)

   4. 停止nexus：./nexus stop

   5. 访问nexus首页：IP地址+端口号(8081)+nexus



# 四、登录nexus

   1. 账号密码
   
      1. 默认登录账号：admin 
           
      2. 默认登录密码：admin123
      
         可在security--Users中修改用户信息

   2. 检查仓库
    
       1. 在Views/Repositories--Repositories 可查看仓库信息 
       
          1. 仓库类型Type简述
          
             1. group(仓库组)         nexus通过仓库组来管理各种仓库，访问仓库组等于访问他管理的各种仓库
             
             2. hosted(宿主仓库) 
                
                1. releases          发布版本
                   
                2. snapshots       快照版本
                   
                3. 3rd party 
                   
             3. proxy(代理仓库)  
             
                在Central仓库下面的configuration中的 download remote indexes 改为true 按save保存
                
                表示允许远程下载，把远程仓库中的所有文件都下载到本地
                
             4. virtual(虚拟仓库)      前三个较常用
             
             5. 注意
             
                1. 中央仓库更新，nexus私服的东西也跟着更新，不过不是自动更新，是触发更新，就是maven项目
                
                   访问私服需要下载某个项目的时候，私服发现这个索引不存在，就会自动更新。
                   
                2. 索引库就是jar包



# 五、配置maven的settings.xml文件

   1. 在settings文件中配置私服信息
   
      修改maven的setting.xml配置文件，在<service>标签中添加以下几个server信息
      
      1. tomcat的server信息，原来有的就不用加了
      
         ```
            <server>
                        <id>tomcat7</id>
                        <username>tomcat</username>
                        <password>tomcat</password>
            </server>
         ```
         
      2. nexus信息，id自定义，自己记住
      
         ```
            <server>
                        <id>nexus-releases</id>                            
                        <username>deployment</username>
                        <password>deployment123</password>
            </server>
         ```
         
      3. nexus信息，id自定义，自己记住
      
         ```
            <server>
                        <id>nexus-snapshots</id>
                        <username>deployment</username>
                        <password>deployment123</password>
            </server>
         ```
         
         username 和 password用nexus里面提供的，在User里面查看，可在User里面Add添加用户
         
      4. profile标签，详见maven的settings.xml文件
      
      5. settings.xml文件不能带中文字符，否则eclipse不能导入配置文件
      
         settings.xml文件只需配置一次，所有maven工程都可以通用        



#  六、配置maven工程的pom文件

   此pom文件在maven的顶级父工程中定义，所有子工程自动依赖导入
   
   ```
      <dependencies>
	  		<dependency>
	  			<groupId>org.springframework</groupId>
	  			<artifactId>spring-context</artifactId>
	  	 		<version>4.1.6.RELEASE</version>
	  		</dependency>
	  </dependencies>
	
		<distributionManagement>
      <!-- 这里的id要和maven的settings.xml文件中server标签的两个id对应上 -->
			<repository>
				<id>nexus-releases</id>
				<name>Nexus Release Repository</name>
				<url>http://192.168.1.105:8081/nexus/content/repositories/releases</url>
			</repository>
			<snapshotRepository>
				<id>nexus-snapshots</id>
				<name>Nexus Snapshot Repository</name>
				<url>http://192.168.1.105:8081/nexus/content/repositories/snapshots</url>
			</snapshotRepository>
		</distributionManagement>
	
	  <build>
	  		<plugins>
	  		
	  				<!-- 这是一个向本地私服上传的东西提供源代码信息的插件
	  				没有这个插件就只有提供jar包，字节码，没有源代码信息 -->
	  				<plugin>
	  					<groupId>org.apache.maven.plugins</groupId>
	  					<artifactId>maven-source-plugin</artifactId>
	  					<version>2.1.2</version>
	  					<executions>
	  						<execution>
	  							<id>attach-sources</id>
	  							<goals>
	  								<goal>jar</goal>
	  							</goals>
	  						</execution>
	  					</executions>
	  				</plugin>
	  		</plugins>
	  </build>
   ```
   
# 七、离线手动安装索引库

   1. 在4.2.1.3中，如果允许远程下载索引，由于文件比较大，下载速度会很慢，这时可以提前先把文件下载下来，然后再安装进去，步骤如下
   
      1. 下载以下三个文件放在一个文件夹zip(命名随意)
      
         1. nexus-maven-repository-index.gz   (http://repo.maven.apache.org/maven2/.index/最下面)
         
         2. nexus-maven-repository-index.properties  (http://repo.maven.apache.org/maven2/.index/最下面)
         
         3. indexer-cli-5.1.1  (百度自己找)

      2. 切换到zip目录，执行Java -jar indexer-cli-5.1.1.jar -u nexus-maven-repository-index.gz -d indexer
   
         文件较大，需要等待大约10分钟左右的解压过程，解压完成后停止nexus服务

      3. 删除\nexus\sonatype-work\nexus\indexer\central-ctx 中内容，把解压后indexer文件夹中内容复制到
      
         /nexus/sonatype-work\nexus\indexer\central-ctx 中，启动nexus
      
   2. 索引安装完之后就Run as--install
                       
# 八、发布本地工程到nexus

   1. 只能发布到宿主仓库(Type为hosted的仓库)
   
      1. 如果版本号以SNAPSHOT结尾的就会发布到快照库

      2. 不以SNAPSHOT结尾的发布到Releases库

      3. 第三方库必须手工上传，无法通代码发布，在3rd party底下的Artifact Upload那里挨个上传

   2. 以deploy(部署)命令run maven项目
         
      1. 注意：同一个版本的项目在同一个库(比如Releases)中只能发布一次，多次发布需要修改nexus配置
      
         在对应库的Configuration中设置Allow Redeploy

   3. install作用

      1. 扫描pom文件里的所有依赖，如果依赖在本地仓库不存在，会访问私服或者镜像仓库，然后再下载

      2. 把jar包安装到本地仓库
          





























