package com.codigo.gateway_service.config;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;

import java.io.IOException;

@Configuration
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(this.messageConverters));
    }

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 403) {
                if (response.body() != null) {
                    String responseBody = null;
                    try {
                        responseBody = Util.toString(response.body().asReader());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    return new RuntimeException("Feign error: 403 Forbidden - " + responseBody);
                } else {
                    return new RuntimeException("Feign error: 403 Forbidden - No message provided");
                }
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}