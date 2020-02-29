# 一、 springboot整合Mybatis

- 单数据源
   - 配置properties文件
      ```
         spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8
         spring.datasource.username=root
         spring.datasource.password=123456
         spring.datasource.driver-class-name=com.mysql.jdbc.Driver
         mybatis.mapper-locations=classpath:mapper/*.xml
      ```
   
   - 定义dao层，写mapper接口
   
   - 配置mapper.xml文件，在resources文件夹下建一个mapper文件夹放进去，并且指定namespace为dao层的包路径
   
   - 在mybatis的config类里面（如果有的话）和Application启动类打上@MapperScan注解扫描dao层的包路径
   
   - 如果使用springboot默认的数据源，则只需在application.properties文件中写配置信息，不用另外写Config类，因为它会自动配置数据源和mybatis相关配置，可以查看MybatisAutoConfiguration类源码，所以使用默认数据源时Config类不是必须的
   
- 多数据源  

   - 数据源的定义：数据源表示一个与数据库的连接（传统）或者表示很多与数据库的连接（使用数据库连接池）

	- 多数据源配置思路：

		- 看成四部分：   

			- 上面java的：Mapper接口，config配置类

			- 下面resources的：Mapper.xml，配置文件 

		- 各部分的关系如下：

			- Mapper接口 <- Mapper.xml <- config配置类 -> 配置文件  

			- 在配置文件中统一定义多个数据源的配置

			- 一个数据源一个config配置类，在config配置类中定义datasource的方法上用注解指定配置文件中的DataSource

			- 在config配置类中扫描Mapper.xml的路径

			- Mapper.xml的namespace定义Mapper接口的路径
   
   - 步骤
      - 配置yml文件
         ```
            spring:
                 datasource:
                    #主数据库
                    master:
                        driver-class-name: com.mysql.jdbc.Driver
                        jdbc-url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8
                        username: root
                        password: 123456
                    #从数据库
                    cluster:
                        driver-class-name: com.mysql.jdbc.Driver
                        jdbc-url: jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=utf-8
                        username: root
                        password: 123456
         ```
      
      - 写Config类
         - 主库配置类（主库要在配置类方法上面加@Primary注解，从库不用）
            ```
               @Configuration
               @MapperScan(basePackages = "com.example.project.dao.master",sqlSessionTemplateRef = "masterSqlSessionTemplate")
               public class MasterDataSourceConfig {

               //    创建DataSource
                   @Bean(name = "masterDataSource")
                   @ConfigurationProperties(prefix = "spring.datasource.master")
                   @Primary
                   public DataSource masterDataSource(){
                       return DataSourceBuilder.create().build();
                   }

               //    创建工厂
                   @Bean(name = "masterSqlSessionFactory")
                   @Primary
                   public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource")DataSource dataSource) throws Exception{
                       SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
                       bean.setDataSource(dataSource);
                       bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                               "classpath:mapper/master/*.xml"));
                       return bean.getObject();
                   }

               //    创建事务
                   @Bean(name = "masterTransactionManager")
                   @Primary
                   public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource")
                                                                                            DataSource dataSource){
                       return new DataSourceTransactionManager(dataSource);
                   }

               //    创建模板
                   @Bean(name = "masterSqlSessionTemplate")
                   @Primary
                   public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory")
                                                                      SqlSessionFactory sqlSessionFactory){
                       return new SqlSessionTemplate(sqlSessionFactory);
                   }
               }
            ```
            
         - 从库配置类
            ```
               @Configuration
               @MapperScan(basePackages = "com.example.project.dao.cluster",sqlSessionTemplateRef = "clusterSqlSessionTemplate")
               public class ClusterDataSourceConfig {
                   //    创建DataSource
                   @Bean(name = "clusterDataSource")
                   @ConfigurationProperties(prefix = "spring.datasource.cluster")
                   public DataSource masterDataSource(){
                       return DataSourceBuilder.create().build();
                   }

                   //    创建工厂
                   @Bean(name = "clusterSqlSessionFactory")
                   public SqlSessionFactory masterSqlSessionFactory(@Qualifier("clusterDataSource")DataSource dataSource) throws Exception{
                       SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
                       bean.setDataSource(dataSource);
                       bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                               "classpath:mapper/cluster/*.xml"));
                       return bean.getObject();
                   }

                   //    创建事务
                   @Bean(name = "clusterTransactionManager")
                   @Primary
                   public DataSourceTransactionManager masterTransactionManager(@Qualifier("clusterDataSource")
                                                                                        DataSource dataSource){
                       return new DataSourceTransactionManager(dataSource);
                   }

                   //    创建模板
                   @Bean(name = "clusterSqlSessionTemplate")
                   public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("clusterSqlSessionFactory")
                                                                              SqlSessionFactory sqlSessionFactory){
                       return new SqlSessionTemplate(sqlSessionFactory);
                   }
               }
            ```
         
         - 上面是针对使用默认数据源的配置，如果使用其他数据源，例如阿里的driud，只需做如下修改：
           
            - 将配置类中创建DataSource的方法中的DataSourceBuilder改为DuridDataSourceBuilder
         
      - 在resources文件夹下新建mapper文件夹，写mapper.xml文件，namespace定义为mapper包的路径         
