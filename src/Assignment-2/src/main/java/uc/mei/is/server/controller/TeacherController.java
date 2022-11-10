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

import org.apache.log4j.Logger;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final RelationshipRepository relationshipRepository;
    private static Logger log = Logger.getLogger(StudentController.class.getName());

    @PostMapping
    public Mono<Teacher> create(@RequestBody Teacher teacher) {
        log.info("Teacher created: " + teacher.toString());
        return this.teacherRepository.save(teacher);
    }

    @PutMapping
    public Mono<Teacher> update(@RequestBody Teacher teacher) {
        log.info("Teacher updated: " + teacher.toString());
        return this.teacherRepository.findById(teacher.getId())
                                     .flatMap(x -> this.teacherRepository.save(teacher));
    }

    @GetMapping(value = "/list")
    public Flux<Teacher> getAll() {
        log.info("All teachers listed");
        return this.teacherRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Teacher>> get(@PathVariable int id) {
        log.info("Teacher " + id + " listed");
        return this.teacherRepository.findById(id)
                                     .map(ResponseEntity::ok)
                                     .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@RequestBody Teacher teacher) {
        log.info("Teacher " + teacher.getId() + " deleted");
        return this.teacherRepository.deleteById(teacher.getId());
    }

    @GetMapping(value = "/{id}/students")
    public Flux<Integer> studentList(@PathVariable int id) {
        log.info("Students of teacher " + id + " listed");
        return this.relationshipRepository.findAllByTeacherId(id)
                                     .map(Relationship::getStudentId);
    }

}
