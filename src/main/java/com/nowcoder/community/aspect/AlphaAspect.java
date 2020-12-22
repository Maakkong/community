package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/21 20:27
 * @Description 切面示例
 */
//@Component
//@Aspect
public class AlphaAspect {

    //execution(方法修饰符(可选)  返回类型  类路径 方法名  参数  异常模式(可选))
    //1）execution(public * *(..))——表示匹配所有public方法
    //2）execution(* set*(..))——表示所有以“set”开头的方法
    //3）execution(* com.xyz.service.AccountService.*(..))——表示匹配所有AccountService接口的方法
    //4）execution(* com.xyz.service.*.*(..))——表示匹配service包下所有的方法
    //5）execution(* com.xyz.service..*.*(..))——表示匹配service包和它的子包下的方法
//    @Pointcut("execution(* com.nowcoder.community.service..*.*(..))")
//    public void pointCut(){
//
//    }
//
//    @Before("pointCut()")
//    public void before(){
//        System.out.println("before");
//    }
//
//    @After("pointCut()")
//    public void after(){
//        System.out.println("after");
//    }
//
//    @AfterReturning("pointCut()")
//    public void afterReturning(){
//        System.out.println("afterReturning");
//    }
//
//    @AfterThrowing("pointCut()")
//    public void afterThrowing(){
//        System.out.println("pointCut()");
//    }
//
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
//        //调用目标组件方法
//        System.out.println("aroundBefore");
//        Object obj = joinPoint.proceed();
//        System.out.println("aroundAfter");
//        return obj;
//    }

}
