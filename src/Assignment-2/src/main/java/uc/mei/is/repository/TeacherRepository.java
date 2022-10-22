package uc.mei.is.repository;

import uc.mei.is.entity.Teacher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends R2dbcRepository<Teacher, String> {
  //Mono<Teacher> findByMemberId(String memberId);
}