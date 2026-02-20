package com.github.syann97.toymsa.apigatewayservice.filter;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	Environment env;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}

	public static class Config {
		// Put configuration properties here
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer ", "");

			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}

			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		log.error(err);

		byte[] bytes = "The requested token is invalid.".getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return response.writeWith(Flux.just(buffer));
	}

	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		String subject = null;

		try {
			// 1. SecretKey 생성 (User Service와 동일한 방식)
			log.info("Gateway가 현재 사용하는 Secret: {}", env.getProperty("token.secret"));
			byte[] secretKeyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);
			SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyBytes);

			// 2. 최신 JJWT 0.12.x 파싱 문법 적용
			subject = Jwts.parser()
				.verifyWith(signingKey) // setSigningKey 대신 verifyWith 사용
				.build()
				.parseSignedClaims(jwt) // parseClaimsJws 대신 사용
				.getPayload()           // getBody 대신 getPayload 사용
				.getSubject();

		} catch (Exception ex) {
			log.error("JWT validation error: {}", ex.getMessage()); // 진짜 에러 원인을 로그로 확인!
			returnValue = false;
		}

		if (subject == null || subject.isEmpty()) {
			returnValue = false;
		}

		return returnValue;
	}

}