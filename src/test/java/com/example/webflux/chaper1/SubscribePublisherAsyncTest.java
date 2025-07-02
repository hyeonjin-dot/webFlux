package com.example.webflux.chaper1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class SubscribePublisherAsyncTest {
    @Test
    public void produceOneToNineFlux(){
        Flux<Integer> intFlux = Flux.<Integer>create(sink ->{
            for (int i = 1; i <= 9; i++){
                try {
                    Thread.sleep(500);
                }catch (Exception e){

                }
                sink.next(i);
            }
            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic());

//        Scheduler

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
