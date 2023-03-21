package com.stm.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("provider-demo")
public interface ComsumerService {
    
    @RequestMapping("/hello")
    public String hello();
}
