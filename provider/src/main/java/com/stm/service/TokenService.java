package com.stm.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.stm.pojo.User;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token = "";
        /*JWTCreator.Builder builder = JWT.create();
        
        builder.withExpiresAt(calendar.getTime());*/
        Calendar calendar = Calendar.getInstance();

        //设置token过期时间
        calendar.add(Calendar.MINUTE, 30);
        token = JWT.create().withAudience(user.getId()).withIssuedAt(start).withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
