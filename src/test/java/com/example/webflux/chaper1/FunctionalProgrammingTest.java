package com.example.webflux.chaper1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@SpringBootTest
public class FunctionalProgrammingTest {
    @Test
    public void produceOneToNine(){
        List<Integer> sink = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            sink.add(i);
        }

        sink = map(sink, (data) -> data * 2);

        sink = filter(sink, (data) -> data % 4 == 0);

        forEach(sink, (data) -> System.out.println(data));
    }

    @Test
    public void produceOneToNineStream(){
        IntStream.rangeClosed(1, 9).boxed()
                .map((data) -> data * 2)
                .filter(value -> value % 4 == 0)
                .forEach((data) -> System.out.println(data));
    }

    @Test
    public void produceOneToNineFlux(){
        Flux<Integer> intFlux = Flux.create(sink ->{
            for (int i = 1; i <= 9; i++){
                sink.next(i);
            }
            sink.complete();
        });

        // subscribe가 없으면 그냥 사라짐
        intFlux.subscribe(data -> System.out.println("webFlux가 구독중 " + data));
        System.out.println("Netty 이벤트 루프로 스레드 복귀!");
    }

    @Test
    public void produceOneToNineFluxStream(){
        Flux.fromIterable(IntStream.rangeClosed(1, 9).boxed().toList())
                .map((data) -> data * 2) // operator 대부분이 stream과 유사하게 동작
                .filter(value -> value % 4 == 0)
                .subscribe((data) -> System.out.println(data));
    }

    private static void forEach(List<Integer> sink, Consumer<Integer> consumer) {
        for (int i = 0; i < sink.size(); i++){
            consumer.accept(sink.get(i));
        }
    }

    private static List<Integer> filter(List<Integer> sink, Function<Integer, Boolean> filter) {
        List<Integer> sink2 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            if (filter.apply(sink.get(i))) {
                sink2.add(sink.get(i));
            }
        }

        sink = sink2;
        return sink;
    }

    private static List<Integer> map(List<Integer> sink, Function<Integer, Integer> mapper) {
        List<Integer> sink1 = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            sink1.add(mapper.apply(sink.get(i)));
        }

        sink = sink1;
        return sink;
    }
}
