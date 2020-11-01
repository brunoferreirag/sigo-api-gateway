package br.com.indtextbr.services.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class SigoApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigoApiGatewayApplication.class, args);
	}

}