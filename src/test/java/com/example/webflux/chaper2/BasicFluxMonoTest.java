package com.example.webflux.chaper2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class BasicFluxMonoTest {

    @Test
    public void testBasicFluxMono(){
        // flux -> 빈 함수로부터 시작 가능 or 데이터로부터 시작 가능
        Flux.just(1, 2, 3, 4, 5)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("flux가 구독한 data " + data));
        // just로 데이터 흐름 시작 -> 데이터 가공 -> subscribe에서 데이터 방출

        // mono 0-1개의 데이터만 방출 가능한 객체 -> Optional 같은 객체
        // flux 0개 이상의 데이터를 방출할 수 있는 객체 -> List, Stream 같은 객체
        Mono.just(2)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono가 구독한 data " + data));
    }

    @Test
    public void testFluxMonoBlock(){
        Mono<String> justString = Mono.just("String");
        String string = justString.block();
        System.out.println("string = " + string);
    }
}
