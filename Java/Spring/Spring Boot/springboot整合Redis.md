# 一、 springboot整合Redis

- springboot序列化对象
  
    - RedisTemplate可以存储对象，但要实现Serializable接口，以二进制数组方式存储，内容没有可读性

- springboot默认使用Lettuce做redis客户端
