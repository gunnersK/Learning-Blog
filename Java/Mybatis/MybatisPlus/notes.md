- 可在自动生成的serviceImpl中，用this.getBaseMapper()调用对应mapper的方法，而不需要实例化mapper

- @TableField(exist = false)注解，可以打在自动生成的实体类中不是数据库的字段，插入的时候就会忽略这个字段，不会报错

