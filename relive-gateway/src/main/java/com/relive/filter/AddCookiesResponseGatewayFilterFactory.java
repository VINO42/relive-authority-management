package com.relive.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.support.GatewayToStringStyler;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author: ReLive
 * @date: 2021/10/24 4:43 下午
 */
@Component
public class AddCookiesResponseGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    AddCookiesResponseGatewayFilterFactory.this.addCookies(exchange, config);
                }));
            }

            public String toString() {
                return GatewayToStringStyler.filterToStringCreator(AddCookiesResponseGatewayFilterFactory.this).append(config.getName(), config.getValue()).toString();
            }
        };
    }

    protected void addCookies(ServerWebExchange exchange, NameValueConfig config) {
        MultiValueMap<String, ResponseCookie> cookies = exchange.getResponse().getCookies();
        cookies.add(config.getName(), ResponseCookie.from(config.getValue(), UUID.randomUUID().toString())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .build());
    }
}

