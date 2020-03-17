- 可在自动生成的serviceImpl中，用this.getBaseMapper()调用对应mapper的方法，而不需要实例化mapper

- 逆向工程指定表名，在策略配置StrategyConfig中调用strategy.setInclude("campaign_smart_puller")即可