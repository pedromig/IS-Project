package uc.mei.is.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.repository.RelationshipRepository;

import org.apache.log4j.Logger;

@RestController
@RequestMapping("/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipRepository relationshipRepository;
    static Logger log = Logger.getLogger(RelationshipController.class.getName());

    @PostMapping
    public Mono<Relationship> create(@RequestBody Relationship relationship) {
        log.info("Relationship created: " + relationship.toString());
        return this.relationshipRepository.save(relationship);
    }

    @DeleteMapping
    public Mono<Void> update(@RequestBody Relationship relationship) {
        log.info("Relationship updated: " + relationship.toString());
        return this.relationshipRepository.deleteByTeacherIdOrStudentId(
            relationship.getTeacherId(),
            relationship.getStudentId()
        );
    }

}
