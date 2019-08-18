package com.xiu.fastframework.spring.aop;
/**
 * 
 1.AuthChecker 注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthChecker {
}

2.HttpAopAdviseDefine.java

@Component
@Aspect
public class HttpAopAdviseDefine {

    // 定义一个 Pointcut, 使用 切点表达式函数 来描述对哪些 Join point 使用 advise.
    @Pointcut("@annotation(com.xys.demo1.AuthChecker)")
    public void pointcut() {
    }

    // 定义 advise
    @Around("pointcut()")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        // 检查用户所传递的 token 是否合法
        String token = getUserToken(request);
        if (!token.equalsIgnoreCase("123456")) {
            return "错误, 权限不合法!";
        }

        return joinPoint.proceed();
    }

    private String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("user_token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}

**/
