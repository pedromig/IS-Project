package uc.mei.is.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.repository.RelationshipRepository;

@RestController
@RequestMapping("/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipRepository relationshipRepository;

    @PostMapping
    public Mono<Relationship> create(@RequestBody Relationship relationship) {
        return this.relationshipRepository.save(relationship);
    }

    @DeleteMapping
    public Mono<Void> update(@RequestBody Relationship relationship) {
        return this.relationshipRepository.deleteByTeacherIdOrStudentId(
            relationship.getTeacherId(),
            relationship.getStudentId()
        );
    }

}
