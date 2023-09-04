package com.Gateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(originPatterns = "*")
public class Config {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("websocket", r -> r.path("/api/websocket/**")
                        .uri("wss://localhost:8085/"))
                .route("rest",r-> r.path("/api/rest/**")
                      .uri("https://localhost:8085/"))
                .build();
    }

}
