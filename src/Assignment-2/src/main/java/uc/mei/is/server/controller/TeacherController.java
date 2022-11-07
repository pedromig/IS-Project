package uc.mei.is.server.controller;

import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    // this can also be used to update teachers
    @PostMapping (value = "/add/{name}")
    public Mono<Teacher> addTeacher(@PathVariable("name") String name) {
        Teacher t = Teacher.builder()
                                .name(name)
                                .build();
        teacherRepository.save2(t.getName()).subscribe();

        return Mono.just(t);
             
    }

    @PutMapping(value = "/upd/{id}/{name}")
    public Mono<Teacher> updTeacher(@PathVariable("id") int id, @PathVariable("name") String name) {
        Teacher t = Teacher.builder()
                            .id(id)
                            .name(name)
                            .build();
        teacherRepository.update(t.getId(), t.getName()).subscribe();
        return Mono.just(t);   
    }

    @DeleteMapping (value = "/del/{id}")
    public Mono<Void> deleteTeacher(@PathVariable("id") int id) {
        return teacherRepository.deleteById(Integer.toString(id));
        
    }

    

    @GetMapping (value = "/{id}")
    public Mono<Teacher> getAllTeachers(@PathVariable("id") int id) {
        Mono<Teacher> t = teacherRepository.findById(Integer.toString(id));
        return t;
    }

    @GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/all")
    public Flux<Teacher> teachers () {
        Flux<Teacher> teachers = teacherRepository.findAll();

        return teachers;
    }
    
}
