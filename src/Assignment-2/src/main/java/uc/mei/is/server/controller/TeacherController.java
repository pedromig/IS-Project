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

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final RelationshipRepository relationshipRepository;

    @PostMapping
    public Mono<Teacher> create(@RequestBody Teacher teacher) {
        return this.teacherRepository.save(teacher);
    }

    @PutMapping
    public Mono<Teacher> update(@RequestBody Teacher teacher) {
        return this.teacherRepository.findById(teacher.getId())
                                     .flatMap(x -> this.teacherRepository.save(teacher));
    }

    @GetMapping(value = "/list")
    public Flux<Teacher> getAll() {
        return this.teacherRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Teacher>> get(@PathVariable int id) {
        return this.teacherRepository.findById(id)
                                     .map(ResponseEntity::ok)
                                     .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@RequestBody Teacher teacher) {
        return this.teacherRepository.deleteById(teacher.getId());
    }

    @GetMapping(value = "/{id}/students")
    public Flux<Integer> studentList(@PathVariable int id) {
        return this.relationshipRepository.findAllByTeacherId(id)
                                     .map(Relationship::getStudentId);
    }

}
