package com.stm.pojo;

import com.stm.annotation.AnnotationTest;
import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String password;
    @AnnotationTest(dicCode="BCA1001")
    private String sex;
    
}
