# zuul与spring cloud gateway比较

## 1.开源组织
zuul 第一代网关，Netflix开发

spring cloud gateway 第二代网关，Spring Cloud 微服务平台的一个子项目，目标是替代 Netflix Zuul


## 2.底层实现

Zuul构建于 Servlet 2.5，兼容 3.x，使用的是阻塞式的 API，不支持长连接

Spring Cloud Gateway构建于 Spring 5+，基于 Spring Boot 2.x 响应式的、非阻塞式的 API,支持长连接

## 3.性能表现

Spring Cloud Gateway更好，目标是替代 Netflix Zuul

原理：

客户端向 Spring Cloud Gateway 发出请求，如果请求与网关程序定义的路由匹配，则该请求就会被发送到网关 Web 处理程序，此时处理程序运行特定的请求过滤器链。

过滤器之间用虚线分开的原因是过滤器可能会在发送代理请求的前后执行逻辑。所有 pre 过滤器逻辑先执行，然后执行代理请求；代理请求完成后，执行 post 过滤器逻辑。




![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/springcloud.png)


