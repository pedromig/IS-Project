package uc.mei.is.server.repository;

import uc.mei.is.server.entity.Teacher;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends R2dbcRepository<Teacher, String> {
  @Query("SELECT * FROM teacher WHERE teacher.id = $1")
  Mono<Teacher> findById(int teacherId);

  Flux<Teacher> findAll();

  /*@Query("INSERT INTO teacher (id, name) VALUES (1, 'joão'); ")
  void add(Teacher t);*/

}