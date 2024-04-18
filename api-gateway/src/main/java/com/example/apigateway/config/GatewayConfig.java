package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Value("${services.url.auth-service}")
    private String authUrl;
    @Value("${services.url.user-service}")
    private String userUrl;
    @Value("${services.url.task-service}")
    private String taskUrl;
    @Value("${services.url.comment-service}")
    private String commentUrl;
    @Value("${services.url.notification-service}")
    private String notificationUrl;

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/api/**")
                        .filters(f -> f.filter(filter))
                        .uri(userUrl))
                .route("auth-service", r -> r.path("/auths/api/**")
                        .filters(f -> f.filter(filter))
                        .uri(authUrl))
                .route("task-service", r -> r.path("/tasks/api/**")
                        .filters(f -> f.filter(filter))
                        .uri(taskUrl))
                .route("comment-service", r -> r.path("/comments/api/**")
                        .filters(f -> f.filter(filter))
                        .uri(commentUrl))
                .route("notification-service", r -> r.path("/notifications/api/**")
                        .filters(f -> f.filter(filter))
                        .uri(notificationUrl))
                .build();
    }
}
