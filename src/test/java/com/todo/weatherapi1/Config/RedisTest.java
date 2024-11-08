package com.todo.weatherapi1.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTest {
    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testRedisCacheMangager(){
        Cache cache = cacheManager.getCache("WeatherCache");
        assertThat(cache).isNotNull();

        String key ="testKey";

        String value = "testValue";

        cache.put(key,value);
        String cacheValue = cache.get(key,String.class);

        assertThat(cacheValue).isEqualTo(value);
    }
}
