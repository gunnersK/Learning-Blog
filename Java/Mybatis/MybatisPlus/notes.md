<<<<<<< HEAD
- 可在自动生成的serviceImpl中，用this.getBaseMapper()调用对应mapper的方法，而不需要实例化mapper

- 逆向工程指定表名，在策略配置StrategyConfig中调用strategy.setInclude("campaign_smart_puller")即可
=======
- 可在自动生成的serviceImpl中，用this.getBaseMapper()调用对应mapper的方法，而不需要实例化mapper

- @TableField(exist = false)注解，可以打在自动生成的实体类中不是数据库的字段，插入的时候就会忽略这个字段，不会报错

>>>>>>> 7307e71a5631041423e0117458bf75740ae24c13
