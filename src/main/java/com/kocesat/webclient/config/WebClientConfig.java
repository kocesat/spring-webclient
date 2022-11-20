package com.kocesat.webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfig {
    private final static int TIMEOUT = 7000;

    @Bean HttpClient httpClient(){
//        SslContextBuilder clientCtx = SslContextBuilder.forClient()
//                .sslProvider(SslProvider.JDK);
        final HttpClient httpClient = HttpClient.create()
//                .secure(spec -> spec.sslContext(clientCtx))
                .compress(true)
//                .wiretap("reactor.netty.http.client.HttpClient",
//                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });
        httpClient.warmup().block();
        log.info("Netty Http Client initialized");
        return httpClient;
    }

    @Bean
    public WebClient todoWebClient() {
        return WebClient.builder()
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .baseUrl("https://jsonplaceholder.typicode.com/todos")
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info(String.format("Requesting %s", clientRequest.url()));
            return Mono.just(clientRequest);
        });
    }
    @Bean
    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info(String.format("Response %s", response.statusCode()));
            return Mono.just(response);
        });
    }
}
