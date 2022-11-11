package uc.mei.is.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.entity.Teacher;
import uc.mei.is.server.repository.RelationshipRepository;
import uc.mei.is.server.repository.TeacherRepository;

import java.util.logging.Level;

import org.apache.log4j.Logger;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final RelationshipRepository relationshipRepository;
    private static Logger log = Logger.getLogger(TeacherController.class.getName());

    @PostMapping
    public Mono<Teacher> create(@RequestBody Teacher teacher) {
        return this.teacherRepository.save(teacher)
                                     .log(log.getName(), Level.FINEST)
                                     .doOnSubscribe(s -> log.info("Teacher created: " + teacher.toString()))
                                     .onErrorContinue((ex, value) -> log.warn(
                                         "Unexpected error while handling {}",
                                         ex
                                     ));
    }

    @PutMapping
    public Mono<Teacher> update(@RequestBody Teacher teacher) {
        return this.teacherRepository.findById(teacher.getId())
                                     .flatMap(x -> this.teacherRepository.save(teacher))
                                     .log(log.getName(), Level.FINEST)
                                     .doOnSubscribe(s -> log.info("Teacher updated: " + teacher.toString()))
                                     .onErrorContinue((ex, value) -> log.warn(
                                         "Unexpected error while handling {}",
                                         ex
                                     ));
    }

    @GetMapping(value = "/list")
    public Flux<ResponseEntity<Teacher>> getAll() {
        return this.teacherRepository.findAll()
                                     .map(ResponseEntity::ok)
                                     .defaultIfEmpty(ResponseEntity.notFound().build())
                                     .log(log.getName(), Level.FINEST)
                                     .doOnSubscribe(s -> log.info("All teachers listed"))
                                     .onErrorContinue((ex, value) -> log.warn(
                                         "Unexpected error while handling {}",
                                         ex
                                     ));
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Teacher>> get(@PathVariable int id) {
        return this.teacherRepository.findById(id)
                                     .map(ResponseEntity::ok)
                                     .defaultIfEmpty(ResponseEntity.notFound().build())
                                     .log(log.getName(), Level.FINEST)
                                     .doOnSubscribe(s -> log.info("Teacher " + id + " listed"))
                                     .onErrorContinue((ex, value) -> log.warn(
                                         "Unexpected error while handling {}",
                                         ex
                                     ));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return this.teacherRepository.deleteById(id)
                                     .log(log.getName(), Level.FINEST)
                                     .doOnSubscribe(s -> log.info("Teacher " + id + " deleted"))
                                     .onErrorContinue((ex, value) -> log.warn(
                                         "Unexpected error while handling {}",
                                         ex
                                     ));

    }

    @GetMapping(value = "/{id}/students")
    public Flux<ResponseEntity<Integer>> studentList(@PathVariable int id) {
        log.info("Students of teacher " + id + " listed");
        return this.relationshipRepository.findAllByTeacherId(id)
                                          .map(Relationship::getStudentId)
                                          .map(ResponseEntity::ok)
                                          .defaultIfEmpty(ResponseEntity.notFound().build())
                                          .log(log.getName(), Level.FINEST)
                                          .doOnSubscribe(s -> log.info("Students of teacher " + id + " listed"))
                                          .onErrorContinue((ex, value) -> log.warn(
                                              "Unexpected error while handling {}",
                                              ex
                                          ));
    }

}
