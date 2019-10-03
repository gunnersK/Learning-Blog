# 一、ReentrantLock

  1. ReentrantLock和synchronized的理解必须手动释放锁，不像synchronized一样(jvm会帮你自动释放锁)一般在finally里面释放锁

  2. 默认synchronized都是不公平锁
