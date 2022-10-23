package uc.mei.is.server.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import uc.mei.is.server.entity.Teacher;

import java.time.Duration;
import java.util.stream.Stream;


@SpringBootApplication
public class ReactiveService {
    @GetMapping("/teachers/{id}")
    Mono<Teacher> getAllTeachers(@PathVariable int id) {
        return Mono.just(Teacher.builder().id(id)
                                .name("João")
                                .build());
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/teachers")
    Flux<Teacher> teachers() {

        Flux<Teacher> teacherFlux = Flux.fromStream(Stream.generate(() -> Teacher.builder()
                                                                                 .id((int) System.currentTimeMillis())
                                                                                 .name("Miguel")
                                                                                 .build()));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(teacherFlux, durationFlux).map(Tuple2::getT1);
    }
}


