package com.codigo.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {
    private final WebClient.Builder webClientBuilder;

    public GatewayConfig(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AuthFilter authFilter) {
        return builder.routes()
                .route("category", r -> r.path("/api/category/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8081"))
                .route("product", r -> r.path("/api/product/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8082"))
                .route("permit", r -> r.path("/api/permit/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8083"))
                .route("permit-person", r -> r.path("/api/permit-person/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8083"))
                .route("venta", r -> r.path("/api/sale/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8084"))
                .route("person", r -> r.path("/api/person/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8085"))
                .route("security", r -> r.path("/api/authentication/**")
                        .uri("http://localhost:8086"))
                .build();
    }
}
