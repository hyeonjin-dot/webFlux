package com.example.webflux.chaper2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class BasicMonoOperatorTest {

    //just, empty
    @Test
    public void startMonoFromData(){
        Mono.just(1).subscribe(data -> System.out.println(data));

        Mono.empty().subscribe(data -> System.out.println("empty data " + data));
    }

    @Test
    public void startMonoFromFunction(){
        Mono<String> monoFromCallable = Mono.fromCallable(() -> { // 동기적인 객체를 반환할때
            return callRestTemplate("안녕");
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> defer = Mono.defer(() -> { // mono를 반환하고 싶을때
            return Mono.just("안녕"); // 이 defer를 구독해야 라인이 실행 (데이터가 생성)
        });
    }

    public String callRestTemplate(String request){
        return request + " callRestTemplate 응답";
    }

    public Mono<String> callWebClinent(String request){
        return Mono.just(request +  "callWebClient");
    }

    @Test
    public void testDeferNecessity(){
        // a b c 만드는 로직도 mono를 흐름안에서 관리하고 싶다
        Mono<String> stringMono = Mono.defer(() -> {
            String a = "안녕";
            String b = "하세";
            String c = "요";
            return callWebClinent(a + b + c);
        }).subscribeOn(Schedulers.boundedElastic());
    }


    // mono에서 데이터가 많아져서 flux로 바꾸고 싶다 -> flatMapMany
    @Test
    public void monoToFlux(){
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerFlux.subscribe(data -> System.out.println(data));
    }
}

/*
* 모노의 흐름 시작 방법
* 1. 데이터로부터 시작 -> 일반적인 경우 just / 특수 경우 empty(Optional.empty())
* 2. 함수로부터 시작 ->
*   동기적인 객체를 mono로 반환하고 싶을때 fromCallable / 코드의 흐름을 Mono 안에서 관리하면서 Mono를 반환하고 싶을 때 defer
*/
