package com.stm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        String str ="git@github.com:suntm1001/nacos_project.git";
        return "l";
    }
}
