package uc.mei.is.server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;

public interface RelationshipRepository extends R2dbcRepository<Relationship, String> {
    
    @Query("INSERT INTO relationship (student_id, teacher_id) VALUES ($1, $2)")
    Mono<Relationship> save2(int student_id, int teacher_id);

    @Query("DELETE FROM relationship WHERE student_id = $1 AND teacher_id = $2")
    Mono<Void> deleteByStudentTeacherIds(int student_id, int teacher_id);

    @Query("SELECT teacher_id FROM relationship WHERE student_id = $1")
    Flux<Integer> findTeacherIdsOfStudent(int student_id);

    @Query("SELECT student_id FROM relationship WHERE teacher_id = $1")
    Flux<Integer> findStudentIdsOfTeacher(int teacher_id);
}
