package uc.mei.is.server.entity;

import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Builder
public class Relationship {
    @Id
    private final int id;
    @Column("student_id")
    private final int student_id;
    @Column("teacher_id")
    private final int teacher_id;
    
}


