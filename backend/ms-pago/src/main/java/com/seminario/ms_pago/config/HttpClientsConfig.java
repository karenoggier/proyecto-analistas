package com.seminario.ms_pago.config;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.seminario.ms_pago.client.PedidoClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class HttpClientsConfig {

    @Value("${pedido.ms.url:http://localhost:8082}")
    private String pedidoBaseUrl;

     @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)  // Usa HTTP/2 cuando esté disponible
            .connectTimeout(Duration.ofSeconds(5))
            .executor(Executors.newVirtualThreadPerTaskExecutor())  // Virtual Threads
            .build();
    }

    @Bean
    public JdkClientHttpRequestFactory requestFactory(HttpClient httpClient) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(5));
        return factory;
    }

    @Bean
    public ClientHttpRequestInterceptor jwtTokenInterceptor() {
        return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                String token = jwtAuth.getToken().getTokenValue();
                
                // Log seguro: solo para debugging, con token enmascarado
                if (log.isDebugEnabled()) {
                    String maskedToken = maskToken(token);
                    log.debug("Propagando JWT token a {}: {}", 
                             request.getURI().getHost(), maskedToken);
                }
                
                // Añade el token como Bearer Authentication
                request.getHeaders().setBearerAuth(token);
                
            } else {
                log.warn("No se encontró JWT token en SecurityContext para request a: {}", 
                        request.getURI());
            }
            
            return execution.execute(request, body);
        };
    }

    @Bean
    public RestClient pedidoRestClient(
            JdkClientHttpRequestFactory requestFactory,
            ClientHttpRequestInterceptor jwtTokenInterceptor) {
        
        log.info("Configurando RestClient para MS-Pedido: {}", pedidoBaseUrl);
        
        return RestClient.builder()
            .baseUrl(pedidoBaseUrl)
            .requestFactory(requestFactory)
            .requestInterceptor(jwtTokenInterceptor) // Propaga el JWT automáticamente
            .build();
    }

    @Bean
    public PedidoClient pedidoClient(RestClient pedidoRestClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(pedidoRestClient))
            .build();
        
        return factory.createClient(PedidoClient.class);
    }

    private String maskToken(@Nullable String token) {
        if (token == null || token.length() < 20) {
            return "***";
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }
}   