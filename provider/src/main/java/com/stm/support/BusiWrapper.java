package com.stm.support;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class BusiWrapper<T> implements Serializable {
    private T obj;
    
    private List<T> objs;
    
    private String resMsg;
}
