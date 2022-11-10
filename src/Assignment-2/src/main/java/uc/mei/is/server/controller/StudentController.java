package uc.mei.is.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.entity.Student;
import uc.mei.is.server.repository.RelationshipRepository;
import uc.mei.is.server.repository.StudentRepository;

import org.apache.log4j.Logger;
import java.util.logging.Level;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;
    private final RelationshipRepository relationshipRepository;
    private static Logger log = Logger.getLogger(StudentController.class.getName());


    @PostMapping
    public Mono<Student> create(@RequestBody Student student) {
        return this.studentRepository.save(student)
                                    .log(log.getName(), Level.FINEST)
                                    .doOnSubscribe(s -> log.info("Student created: " + student.toString()))            
                                    .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @PutMapping
    public Mono<Student> update(@RequestBody Student student) {
        return this.studentRepository.findById(student.getId())
                                .flatMap(x -> this.studentRepository.save(student))
                                .log(log.getName(), Level.FINEST)
                                .doOnSubscribe(s -> log.info("Student updated: " + student.toString()))            
                                .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @GetMapping(value = "/list")
    public Flux<Student> getAll() {
        return this.studentRepository.findAll()
                                    .log(log.getName(), Level.FINEST)
                                    .doOnSubscribe(s -> log.info("All students listed"))            
                                    .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Student>> get(@PathVariable int id) {
        return this.studentRepository.findById(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build())
                                .log(log.getName(), Level.FINEST)
                                .doOnSubscribe(s -> log.info("Student " + id + " listed"))            
                                .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@RequestBody Student student) {
        return this.studentRepository.deleteById(student.getId())
                                    .log(log.getName(), Level.FINEST)
                                    .doOnSubscribe(s -> log.info("Student " + student.getId() + " deleted"))            
                                    .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @GetMapping(value = "/{id}/supervisors")
    public Flux<Integer> supervisorList(@PathVariable int id) {
        return this.relationshipRepository.findAllByStudentId(id).map(Relationship::getTeacherId)
                                        .log(log.getName(), Level.FINEST)
                                        .doOnSubscribe(s -> log.info("Supervisors of student " + id + " listed"))            
                                        .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }
}
