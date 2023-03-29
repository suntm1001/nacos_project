package com.stm.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FastJsonDemo {
    public static void main(String[] args) {
        //JSON
        List<HashMap<String,Object>> list = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","22");
        map.put("2","211");
        list.add(map);
        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("1","22");
        map1.put("2","211");
        list.add(map1);
        Page page = new Page();
        page.setCurrent(1);
        page.setSize(2);
        //对象转json字符
        String str = JSONObject.toJSONString(page);
        String str1 = JSONObject.toJSONString(list);
        System.err.println(str1);
        System.err.println(str);
        //json字符转对象
        Page pageJson = JSON.parseObject(str,Page.class);
        System.err.println(pageJson.getCurrent());
    }
}
