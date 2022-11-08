package uc.mei.is.server.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import uc.mei.is.server.entity.Student;

public interface StudentRepository extends R2dbcRepository<Student, Integer> {}
