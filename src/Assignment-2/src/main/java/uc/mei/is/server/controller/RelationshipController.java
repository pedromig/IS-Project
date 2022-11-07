package uc.mei.is.server.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.repository.RelationshipRepository;

@RestController
@RequestMapping("/relationship")
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipRepository relationshipRepository;

    @PostMapping (value = "/add/{student_id}/{teacher_id}")
    public Mono<Relationship> addRelationship(@PathVariable("student_id") int student_id, @PathVariable("teacher_id") int teacher_id) {

        Relationship r = Relationship.builder()
                                    .student_id(student_id)
                                    .teacher_id(teacher_id)
                                    .build();
        
        relationshipRepository.save2(r.getStudent_id(), r.getTeacher_id()).subscribe();
        return Mono.just(r);                      
    }


    // TODO probably remove this
    @GetMapping (value = "/{id}")
    public Mono<Relationship> getRelationshipById(@PathVariable("id") int id) {
        Mono<Relationship> r = relationshipRepository.findById(Integer.toString(id));
        return r;
    }

    @GetMapping (value = "/findTeachers/{student_id}")
    public Flux<Integer> getStudentIds(@PathVariable("student_id") int student_id) {
        return relationshipRepository.findTeacherIdsOfStudent(student_id);
    }

    @GetMapping (value = "/findStudents/{teacher_id}")
    public Flux<Integer> getTeacherIds(@PathVariable("teacher_id") int teacher_id) {
        return relationshipRepository.findStudentIdsOfTeacher(teacher_id);
    }

    @DeleteMapping (value = "/del/{student_id}/{teacher_id}")
    public Mono<Void> deleteRelationship(@PathVariable("student_id") int student_id, @PathVariable("teacher_id") int teacher_id) {
        return relationshipRepository.deleteByStudentTeacherIds(student_id, teacher_id);
        
    }


    
}
