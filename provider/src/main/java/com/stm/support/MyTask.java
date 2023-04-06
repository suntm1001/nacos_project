package com.stm.support;

import java.util.List;
import java.util.concurrent.Callable;

public class MyTask implements Callable<String> {
    private Integer num;
    public MyTask(Integer num) {
        this.num = num;
    }
    private String process() {
        String str="成功";
        try {
            if(num>500){
                str="成功";
            }else{
                str="失败";
            }
            Thread.sleep(200);	//模拟处理延时
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;	//整数进行加1操作
    }
    
    @Override
    public String call() throws Exception {
        
        return process();
    }
}
