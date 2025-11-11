package com.example.client;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		callRemoteService("http://localhost:8081/productdata", "guest", "guest");
		callRemoteService("http://localhost:8081/phone", "leo", "password");
		callRemoteService("http://localhost:8081/phone", "frank", "password");
		callRemoteService("http://localhost:8081/salary", "lore", "password"); // john is not MANAGER
		callRemoteService("http://localhost:8081/salary", "frank", "password");
	}
	public void callRemoteService(String url, String username, String password){
		String accessToken = getAccessToken(username, password);
		// Call resource server
		String response = WebClient.create(url)
									.get()
									.headers(h ->{
									if (accessToken != null)
									h.setBearerAuth(accessToken);
									})
									.retrieve()
									.onStatus(status -> status.value() == 401, clientResponse -> {
									System.out.println("Error: Unauthorized - token missing or expired for call "+url+" with user "+username);
									return Mono.empty();
		})
		.onStatus(status -> status.value() == 403, clientResponse -> {
		System.out.println("Forbidden: insufficient roles "+url+" with user "+username);
		return Mono.empty();
		})
		.bodyToMono(String.class)
		.block();
		System.out.println("Call to " +url+ " for user "+username+" gave the response "+ response);
	}

	@SuppressWarnings("unchecked")
	public String getAccessToken(String username, String password){
		WebClient webClient = WebClient.builder()
			.baseUrl("http://localhost:8090/realms/realm2/protocol/openid-connect/token")
			.build();
		// Get access token
		Map<String, Object> tokenResponse = webClient.post()
		.header("Content-Type", "application/x-www-form-urlencoded")
		.body(BodyInserters.fromFormData("grant_type", "password")
			.with("username", username)
			.with("password", password)
			.with("scope", "openid roles")
			.with("client_id", "myclient")
			.with("client_secret", "mysecret123")) // todo put to ENV
		.retrieve()
		.onStatus(status -> status.value() == 401, clientResponse -> {
			System.out.println("No token for user "+username);
			return Mono.empty(); // Do NOT throw
		})
		.bodyToMono(Map.class)
		.block();
		String accessToken = (String) tokenResponse.get("access_token");
		return accessToken;
	}
}

