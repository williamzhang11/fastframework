package com.xiu.fastframework.springcloud.gateway;
/**
 * 网关限流
 * @author william
 * 
 * 流做在请求filter最靠前的地方,比鉴权pre前
 * 执行,不然流量都进去了,这里需要一个算法---令牌桶算法,这
 * 个算法很多地方已经实现了,拿来用即可,意思是以一定速率将令牌放入桶中,桶中
 * 的令牌满了,就不会再放进去了,外部的请求进入,请求将获得桶中的令牌,得令牌者可通
 * 行,没有令牌请求将被拒绝
 *
 */
/*public class RateLimiterFilter extends ZuulFilter {

    //create 每秒放入100个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    *//**
     * filter类型
     *
     * @return
     *//*
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    *//**
     * filter执行顺序，值越小优先级越高
     * 官方推荐使用x-1方式优先排序
     * 选择最高优先级SERVLET_DETECTION_FILTER_ORDER，并-1
     *
     * @return
     *//*
    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    *//**
     * filter 开启关闭
     *
     * @return
     *//*
    @Override
    public boolean shouldFilter() {
        return true;
    }

    *//**
     * 实现filter逻辑
     *
     * @return
     * @throws ZuulException
     *//*
    @Override
    public Object run() throws ZuulException {
        //判断--获取通行令牌-->如果没有令牌不等于之前的没有权限401，可以抛出自定义异常或者其他处理
        if (!RATE_LIMITER.tryAcquire()) {
            throw new RateLimiterException();
        }

        return null;
    }
}*/
