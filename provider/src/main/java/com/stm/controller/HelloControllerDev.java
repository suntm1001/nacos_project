package com.stm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControllerDev {
    @RequestMapping("/hellodev")
    public String hello(){
        String str ="git@github.com:suntm1001/nacos_project.git";
        String s = "ss";
        return "l";
    }
}
