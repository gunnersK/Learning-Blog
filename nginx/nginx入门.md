# nginx主要就是学习配置文件

### 一、nginx的操作

   1. 开启，关闭和重启在sbin路径下操作

      1. 开启：./nginx

      2. 关闭：./nginx -s stop

      3. 重启： ./nginx -s relaod

   2. 以上操作也可以不进入sbin目录，直接打sbin目录的全路径之后接上开启关闭重启的代码

      1. 即：/usr/local/nginx/sbin/nginx -s ....

### 二、nginx.conf配置文件总体讲解（待完善）

   1. worker_processes   工作进程，根据cpu核心和线程设置

   2. 文件里是K-V值，每个value写完后要加分号
   
   3. 最核心的是虚拟主机（server{}）的配置：
   
      1. listen  管监听的端口
      
      2. server_name  自定义域名
      
      3. location{}  配置资源路径
    
### 三、location配置

   先来段代码
   
   ```
      location / {
            root   html;
            index  index.html index.htm;
        }
   ```
   
   1. location后面要定义正则表达式，表示请求路径匹配所定义的正则表达式则走这个location，实际上是对url做一次简单的过滤
   
      1. 上面代码location后面的 / 代表匹配所有形式的请求路径
      
      2. 下面代码表示匹配以.jsp结尾的请求

      ```
         location ~ \.jsp$ {
                proxy_pass http://192.168.182.3:8080;
                proxy_set_header X-real-ip $remote_addr;
       }
      ```
      3. 下面代码表示匹配所有带test的请求
      
      ```
         location ~ test {
           root bhz.com;
           index index.html;
        }
      ```
   
   2. root是定义根文件夹，走这个location的请求就会到root后面定义的路径下去找资源
   
   3. index是默认欢迎项
   
### 四、反向代理

   1. 在location中定义 proxy_pass 值
   
      1. proxy_pass后面定义要跳转的地址   
     
         例如：proxy_pass http://192.168.182.3:8080;
        
         这里的地址是一个tomcat。如果在jsp里直接通过request域拿请求端的ip，则拿到的实际是nginx的IP地址。因为nginx做了代理
         
         可以在location中加这么一句拿到真实请求端的ip
         
         ```
            proxy_set_header X-real-ip $remote_addr;
         ```
         表示nginx在请求头(header)定义一个名为X-real-ip的变量，保存真实请求端的ip，
         
         然后转发给目标地址，这样目标地址的主机就能在jsp中在request域里面通过这个变量拿到真实ip
         
         <%=request.getHeader("X-real-ip") %>
         
### 五、负载均衡（这只是负载均衡最简单的实现）

   1. 在http{}下，定义一个与server{}同级的upstream{}，写法：
          
      ```
         upstream myapp {
              server 192.168.182.3:8080 weight=1 max_fails=2 fail_timeout=30s;
              server 192.168.182.5:8080 weight=1 max_fails=2 fail_timeout=30s;
         }
      ```
      1. upstream后面定义一个名字给location引用
      
      2. upstream{}里面定义若干个负载均衡需要的主机地址
      
         1. weight表示这个主机地址被分配到的请求的权重
         
         2. max_fails表示被认为宕机的连接失败的次数
         
         3. fail_timeout表示超过多久连接失败
         
   2. location{}配置
   
      1. 在location中定义 proxy_pass 值，值写upstream 名字{} 起的名字
      
      
         
