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
    
    @Pointcut("execution(* com.stm.controller..*.*(..))")
    public void export(){
        
    }
    
    @Around("export()")
    public Object afterExport(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object object = new Object();
    object = proceedingJoinPoint.proceed();
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

