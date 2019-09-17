# spring cloud是什么

Spring Cloud 是构建在 Spring Boot 基础之上，用于快速构建分布式系统的通用模式的工具集。
或者说，换成大家更为熟知的，用于构建微服务的技术栈。

# Spring Cloud 和 Spring Boot 的区别和关系？

1.Spring Boot 专注于快速方便的开发单个个体微服务。

2.Spring Cloud 是关注全局的微服务协调整理治理框架以及一整套的落地解决方案，
它将 Spring Boot 开发的一个个单体微服务整合并管理起来，为各个微服务之间提供：
配置管理，服务发现，断路器，路由，微代理，事件总线等的集成服务。

3.Spring Boot 可以离开 Spring Cloud 独立使用，但是 Spring Cloud 
离不开 Spring Boot ，属于依赖的关系。
总结：
Spring Boot ，专注于快速，方便的开发单个微服务个体。
Spring Cloud ，关注全局的服务治理框架。


# Spring Cloud 有哪些组件？

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/springcloud.JPG)

最为熟知的，可能就是 Spring Cloud Netflix ，它是 Netflix 公司基于它们
自己的 Eureka、Hystrix、Zuul、Ribbon 等组件，构建的一个 Spring Cloud 实现技术栈

# spring cloud与dubbo
Dubbo 是一个分布式服务框架，致力于提供高性能和透明化的 RPC 远程服务调用方案，以及 SOA 服务治理方案。简单的说，Dubbo 就是个服务框架，说白了就是个远程服务调用的分布式框架。

Spring Cloud 基于 Spring Boot，为微服务体系开发中的架构问题，提供了一整套的解决方案——服务注册与发现，服务消费，服务保护与熔断，网关，分布式调用追踪，分布式配置管理等。






