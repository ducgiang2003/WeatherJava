package com.todo.weatherapi1.Controller;

import com.todo.weatherapi1.Models.WeatherResponse;
import com.todo.weatherapi1.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    public static final Logger logger = Logger.getLogger(WeatherController.class.getName());

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast/{country}")
    public ResponseEntity<WeatherResponse> getWeatherForcast(@PathVariable String country){
        logger.info("Request for weather forcast for place: "+country);
        //Get weather from Redis Cache
        WeatherResponse weatherDetails = weatherService.getWeatherRedis(country);
        if(weatherDetails!=null){
            logger.info("Fetched from Redis Cache: "+weatherDetails);

            return new ResponseEntity<>(weatherDetails, HttpStatus.OK);
        }
        logger.info("Fetching weather details from API");
        weatherDetails = weatherService.getWeatherResponse(country);
        if(weatherDetails!=null){
            logger.info("Fetched from API: "+weatherDetails);
            return new ResponseEntity<>(weatherDetails, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/test")
    public String sayHello(){
        return "Hello World";
    }




}
