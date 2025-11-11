package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BeanConfig {
	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(r -> r.path("/product/**")
				.uri("lb://product-service"))
			.route(r -> r.path("/stock/**")
				.uri("lb://stock-service"))
			.build();
		}
}