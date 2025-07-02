package com.example.webflux.chaper1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootTest
public class WebClientTest {
    private WebClient webClient = WebClient.builder().build();

    @Test
    public void testWebClient() {
        // webClient를 사용해서 IO블로킹 최소화
        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/flux")
                        .retrieve()
                                .bodyToFlux(Integer.class);

        // subscribe가 없으면 그냥 사라짐
        intFlux.subscribe(data -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("webFlux가 구독중 " + data);
        });
        System.out.println("Netty 이벤트 루프로 스레드 복귀!");
        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }
    }

}

