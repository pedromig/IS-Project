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

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;
    private final RelationshipRepository relationshipRepository;
    static Logger log = Logger.getLogger(StudentController.class.getName());


    @PostMapping
    public Mono<Student> create(@RequestBody Student student) {
        log.info("Student created: " + student.toString());
        return this.studentRepository.save(student);
    }

    @PutMapping
    public Mono<Student> update(@RequestBody Student student) {
        log.info("Student updated: " + student.toString());
        return this.studentRepository.findById(student.getId())
                                .flatMap(x -> this.studentRepository.save(student));
    }

    @GetMapping(value = "/list")
    public Flux<Student> getAll() {
        log.info("All students listed");
        return this.studentRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Student>> get(@PathVariable int id) {
        log.info("Student " + id + " listed");
        return this.studentRepository.findById(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@RequestBody Student student) {
        log.info("Student " + student.getId() + " deleted");
        return this.studentRepository.deleteById(student.getId());
    }

    @GetMapping(value = "/{id}/supervisors")
    public Flux<Integer> supervisorList(@PathVariable int id) {
        log.info("Supervisors of student " + id + " listed");
        return this.relationshipRepository.findAllByStudentId(id).map(Relationship::getTeacherId);
    }
}
