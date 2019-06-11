package com.addplus.server.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.addplus.server")
@EnableTransactionManagement
@EnableScheduling
public class WebApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext= SpringApplication.run(WebApplication.class, args);
        RedisTemplate<String,String> redisTemplate=(RedisTemplate<String, String>) applicationContext.getBean("redisTemplate");
/*        redisTemplate.opsForValue().set("aaaa","8s");*/
 /*       System.out.println(redisTemplate.opsForValue().get("aaaa"));*/

    }

}
