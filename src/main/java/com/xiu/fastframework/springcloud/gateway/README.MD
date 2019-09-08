# 网关服务

在 Spring Cloud 中，能够使用的网关服务，主要是两个，如下

（1）spring-cloud-netflix-zuul ，基于 Zuul1 实现

（2）spring-cloud-gateway ，基于 Spring Webflux 实现。

## 为什么要网关服务？

优点：
（1）阻止将内部的敏感信息暴露给外部的客户端
API网关通过提供微服务绑定和解绑的能力来将外部的公开API与内部微服务API分离开。
这样就具备重构和裁切微服务而不会影响外部绑定的客户端的能力。它同时对客户端隐藏了
服务发现和服务版本这些细节，因为它对所有微服务提供单一的访问点。

（2）为服务增加额外的安全层
API网关通过提供额外的安全层帮助阻止大规模攻击。这些攻击包括SQL注入，XML解析漏洞和DoS攻击。

（3）可以支持混合通讯协议
不同于外部API一般使用HTTP或REST，内部微服务可以从使用不用通讯协议中收获益处。这些协议可以
是ProtoBuf或AMQP，甚至是诸如SOAP，JSON-RPC或者XML-RPC这样的系统集成协议。API网关可
以对这些协议提供统一的外部REST接口，这就允许开发团队挑选一款最适合于内部架构的协议。

（4）降低微服务的复杂度
微服务中有一些常见的要点，诸如使用API令牌进行授权，访问控制和调用频次限制。这每个点都需要
每个服务区增加额外的时间去实现它们。API网关将这些要点从你的代码中提取出来，允许你的服务只关注
于它们需要关注的任务。

（5）微服务模拟和虚拟化
随着微服务API从外部API中分离出来，你可以模拟你的服务去做设计验证或者很方便的做集成测试。

缺点：

（1）由于API网关需要额外的配置，你的部署架构需要更多的编排和管理能力。

（2）在发布阶段需要管理路由的配置，以保障外部API被正确的路由到微服务上。

（3）除非很好的使用高可用，可扩展的架构，否则API网关将会变成瓶颈和单点。


另外通过网关可用实现同一功能：
使用网关服务，我们实现统一的功能：

动态路由

灰度发布

健康检查

限流

熔断

认证: 如数支持 HMAC, JWT, Basic, OAuth 2.0 等常用协议

鉴权: 权限控制，IP 黑白名单，同样是 OpenResty 的特性

可用性

高性能

## Zuul

作用：API 网关，路由，负载均衡等多种作用。
简介：类似 Nginx ，反向代理的功能，不过 Netflix 自己增加了一些配合其他组件的特性。

在微服务架构中，后端服务往往不直接开放给调用端，而是通过一个 API网关根据请求的 url ，路由到相应的服务。当添加API网关后，在第三方调用端和服务提供方之间就创建了一面墙，这面墙直接与调用方通信进行权限控制，后将请求均衡分发给后台服务端。

Zuul网关的核心是一系列的过滤器，这些过滤器可以对请求或者响应结果做一系列过滤，
Zuul 提供了一个框架可以支持动态加载，编译，运行这些过滤器，这些过滤器是使用责
任链方式顺序对请求或者响应结果进行处理的，这些过滤器直接不会直接进行通信，但是通
过责任链传递的RequestContext参数可以共享一些东西。




```
package com.chhliu.springcloud.zuul;  
import javax.servlet.http.HttpServletRequest;  
import com.netflix.zuul.ZuulFilter; 
import com.netflix.zuul.context.RequestContext; 
public class AccessUserNameFilter extends ZuulFilter { 
  @Override
  public Object run() { 
    RequestContext ctx = RequestContext.getCurrentContext(); 
    HttpServletRequest request = ctx.getRequest(); 
  
    System.out.println(String.format("%s AccessUserNameFilter request to %s", request.getMethod(), request.getRequestURL().toString())); 
  
    String username = request.getParameter("username");// 获取请求的参数 
    if(null != username && username.equals("chhliu")) {// 如果请求的参数不为空，且值为chhliu时，则通过 
      ctx.setSendZuulResponse(true);// 对该请求进行路由 
      ctx.setResponseStatusCode(200); 
      ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态 
      return null; 
    }else{ 
      ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由 
      ctx.setResponseStatusCode(401);// 返回错误码 
      ctx.setResponseBody("{\"result\":\"username is not correct!\"}");// 返回错误内容 
      ctx.set("isSuccess", false); 
      return null; 
    } 
  } 
  
  @Override
  public boolean shouldFilter() { 
    return true;// 是否执行该过滤器，此处为true，说明需要过滤 
  } 
  
  @Override
  public int filterOrder() { 
    return 0;// 优先级为0，数字越大，优先级越低 
  } 
  
  @Override
  public String filterType() { 
    return "pre";// 前置过滤器 
  } 
} 
```

在zuul中定义了四种不同生命周期的过滤器类型，具体如下：

pre：可以在请求被路由之前调用

route：在路由请求时候被调用

post：在route和error过滤器之后被调用

error：处理请求时发生错误时被调用

Zuul的主要请求生命周期包括“pre”，“route”和“post”等阶段。对于每个请求，都会运行具有这些类型的所有过滤器。

filterOrder：通过int值来定义过滤器的执行顺序

shouldFilter：返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效

run：过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，不对其进行路由，然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码

Zuul大部分功能都是通过过滤器来实现的，Zuul中定义了4种标准过滤器类型，这些过滤器类型对应于请求的典型生命周期。

PRE:这种过滤器在请求被路由之前调用。可利用这种过滤器实现身份验证、在集群中选择请求的微服务，记录调试信息等。

ROUTING:这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netflix Ribbon请求微服务。

POST:这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP header、收集统计信息
和指标、将响应从微服务发送给客户端等。

ERROR:在其他阶段发送错误时执行该过滤器。

除了默认的过滤器类型，Zuul还允许创建自定义的过滤器类型。例如，可以定制一种STATIC类型的过滤器，直
接在Zuul中生成响应，而不将请求转发到后端的微服务。

Zuul请求的生命周期如下图，该图详细描述了各种类型的过滤器的执行顺序。

![image](https://github.com/williamzhang11/fastframework/blob/master/src/main/java/com/xiu/fastframework/image/zuul.JPG)

当zuul接受到请求后，首先会由前置过滤器进行处理，然后在由路由过滤器具体把请求转发到后端应用，
然后在执行后置过滤器把执行结果写会到请求方，当上面任何一个类型过滤器执行出错时候执行该过滤器。



