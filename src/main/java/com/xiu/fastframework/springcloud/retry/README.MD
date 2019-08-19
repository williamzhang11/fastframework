# spring cloud重试机制

## Ribbon+RestTemplate的重试

对于整合了Ribbon的RestTemplate，例如一个RestTemplate添加了@LoadBalanced 注解

```
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
  SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new       SimpleClientHttpRequestFactory();
  simpleClientHttpRequestFactory.setConnectTimeout(1000);
  simpleClientHttpRequestFactory.setReadTimeout(1000);
  return new RestTemplate(simpleClientHttpRequestFactory);
}


spring:
  cloud:
    loadbalancer:
      retry:
        enabled: true
ribbon:
  # 同一实例最大重试次数，不包括首次调用
  MaxAutoRetries: 1
  # 重试其他实例的最大重试次数，不包括首次所选的server
  MaxAutoRetriesNextServer: 2
  # 是否所有操作都进行重试
  OkToRetryOnAllOperations: false

```

## Feign的重试

Feign本身也具备重试能力，在早期的Spring Cloud中，Feign使用的是 feign.Retryer.Default
#Default() ，重试5次。但Feign整合了Ribbon，Ribbon也有重试的能力，此时，就可能会导致行为的混乱。

Spring Cloud意识到了此问题，因此做了改进，将Feign的重试改为 feign.Retryer#NEVER_RETRY ，如需使用Feign的重试，只需使用Ribbon的重试配置即可。因此，对于Camden以及以后的版本，Feign的重试可使用如下属性进行配置：

```
ribbon:
  MaxAutoRetries: 1
  MaxAutoRetriesNextServer: 2
  OkToRetryOnAllOperations: false

```

## Zuul的重试

```
zuul:
  # 开启Zuul的重试
  retryable: true
ribbon:
  MaxAutoRetries: 1
  MaxAutoRetriesNextServer: 2
  OkToRetryOnAllOperations: false


```
上面我们使用 zuul.retryable=true 对Zuul全局开启了重试，事实上，也可对指定路由开启/关闭重试：

基于HTTP响应码重试

```
clientName:
  ribbon:
    retryableStatusCodes: 404,502

```
注意点

Hystrix的超时时间必须大于超时的时间，否则，一旦Hystrix超时，就没办法继续重试了。
一般来说，不建议将ribbon.OkToRetryOnAllOperations 设为true。因为一旦启用该配置，则表示重试任何操作，包括POST请求，而由于缓存了请求体，此时可能会影响服务器的资源。

