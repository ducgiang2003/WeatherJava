package com.todo.weatherapi1.Config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;


@Component
public class RareLimiterFilter implements Filter {

    private final int MAX_REQUESTS = 10;

    private final int TIME_LIMIT_Minutes = 2;
    //Setting Bucket
    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(MAX_REQUESTS, Refill.greedy(MAX_REQUESTS, Duration.ofMinutes(TIME_LIMIT_Minutes))))//10 requests per minute
            .build();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //Check if client can make request
        if (bucket.tryConsume(1)) {

            filterChain.doFilter(request, response);
        } else {
            //429 Too many requests
            ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            if (!response.isCommitted()) {
                response.getWriter().write("Too many requests");
            }
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
