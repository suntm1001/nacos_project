package com.stm.controller;

import com.stm.service.ComsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
    @Autowired
    ComsumerService comsumerService;
 @Autowired
 private RestTemplate restTemplate;
    @RequestMapping("test")
    public String test(){
        
        return "1";
    }

    @GetMapping("/sayHi")
    public String sayHi() {
        /*String url = String.format("http://provider-demo/hello");
        System.out.println("request url:" + url);
        return restTemplate.getForObject(url, String.class);*/
        
        return comsumerService.hello();
    }
}
