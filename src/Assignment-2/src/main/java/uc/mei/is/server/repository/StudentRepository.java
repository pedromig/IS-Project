package uc.mei.is.server.repository;

import java.util.Date;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Student;

public interface StudentRepository extends R2dbcRepository<Student, String> {

    @Query("INSERT INTO student (id, name, birth_date, credits, average) VALUES ($1, $2, $3, $4, $5) ON CONFLICT (id) DO UPDATE SET name = $2, birth_date = $3, credits = $4, average = $5")
    Mono<Student> save2(int id, String name, Date birth_date, int credits, Float average);
    
}
