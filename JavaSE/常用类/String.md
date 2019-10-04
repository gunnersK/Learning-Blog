# 一、常量池

   1. 字符串常量池是存在于方法区的
   
   2. 字符串对象的创建
   
      1. String str4 = new String(“abc”) 产生多少对象？
      
         1. 先在常量池中查找有没有"abc"对象，有就直接返回该对象，没有就新建一个
         
         2. 然后在堆中new一个String对象
         
      2. String str1 = new String("A"+"B") 会创建多少个对象？
      
         1. 字符串常量池："A"、"B"、"AB"三个
         
         2. 堆：new String("AB") 一个
   
     
