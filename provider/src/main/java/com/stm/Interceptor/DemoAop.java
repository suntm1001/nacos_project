package com.stm.Interceptor;

import com.stm.annotation.AnnotationTest;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * AOP案例一般用于数据字典的翻译
 */

@Slf4j
@Aspect
@Component
public class DemoAop {
    /**
     * 定义切点,切点为对应controller
     * execution表达式第一个*表示匹配任意的方法返回值，第二个*表示所有controller包下的类，第三个*表示所有方法,第一个..表示任意参数个数。
     */
    //@Pointcut("execution(* com.stm.controller.*.*(..))")
    //定义切点,切点为添加了注解的方法
    @Pointcut("@annotation(com.stm.annotation.PassToken)")
    public void export(){
        
    }

    /**
     * Advice，在切入点上执行的增强处理，主要有五个注解：
     *
     * @Before 在切点方法之前执行
     *
     * @After 在切点方法之后执行
     *
     * @AfterReturning 切点方法返回后执行
     *
     * @AfterThrowing 切点方法抛异常执行
     *
     * @Around 属于环绕增强，能控制切点执行前，执行后
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("export()")
    public Object afterExport(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object object = new Object();
    //方法中的参数JoinPoint为连接点对象，它可以获取当前切入的方法的参数、代理类等信息，因此可以记录一些信息，验证一些信息等
    object = proceedingJoinPoint.proceed();
    //获取所有参数
    Object[] args = proceedingJoinPoint.getArgs();
    //此处获取到数据库查询出来的数据之后，即可根据注解判断对需要进行字典转化的属性进行转化
    List<Object> list = (List<Object>) object;
    for (Object object1:list){
        dictMapping(object1);
    }
    return object;
    }
    
    private void dictMapping(Object object){
        try {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0) {
                return;
            }
            for (Field field : fields) {
                AnnotationTest annotationTest = field.getAnnotation(AnnotationTest.class);
                if (null == annotationTest) {
                    continue;
                }
                field.setAccessible(true);

                //获取字典
                String code = annotationTest.dicCode();
                if (StringUtils.isEmpty(code)) {
                    continue;
                }
                //获取该属性的当前值
                Object col = field.get(object);
                if(null == col){
                    continue;
                }
                /*String fieldVal = col.toString();
                //查找字典名
                String dicSubName=fieldVal;
                field.set(object,dicSubName);*/
                //这里根据当前值找到对应的翻译
                String val="男";
                //将字典翻译值回写给当前属性
                field.set(object,val);
            }
        }catch (Exception e){
            log.error("数据字典转化错误");
        }
    }
}

