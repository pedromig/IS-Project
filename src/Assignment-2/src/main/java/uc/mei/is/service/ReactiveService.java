package uc.mei.is.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uc.mei.is.entity.Teacher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@RestController
public class ReactiveService {

    @GetMapping ("/teachers/{id}")
    Mono<Teacher> getAllTeachers(@PathVariable int id) {
        return Mono.just(Teacher.builder().id(id)
                                .name("João")
                                .build());
    }

    @GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/teachers")
    Flux<Teacher> teachers () {

        Flux<Teacher> teacherFlux = Flux.fromStream( Stream.generate(()->Teacher.builder()
                        .id((int) System.currentTimeMillis())
                        .name("Miguel")
                        .build()));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip( teacherFlux, durationFlux).map(Tuple2::getT1);
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveService.class, args);
    }
    
}
