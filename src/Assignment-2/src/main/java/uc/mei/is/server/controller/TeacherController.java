package uc.mei.is.server.controller;

import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import uc.mei.is.server.entity.Teacher;
import uc.mei.is.server.repository.TeacherRepository;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherRepository teacherRepository;

    @GetMapping (value = "/add/{id}/{name}")
    public Mono<Teacher> addTeacher(@PathVariable("id") int id, @PathVariable("name") String name) {
        Teacher t = Teacher.builder()
                                .id(id)
                                .name(name)
                                .build();
        teacherRepository.save(t).subscribe();
        return Mono.just(t);                      
    }

    @GetMapping (value = "/{id}")
    public Mono<Teacher> getAllTeachers(@PathVariable("id") int id) {
        Mono<Teacher> t = teacherRepository.findById(id);
        return t;
    }

    @GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/all")
    public Flux<Teacher> teachers () {

        /*Flux<Teacher> teacherFlux = Flux.fromStream( Stream.generate(()->Teacher.builder()
                        .id((int) System.currentTimeMillis())
                        .name("Miguel")
                        .build()));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));*/
        Flux<Teacher> teachers = teacherRepository.findAll();

        return teachers;
    }
    
}
