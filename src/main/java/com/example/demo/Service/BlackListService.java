package com.example.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BlackListService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void blackListToken(String token , Long expirationtimeinmillis){

        long now = System.currentTimeMillis();
        long duration = expirationtimeinmillis - now;

        if(duration>0){

            redisTemplate.opsForValue().set(token,"revoked", Duration.ofMillis(duration));

        }
    }

    public boolean blackListed(String token  ){
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

}
