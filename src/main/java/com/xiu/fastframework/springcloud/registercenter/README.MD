# 注册中心

## 有哪些
在 Spring Cloud 中，能够使用的注册中心：

（1）spring-cloud-netflix-eureka-server 和 spring-cloud-netflix-eureka-client ，基于 Eureka 实现

（2）spring-cloud-alibaba-nacos-discovery ，基于 Nacos 实现

（3）spring-cloud-zookeeper-discovery ，基于 Zookeeper 实现

以上的实现，都是基于 spring-cloud-commons 的 discovery 的 DiscoveryClient 接口，实现统一的客户端的注册发现

## 为什么使用服务发现


在传统应用中，服务实例的网络位置是相当于固定的；代码能从一个偶尔更新的配置文件中读取网络配置。
但是对于基于云端的，微服务应用来说，服务实例的网络位置往往是动态分配的。由于扩展、失败和升级，服务实例
会经常动态改变，因此，客户端代码需要更加复杂的服务发现机制。

通过注册中心，调用方(Consumer)获得服务方(Provider)的地址，从而能够调用。

实际情况下，会分成两种注册中心的发现模式：

客户端发现模式
服务端发现模式

在 Spring Cloud 中，我们使用前者，即客户端发现模式。

### 1.客户端发现模式

客户端决定相应服务实例的网络位置，并且对请求实现负载均衡。并且对请求实现负载均衡。客户端查询服务注册表，然后使用
负载均衡算法从中选择一个实例，发出请求。
![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/register1.JPG)

服务实例的网络位置在启动时被记录到服务注册中心，等实例终止时被删除。服务实例的注册信息通常使用心跳机制定期刷新。

Netflix Eureka是一个服务注册中心，为服务实例注册管理和查询可用实例提供rest api接口。netflix Ribbon 是IPC
客户端，与eureka一起实现对请求的负载均衡。

优点：

客户端知晓可用服务实例，能针对特定应用实现智能负载均衡，比如使用哈希一致性。

缺点：

客户端与服务注册绑定，要针对服务端用到的每个编程语言和框架，实现客户端的服务发现逻辑。

### 2. 服务端发现模式

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/findmodule1.JPG)

客户端通过负载均衡器向某个服务提出请求，负载均衡器查询服务注册表，并将请求转发到可用的服务实例。服务实例在服务注册中心
注册和注销

AWS Elastic load balancer（ELB）是服务端发现路由的例子。

优点：

客户端无需关注发现细节，只需简单的向负载均衡器发送请求。这减少编程语言框架需要完成的发现逻辑。

缺点:

负载均衡器会称为一个需要配置和管理的高可用系统组件

## 服务注册表

服务注册表是服务发现的核心，包含服务实例的网络地址的数据库，服务注册表需要高可用而且随时更新。

客户端能够从服务注册表中获取网络地址，而信息过时时，客户端无法发现服务实例。

Eureka就是服务注册表，为注册和请求服务实例提供REST API.服务实例使用POST请求来注册网络地址，
同时将服务端的服务信息缓存到本地。每30s使用put请求刷新来刷新注册信息。注册信息也能通过http 
delete请求或实例超时被移除。客户端能够使用HTTP GET 请求检索已注册的服务实例。

Eureka 客户端，包括服务和服务客户端，查询 DNS 去发现 Eureka 服务的网络地址。客户端首选同一域
内的 Eureka 服务。然而，如果没有可用服务，客户端会使用其它可用域中的 Eureka 服务

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/eureka.JPG)


![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/eurekaframe.jpg)

## Eureka 的自我保护模式
当 Eureka Server 节点在短时间内丢失了过多实例的连接时（比如网络故障或频繁的启动关闭客户端），
那么这个节点就会进入自我保护模式，一旦进入到该模式，Eureka server 就会保护服务注册表中的信息，
不再删除服务注册表中的数据（即不会注销任何微服务），当网络故障恢复后，该 Ereaka Server 节点就
会自动退出自我保护模式。

默认情况下，如果 Ereaka Server 在一段时间内没有接受到某个微服务示例的心跳，便会注销该实例（默认90秒）
，而一旦进入自我保护模式，那么即使你关闭了指定实例，仍然会发现该 Ereaka Server 的注册实例中会存
在被关闭的实例信息.

解决这种情况的方法主要有几种方式：

1. 等待 Eureka Server 自动恢复
正常的情况下，等待网络恢复（或者没有频繁的启动与关闭实例）后，等待一段时间 Eureka Server 会自动关闭自我保护模式，但是如果它迟迟没有关闭该模式，那么便可以尝试手动关闭，如下。

2. 重启 Eureka Server
通常而言，PRD 环境建议对 Eureka Server 做负载均衡，这样在依次关闭并开启 Eureka Server 后，无效的实例会被清除，并且不会对正常的使用照成影响。

3. 关闭 Eureka 的自我保护模式

对于开发环境的 Eureka Server，个人更建议关闭它的自我保护模式，因为你可能需要不断的开启与关闭实例，如果
并未关闭自我保护模式，那么很容易就会触发自我保护模式，此时对调试会相对比较麻烦。

但是关闭自我保护模式，会有另外一个可能的问题，即隔一段时间后，可能会发生实例并未关闭，却无法通过网关访问了，
此时很可能是由于网络问题，导致实例（或网关）与 Eureka Server 断开了连接，Eureka Server 已经将其注销（
网络恢复后，实例并不会再次注册），此时重启 Eureka Server 节点或实例，并等待一小段时间即可。

## Eureka 集群
为了使 Eureka Server 实现高可用，我们需要为它配置集群。这样当有一台 Eureka Server 有故障时，集群中的其他
 Server 可以进行代替。Eureka 集群之中的 Node 通过 P2P 通信的方式共享注册表，以使得每个 Eureka Server 的注册表保持一致


## 服务注册的方式

服务实例必须在注册表中注册和注销。注册和注销有两种不同的方法。方法一是服务实例自己注册，也叫自注册模式（self-registration pattern）；另一种是采用管理服务实例注册的其它系统组件，即第三方注册模式。

### 自注册方式
当使用自注册模式时，服务实例负责在服务注册表中注册和注销。另外，如果需要的话，一个服务实例也要发送心跳来
保证注册信息不会过时
Netflix OSS Eureka 客户端是非常好的案例。

### 第三方注册模式


