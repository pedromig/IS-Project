package uc.mei.is.server.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;

public interface RelationshipRepository extends R2dbcRepository<Relationship, Integer> {
    Mono<Void> deleteByTeacherIdOrStudentId(int teacherId, int studentId);
    Flux<Relationship> findAllByTeacherId(int teacherId);

    Flux<Relationship> findAllByStudentId(int studentId);

}
