package uc.mei.is.server.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Student;

public interface StudentRepository extends R2dbcRepository<Student, String> {

    @Query("INSERT INTO student (name, birth_date, credits, average) VALUES ($1, $2, $3, $4)")
    Mono<Student> save2(String name, LocalDateTime birth_date, int credits, Float average);

    @Query("UPDATE teacher SET name = $2, birth_date = $3, credits = $4, average = $5 WHERE teacher.id = $1")
    Mono<Student> update(int id, String name, LocalDateTime birth_date, int credits, Float average);
    
}
