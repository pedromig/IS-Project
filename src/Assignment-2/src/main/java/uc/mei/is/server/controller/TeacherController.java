package uc.mei.is.server.controller;

import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import uc.mei.is.server.entity.Teacher;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping ("/{id}")
    Mono<Teacher> getAllTeachers(@PathVariable int id) {
        return Mono.just(Teacher.builder().id(id)
                                .name("João")
                                .build());
    }

    //@GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/teachers")
    @GetMapping
    Flux<Teacher> teachers () {

        Flux<Teacher> teacherFlux = Flux.fromStream( Stream.generate(()->Teacher.builder()
                        .id((int) System.currentTimeMillis())
                        .name("Miguel")
                        .build()));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip( teacherFlux, durationFlux).map(Tuple2::getT1);
    }
    
}
