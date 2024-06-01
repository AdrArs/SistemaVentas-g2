package com.codigo.gateway_service.config;

import com.codigo.gateway_service.client.AuthClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final AuthClient authClient;

    public AuthFilter(@Lazy AuthClient authClient) {
        super(Config.class);
        this.authClient = authClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] chunks = tokenHeader.split(" ");
            if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String token = chunks[1];

            try {
                if (authClient.getAuth(token)){
                    return chain.filter(exchange);
                }else {
                    return onError(exchange, HttpStatus.SERVICE_UNAVAILABLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return onError(exchange, HttpStatus.SERVICE_UNAVAILABLE);
            }
        };
    }
    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
    public static class Config {
    }

}
