# ConcurrentDemo
a simple ConcurrentDemo
on 2016.1.23

#2016.1.23 push
#一个简单的饭店消费的多线程控制，不适用显示锁进行操作
#角色：厨师，顾客，送餐人员
#主要技术：
#信号量:Semaphore
#阻塞队列:LinkedBlockingQueue
#线程池:ExecutorService
#
#实现过程：
#CustomerFactory模仿顾客流，在一定时间内产生顾客。
#饭店内座位资源有限，用信号量控制。
#DispatcherController作为饭店的核心分配器，主要作用是动态调整厨师及送餐人员的数量。
#阻塞队列有3个，顾客点完餐，菜单放入A，厨师从A中取出，做好放入B，送餐人员从B中取出，送给顾客，任务完成放入C。
#顾客收到所以的菜之后，完成消费，离开饭店。
#下一位顾客进入。
#
#其余细节不再赘述