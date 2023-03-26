package com.stm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControllerYes {
    @RequestMapping("/hello")
    public String hello(){
        String str ="git@github.com:suntm1001/nacos_project.git";
        String s = "ss2";
        String hebing="da";
        return "l";
    }
}
