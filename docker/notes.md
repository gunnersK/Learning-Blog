## 概念

- 镜像和容器：容器是镜像实例化而来，可以把镜像理解为类，容器就是类实例化后的对象

## 原理

- docker与VM的区别

	- 虚拟机是虚拟硬件和操作系统，面向硬件

	- docker只是虚拟环境，硬件还是用宿主机的，所以启动很快，是一种缩小、精细版的linux，面向软件

||docker|vm|
|:--|:--|---|
|OS|与宿主机共享OS|在宿主机OS上运行虚拟OS|
|存储大小|镜像小，便于存储和传输|镜像庞大|
|运行性能|几乎无额外性能损失|需要额外消耗OS的cpu和内存|
|移植性|轻便、灵活|笨重，与虚拟化技术耦合度高|

## 镜像操作

- 启动：systemctl start docker

- 开机自启动：systemctl enable docker

- 配置阿里云加速器：

	```
	vi /etc/docker/daemon.json
	insert
	{
	  "registry-mirrors: ["https://wp475gm2.mirror.aliyuncs.com"]"
	}
	sudo systemctl daemon-reload
	sudo systemctl restart docker
	```

- 查找镜像：docker search name

	- docker search -s 30 name 列出star数不小于30的镜像

	- docker search --no-trunc name 显示完整的镜像描述

- 拉取镜像：docker pull image_name

	- image_name后可加 ": 版本号"，没加则版本号默认为latest，拉取最新的版本

- 查看当前docker里所有镜像：docker images

- 删除镜像：

	- 删除单个：docker rmi image_id/name

	- 删除多个：docker rmi image_name1:tag image_name2:tag

	- 强制删除：docker rmi -f  

	- 删除全部：docker rmi -f $(docker images -qa)

	- 和pull相同，没指定版本号就默认为latest最新版本 

## 容器操作

- 新建并启动容器(需指定镜像)： docker run [option] image [command] [args...]

	```
	option:
	--name="为容器指定一个别名"
	-d: 后台运行容器
	-i/-t: i表示以交互模式运行容器，t表示为容器重新分配一个伪输入终端，-it通常同时使用
  ```
  
- 启动容器(不需指定镜像)：docker start 容器id/容器name 

- 重启容器：docker restart 容器id/容器name 

- （前台）启动交互式容器：docker run -it name

	- 返回带自身容器id编号（编号可理解为容器的地址）的命令行终端，用这个终端和docker交互

- （后台）启动守护式容器 docker run -d name

	- 容器运行的命令如果不是那些一直挂起的命令（如top，tail），启动后就会自动退出

- 查看容器：docker ps [option]

	```
	不加参数：列出当前正在运行的容器
	-a: 列出当前所有正在运行的容器、历史上运行过的容器
	-l: 列出最近创建的容器
	-n 数字: 列出最近创建的n个容器
	-q: 只显示容器编号
	```
	
- 停止运行中的容器

	```
	温柔停止：docker stop 容器id/容器name
	强制停止：docker kill 容器id/容器name
	```

- 退出容器

	```
	exit: 容器退出停止
	crtl+P+Q: 容器退出不停止	
	```

- 删除容器：docker rm 容器id   和删除镜像一样

- 容器日志：docker logs 容器id/容器name

	```
	-t: 加入时间戳
	-f: 跟随最新日志打印
	--tail 数字: 显示最后多少条	
	```

- 查看容器内部运行的进程：docker top 容器name/容器id

- 查看容器内部细节（json形式展示）：docker inspect 容器name/容器id

- 进入运行的容器并以命令行交互：docker exec/attach 容器id

	- exec	
	```
	不进入容器，在宿主机直接让容器执行命令，得到结果
	docker exec -it 容器id command 
	进入容器，打开容器的命令行终端执行命令
	docker exec -it 容器id /bin/sh(可省略)
	```

	- attach
	```
	直接进入容器，启动命令终端
	docker attach 容器id
	```

- 启动一个做端口映射的容器：docker run -d -p 8888:8080 tomcat

	- -d：后台运行
	- -p：主机端口映射到容器端口	主机端口:容器端口3

- 从容器拷贝文件到主机上

	```
	docker cp 容器id:容器内文件路径 目的主机路径
	```



