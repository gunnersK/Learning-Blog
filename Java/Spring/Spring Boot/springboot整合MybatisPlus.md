- ### 整合步骤

	- 先用生成器的代码生成各层文件

	- 在yml配置文件定义数据源

	- 再写config类，在config类上用MapperScan注解指定mapper路径，就行了

		- mapper.xml文件生成的时候就会自动定义namespace的值，值是mapper包的路径

