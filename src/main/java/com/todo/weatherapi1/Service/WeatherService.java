package com.todo.weatherapi1.Service;

import com.todo.weatherapi1.Models.WeatherResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {
    private static final Logger logger =  LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;
    //This will save value in cache with key as city name and value as weatherCache
   @Resource
    private RestTemplate restTemplate;

   private RedisTemplate<String,WeatherResponse> redisTemplate;

    public WeatherService( RedisTemplate<String, WeatherResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public WeatherResponse getWeatherResponse(String place){
        String url = weatherApiUrl +place+"?unitGroup=metric&key="+weatherApiKey;

        logger.info("URL called: "+url);

        WeatherResponse cachedData = getWeatherRedis(place);


        //Get cached data if it already exist in cahce
        if (cachedData == null) {
            logger.info("No data found in cache for place: " + place);

        } else {
            logger.info("Data found in cache for place: " + place);
            return cachedData;
        }

        Map<String,Object> map = restTemplate.getForObject(url, Map.class);

        List<Map<String,Object>> days = (List<Map<String, Object>>) map.getOrDefault("days",new ArrayList<>());

        ArrayList<WeatherResponse> weatherDetails = new ArrayList<>();

        String country = (String)map.get("address");

        for(Map<String,Object> day : days){
            String temperature = String.valueOf(day.get("temp"));
            String description = (String) day.get("description");
            String dateTime = (String) day.get("datetime");

            WeatherResponse currentInfo = new WeatherResponse(country,temperature,description,dateTime);

            weatherDetails.add(currentInfo);

        }
        if (weatherDetails.isEmpty()) {
            logger.info("No weather details found for place: " + place);
            return null;
        }

        saveWeather(country,weatherDetails.get(0));
        logger.info("Weather Details: "+weatherDetails.get(0));

        return weatherDetails.get(0);

    }

    //opsForValue() can setting TTL for the key
    public void saveWeather(String key , WeatherResponse value){

        redisTemplate.opsForValue().set(key, value, Duration.ofDays(1));

   }
   public WeatherResponse getWeatherRedis(String country){
       return redisTemplate.opsForValue().get(country);
   }




}
