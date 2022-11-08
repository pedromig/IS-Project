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

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;
    private final RelationshipRepository relationshipRepository;

    @PostMapping
    public Mono<Student> create(@RequestBody Student student) {
        return this.studentRepository.save(student);
    }

    @PutMapping
    public Mono<Student> update(@RequestBody Student student) {
        return this.studentRepository.findById(student.getId())
                                .flatMap(x -> this.studentRepository.save(student));
    }

    @GetMapping(value = "/list")
    public Flux<Student> getAll() {
        return this.studentRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Student>> get(@PathVariable int id) {
        return this.studentRepository.findById(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return this.studentRepository.deleteById(id);
    }

    @GetMapping(value = "/{id}/supervisors")
    public Flux<Integer> supervisorList(@PathVariable int id) {
        return this.relationshipRepository.findAllByStudentId(id).map(Relationship::getTeacherId);
    }
}
