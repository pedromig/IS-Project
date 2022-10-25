package uc.mei.is.server.repository;

import uc.mei.is.server.entity.Teacher;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends R2dbcRepository<Teacher, String> {
    @Query("SELECT * FROM teacher WHERE teacher.id = $1")
    Mono<Teacher> findById(int teacherId);

    @Query("UPDATE SET name = $2 WHERE teacher.id = $1")
    Mono<Teacher> updateById(int id, String name);

    @Query("INSERT INTO teacher (id, name) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET name = $2")
    Mono<Teacher> save2(int id, String name);

    //@Query("DELETE FROM teacher WHERE teacher.id = $1")
    //Mono<Teacher> deleteById(int id);

}