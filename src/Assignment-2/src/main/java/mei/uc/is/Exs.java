package mei.uc.is;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import reactor.core.publisher.Flux;

public class Exs {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // assignment 2
        //ex1();
        ex4();
    }

    private static void ex1() {
        
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        Random rnd = new Random(42);
        for (int i = 0; i < 10; i++) {
            numbers.add(rnd.nextInt(0, 10));
            if(i >= 2) {
                System.out.println(numbers.get(numbers.size() - 2) + " : " + numbers.get(numbers.size() - 1) + " : " + (numbers.get(numbers.size() - 2) - (numbers.get(numbers.size() - 1))));
            }
        }
        System.out.println(numbers);

        //numbers.sort(new MyComparator<Integer>());

        /*numbers.sort(
            (n1, n2) -> n1 - n2
        );*/

        BiFunction<Integer, Integer, Integer> method = (n1, n2) -> n1 - n2;

        numbers.sort(method::apply);
        /*numbers.sort(new Comparator<Integer>() {
            @Override 
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        });*/

        System.out.println(numbers);
    }

    private static void ex4() {
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        Flux.just(1, 2, 3, 4, 5)
            .log()
            .map(i -> i *2)
            .filter(v -> v > 5)
            //.string(v: "Number " + v);
            .subscribe(numbers::add);

        Flux<String> flux = Flux.generate(
            () -> 0,
            (state, sink) -> {
                sink.next("3 x " + state + " = " + 3*state);
                if(state == 10) sink.complete();
                return state + 1;
            }
        );

        System.out.println(flux.toString());

        //System.out.println(numbers);
    }

    private static class MyComparator<T extends Integer> implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            return a.compareTo(b);
        }
    }
}