# aop的实现有哪些

实现 AOP 的技术，主要分为两大类

1.静态代理 - 指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为编译时增强



2.动态代理 - 在运行时在内存中“临时”生成 AOP 动态代理类，因此也被称为运行时增强。
目前 Spring 中使用了两种动态代理库：

（1）JDK 动态代理
JDK 动态代理通过反射来接收被代理的类，并且要求被代理的类必须实现一个接口。JDK动态代理的
核心是 InvocationHandler 接口和 Proxy 类。


（2）CGLIB
	
如果目标类没有实现接口，那么 Spring AOP 会选择使用 CGLIB 来动态代理目标类。
当然，Spring 也支持配置，强制使用 CGLIB 动态代理。

CGLIB（Code Generation Library），
是一个代码生成的类库，可以在运行时动态的生成某个类的子类，注意，CGLIB 是通过继承的方式做的
动态代理，因此如果某个类被标记为 final ，那么它是无法使用 CGLIB 做动态代理的。


## Spring AOP and AspectJ AOP 有什么区别？

代理方式不同：
Spring AOP 基于动态代理方式实现。
AspectJ AOP 基于静态代理方式实现。

PointCut支持力度不同：

Spring AOP 仅支持方法级别的 PointCut 。
AspectJ AOP 提供了完全的 AOP 支持，它还支持属性级别的 PointCut 。





	
	
	
	
	