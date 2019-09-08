# 声明式调用

在 Spring Cloud 中，目前使用的声明式调用组件，只有

spring-cloud-openfeign ，基于 Feign 实现

Feign 并非 Netflix 团队开发的组件

Feign 的主要目标是将Java Http 客户端变得简单。

## Feign 实现原理？

Feign的一个关键机制就是使用了动态代理.

(1)首先，如果你对某个接口定义了 @FeignClient 注解，Feign 就会针对这个接口创建一个动态代理

(2)接着你要是调用那个接口，本质就是会调用 Feign 创建的动态代理，这是核心中的核心

(3)Feign的动态代理会根据你在接口上的 @RequestMapping 等注解，来动态构造出你要请求的服务的地址

(4)最后针对这个地址，发起请求、解析响应
![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/feign.JPG)

## Feign 和 Ribbon 的区别？

Ribbon 和 Feign 都是使用于调用用其余服务的，不过方式不同

(1)启动类用的注解不同。

Ribbon 使用的是 @RibbonClient 。

Feign 使用的是 @EnableFeignClients

(2)服务的指定位置不同。

Ribbon 是在 @RibbonClient 注解上设置。

Feign 则是在定义声明方法的接口中用 @FeignClient 注解上设置


(3)调使用方式不同。

Ribbon 需要自己构建 Http 请求，模拟 Http 请求而后用 RestTemplate 发送给其余服务，步骤相当繁琐。

Feign 采使用接口的方式，将需要调使用的其余服务的方法定义成声明方法就可，不需要自己构建 Http 请求。不过要注意的是声明方法的注解、方法签名要和提供服务的方法完全一致。

## Feign 是怎么和 Ribbon、Eureka 整合的？

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/feignribbon.JPG)

(1)首先，用户调用 Feign 创建的动态代理

(2)然后，Feign 调用 Ribbon 发起调用流程。

首先，Ribbon 会从 Eureka Client 里获取到对应的服务列表。
然后，Ribbon 使用负载均衡算法获得使用的服务。

(3)最后，Ribbon 调用 Feign ，而 Feign 调用 HTTP 库最终调用使用的服务





























