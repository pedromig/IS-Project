package uc.mei.is.server.repository;

import uc.mei.is.server.entity.Teacher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TeacherRepository extends R2dbcRepository<Teacher, String> {
  //Mono<Teacher> findByMemberId(String memberId);
}