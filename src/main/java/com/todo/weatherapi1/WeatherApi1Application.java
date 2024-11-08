package com.todo.weatherapi1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;


@SpringBootApplication
@RestController
@EnableCaching
public class WeatherApi1Application {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApi1Application.class, args);


    }


}
