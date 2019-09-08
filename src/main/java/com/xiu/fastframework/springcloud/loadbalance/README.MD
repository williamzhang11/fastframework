# 负载均衡

在 Spring Cloud 中，能够使用的负载均衡如下：

（1）spring-cloud-netflix-ribbon ，基于 Ribbon 实现
（2）spring-cloud-loadbalancer ，提供简单的负载均衡功能

以上的实现，都是基于 spring-cloud-commons 的 loadbalancer 的 ServiceInstanceChooser
 接口，实现统一的服务的选择。并且，负载均衡组件在选择需要调用的服务之后，还提供调用该服务的功能
 
 ## 为什么要负载均衡？
 随着业务的发展，单台服务无法支持访问的需要，于是搭建多个服务集群。那么随之要解决的是，每次请求，调用哪个服务
 也就是需要负载均衡
 
 目前负载均衡有两种模式：
 
服务端模式
客户端模式

在 Spring Cloud 中使用客户端模式。

## 服务端负载均衡

负载均衡是处理高并发，缓解网络压力和进行服务端扩容的重要手段，一般情况下我们所说的负载均衡通常都是
指服务端负载均衡，服务端负载均衡又分为两种，一种是硬件负载均衡，还有一种是软件负载均衡。

硬件负载均衡主要通过在服务器节点之间安装专门用于负载均衡的设备
软件负载均衡则主要是在服务器上安装一些具有负载均衡功能的软件来完成请求分发进而实现负载均衡，常见的就是Nginx。

无论是硬件负载均衡还是软件负载均衡，它的工作原理都不外乎下面这张图

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/loadbalance1.JPG)

无论是硬件负载均衡还是软件负载均衡都会维护一个可用的服务端清单，然后通过心跳机制来删除故障的服务端节点
以保证清单中都是可以正常访问的服务端节点，此时当客户端的请求到达负载均衡服务器时，负载均衡服务器按照
某种配置好的规则从可用服务端清单中选出一台服务器去处理客户端的请求。这就是服务端负载均衡。

## 客户端负载均衡

客户端负载均衡和服务端负载均衡最大的区别在于服务清单所存储的位置。在客户端负载均衡中，所有的客户端节点
都有一份自己要访问的服务端清单，这些清单统统都是从Eureka服务注册中心获取的。在Spring Cloud中我
们如果想要使用客户端负载均衡，方法很简单，开启@LoadBalanced注解即可，这样客户端在发起请求的时候会先
自行选择一个服务端，向该服务端发起请求，从而实现负载均衡。


## 负载平衡的意义什么？

在计算中，负载平衡可以改善跨计算机，计算机集群，网络链接，中央处理单元或磁盘驱动器等多种计算资源的工作
负载分布。负载平衡旨在优化资源使用，最大化吞吐量，最小化响应时间并避免任何单一资源的过载。使用多个组件进行
负载平衡而不是单个组件可能会通过冗余来提高可靠性和可用性。负载平衡通常涉及专用软件或硬件，例如多层交换机或
域名系统服务器进程。

## Ribbon

主要提供客户侧的软件负载均衡算法

Spring Cloud Ribbon 是一个基于 HTTP 和 TCP 的客户端负载均衡工具，它基于 Netflix Ribbon 实现。通过 Spring Cloud 的封装，可以让我们轻松地将面向服务的 REST 模版请求自动转换成客户端负载均衡的服务调用。

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/ribbonloadbalance.JPG)

上图，关键点就是将外界的 rest 调用，根据负载均衡策略转换为微服务调用

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/ribbontheory.jpg)

首先，Ribbon 会从 Eureka Client 里获取到对应的服务列表。
然后，Ribbon 使用负载均衡算法获得使用的服务。
最后，Ribbon 调用对应的服务

## Ribbon负载均衡的配置

Ribbon作为后端负载均衡器，比Nginx更注重的是承担并发而不是请求分发

默认的负载均衡算法是 Round Robin 算法，顺序向下轮询。

## Ribbon 重试机制

在集群环境中，多个节点来提供服务，难免会有某个节点故障

用Nginx做负载均衡的时候，如果你的应用是无状态的，是可以滚动发布的，也就是一台台去重启应用

这样对用户其实是影响比较小的，因为Nginx在转发请求失败后会重新将该请求转发到别的实例上去

我们在用Zuul构建API网关时，发现eureka中的服务挂了一个, 由于ribbon的默认负载机制是轮询
如果一个服务下线，eureka并不会马上清除信息，不像zookeeper那样基于watcher可以实时监控下线
所以在eureka中挂掉的服务没有被清空信息时，zuul会转发到已经故障的机器，导致请求失败
当然这个不会持续很久， 当连续失败hystrix就会处于打开状态，就算有一次失败，我觉得也是不能容忍的
所以我们需要有像Nginx中那样重试的机制来保证请求的成功，哪怕延迟个几百毫秒响应给使用方
在Zuul中我们可以配置ribbon的重试机制来实现，必须依赖一个 Spring Retry

```
<!-- 重试机制 ，必须配，否则重试不生效 -->
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
</dependency>

# hystrix的超时时间必须大于ribbon的超时时间
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=10000
# 开启重试
zuul.retryable=true
spring.cloud.loadbalancer.retry.enabled=true
# 请求连接的超时时间
ribbon.connectTimeout=2000
# 请求处理的超时时间
ribbon.readTimeout=5000
# 对当前实例的重试次数
ribbon.maxAutoRetries=1
# 切换实例的重试次数
ribbon.maxAutoRetriesNextServer=3
# 对所有操作请求都进行重试
ribbon.okToRetryOnAllOperations=true

```

在zuul中要生效除了要依赖spring-retry之外还需要配置zuul.retryable=true

















 
 
 

