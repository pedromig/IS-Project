package uc.mei.is;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.springframework.util.SocketUtils;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Exs {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // assignment 2
        //ex1();
        try {
            ex4();
        } catch (Exception e) {

        }
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

    private static void ex4() throws InterruptedException, ExecutionException {
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        //ex4
        /*
        Disposable flux = Flux.just(1, 2, 3, 4, 5)
            .log()
            .map(i -> i *2)
            .filter(v -> v > 5)
            .subscribe(x -> {System.out.println(x);});

        //ex5
        Flux<String> flux5 = Flux.range(0, 10)
            .map(n -> "Number " + n);

        flux5.subscribe(x -> {System.out.println(x);});

        //ex6
        int window = 7, step = 1;
        var flux6 = Flux.range(1, 100)
            .buffer(window, step)
            .skipLast(1)
            .map(x -> x.stream(). reduce(Integer::sum).orElseThrow() / x.size())
            .subscribe(x -> {System.out.println(x);});*/

        //ex7
        //var ex7 = Mono.from(Flux.range(0, 10)
        //                    .collectList())
        //        .subscribe(System.out::println);

        //ex8
        //var ex8 = Flux.repeat()

        //class
        ExecutorService es = Executors.newFixedThreadPool(4);

        Set<Callable<String>> callableset = new HashSet<>();
        callableset.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Task 1";
            }
        });

        callableset.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Task 2";
            }
        });

        callableset.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Task 3";
            }
        });

        callableset.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Task 4";
            }
        });

        callableset.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Task 5";
            }
        });

        String res = es.invokeAny(callableset);
        System.out.println(res);

        List<Future<String>> res2 = es.invokeAll(callableset);
        for(Future<String> r : res2)
            System.out.println(r.get());

        System.out.println("After all the gets");
        es.shutdown();

            
    }

    private static class MyComparator<T extends Integer> implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            return a.compareTo(b);
        }
    }
}