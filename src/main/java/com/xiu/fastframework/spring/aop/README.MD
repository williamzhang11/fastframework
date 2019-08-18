# 什么是spring aop

面向切面编程, 它与 OOP( Object-Oriented Programming, 面向对象编程) 相辅相成， 
提供了与 OOP 不同的抽象软件结构的视角.
在 AOP 中，以切面( Aspect )作为基本单元。
在 OOP 中，以类( Class )作为基本单元

## 应用场景：
日志，声明式事务，鉴权等

## 什么是 Aspect ？
Aspect 由 PointCut 和 Advice 组成。

Spring AOP 就是负责实施切面的框架，它将切面所定义的横切逻辑编织到切面所指定的连接点中

AOP 的工作重心在于如何将增强编织目标对象的连接点上, 这里包含两个工作:
如何通过 PointCut 和 Advice 定位到特定的 JoinPoint 上。
如何在 Advice 中编写切面代码。
可以简单地认为, 使用 @Aspect 注解的类就是切面

## 什么是 JoinPoint ?
JoinPoint ，切点，程序运行中的一些时间点, 例如：
一个方法的执行。
或者是一个异常的处理。
在 Spring AOP 中，JoinPoint 总是方法的执行点。

## 什么是 PointCut ？
PointCut 是匹配 JoinPoint 的条件 	

## 什么是 Advice ？

Advice ，通知。
特定 JoinPoint 处的 Aspect 所采取的动作称为 Advice 。
Spring AOP 使用一个 Advice 作为拦截器，在 JoinPoint “周围”维护一系列的拦截器。

Before - 这些类型的 Advice 在 JoinPoint 方法之前执行，并使用 @Before 注解标记进行配置。
After Returning - 这些类型的 Advice 在连接点方法正常执行后执行，并使用 @AfterReturning 注解标记进行配置。
After Throwing - 这些类型的 Advice 仅在 JoinPoint 方法通过抛出异常退出并使用 @AfterThrowing 注解标记配置时执行。
After Finally - 这些类型的 Advice 在连接点方法之后执行，无论方法退出是正常还是异常返回，并使用 @After 注解标记进行配置。
Around - 这些类型的 Advice 在连接点之前和之后执行，并使用 @Around 注解标记进行配置。



## 什么是 Target ？

Target ，织入 Advice 的目标对象。目标对象也被称为 Advised Object 。

因为 Spring AOP 使用运行时代理的方式来实现 Aspect ，因此 Advised Object 总是一个代理对象(Proxied Object) 。
注意, Advised Object 指的不是原来的对象，而是织入 Advice 后所产生的代理对象。
Advice + Target Object = Advised Object = Proxy 


















