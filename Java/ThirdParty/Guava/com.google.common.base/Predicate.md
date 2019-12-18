-  <a href="#Predicate">Predicate</a>

- ## <a name="Predicate">Predicate</a>（https://blog.csdn.net/ghsau/article/details/51704892）

    - Predicate接口

        - 总体思路：里面有一个apply方法，和jdk的Predicate的test方法类似，都是去重写，给他一个具体实现，然后被调用

        - 使用：

           - 直接调用它的apply方法

              ```
                 Predicate<CharSequence> predicate = Predicates.containsPattern("^\\d+(\\.\\d{1,3})?$");
                  if(predicate.apply(money) && Doubles.tryParse(money) >= 0){
                      result = Boolean.TRUE;
                 }
              ```

           - 配合Predicates或Iterables等工具类一起用

              ```
                 ///////////////////////////////////Predicates/////////////////////////////////////
                 private Predicate<Examinee> juniorCollege = new Predicate<Examinee>() {
                     public boolean apply(Examinee examinee) {
                         return "大专".equals(examinee.getEducation()) && examinee.getExperience() >= 5;
                     }
                 };

                 private Predicate<Examinee> undergraduate = new Predicate<Examinee>() {
                     public boolean apply(Examinee examinee) {
                         return "本科".equals(examinee.getEducation()) && examinee.getExperience() >= 3;
                     }
                 };

                 private boolean canExam(Examinee examinee) {
                     return Predicates.or(juniorCollege, undergraduate, postgraduate).apply(examinee);
                 }

                 ///////////////////////////////////Iterables/////////////////////////////////////
                 List<Integer> intList = Lists.newArrayList(1, 2, 3, 5, 6);
                 Predicate<Integer> predicate = new Predicate<Integer>() {
                     public boolean apply(Integer value) {
                         return value != 3 && value != 5;
                     }
                 };
                 List<Integer> resultList = Lists.newArrayList(Iterables.filter(intList, predicate));
              ```
              
    - Predicates类              
    
        - and/or方法
        
            - 接收若干个Predicate接口对象，返回一个Predicate实例，再调用它的apply方法，得到布尔值，剩下的意会~
            
                ```
                    Predicates.or(juniorCollege, undergraduate, postgraduate).apply(examinee);
                ```
              
              

   
