package uc.mei.is.server.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import uc.mei.is.server.entity.Teacher;

public interface TeacherRepository extends R2dbcRepository<Teacher, Integer> {}