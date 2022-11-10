package uc.mei.is.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.repository.RelationshipRepository;

import java.util.logging.Level;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipRepository relationshipRepository;
    private static Logger log = Logger.getLogger(RelationshipController.class.getName());

    @PostMapping
    public Mono<Relationship> create(@RequestBody Relationship relationship) {
        return this.relationshipRepository.save(relationship)
                            .log(log.getName(), Level.FINEST)
                            .doOnSubscribe(s -> log.info("Relationship created: " + relationship.toString()))            
                            .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

    @DeleteMapping
    public Mono<Void> update(@RequestBody Relationship relationship) {
        return this.relationshipRepository.deleteByTeacherIdOrStudentId(
            relationship.getTeacherId(),
            relationship.getStudentId()
        ).log(log.getName(), Level.FINEST)
        .doOnSubscribe(s -> log.info("Relationship updated: " + relationship.toString()))            
        .onErrorContinue((ex, value) -> log.warn("Unexpected error while handling {}", ex));
    }

}
