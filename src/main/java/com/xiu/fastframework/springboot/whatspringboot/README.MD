# Springboot是什么?

通过 Spring Boot ，可以快速配置 Spring 项目，引入各种 Spring MVC、Spring Transaction、
Spring AOP、MyBatis 等等框架，而无需不断重复编写繁重的 Spring 配置，降低了 Spring 的使用成本。

Spring Boot 提供了各种 Starter 启动器，提供标准化的默认配置

spring-boot-starter-web 启动器，可以快速配置 Spring MVC 。
mybatis-spring-boot-starter 启动器，可以快速配置 MyBatis 。

并且，Spring Boot 基本已经一统 Java 项目的开发，大量的开源项目都实现了其的 Starter 启动器。例如：

incubator-dubbo-spring-boot-project 启动器，可以快速配置 Dubbo 。
rocketmq-spring-boot-starter 启动器，可以快速配置 RocketMQ 。

## 优点

1、使【编码】变简单。
2、使【配置】变简单。
3、使【部署】变简单。
4、使【监控】变简单。

## 缺点

Spring Boot 的缺点主要是，因为自动配置 Spring Bean 的功能，我们可能无法知道，哪些 Bean 被进行创建了




# Spring Boot、Spring MVC 和 Spring 有什么区别


Spring 的完整名字，应该是 Spring Framework 。它提供了多个模块，Spring IoC、Spring AOP、
Spring MVC 等等。所以，Spring MVC 是 Spring Framework 众多模块中的一个。

而 Spring Boot 是构造在 Spring Framework 之上的 Boot 启动器，旨在更容易的配置一个 Spring 项目。


spring-boot-starter-web ：提供 Spring MVC + 内嵌的 Tomcat 。
spring-boot-starter-data-jpa ：提供 Spring JPA + Hibernate 。
spring-boot-starter-data-redis ：提供 Redis 。
mybatis-spring-boot-starter ：提供 MyBatis 。





