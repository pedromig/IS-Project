package uc.mei.is.server.entity;

import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Builder
public class Relationship {
    @Id
    private int id;
    @Column("student_id")
    private int student_id;
    @Column("teacher_id")
    private int teacher_id;


    public Relationship() {}

    public Relationship(int student_id, int teacher_id) {
        this.student_id = student_id;
        this.teacher_id = teacher_id;
    }

    public Relationship(int id, int student_id, int teacher_id) {
        this.id = id;
        this.student_id = student_id;
        this.teacher_id = teacher_id;
    }
    
}


