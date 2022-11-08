package uc.mei.is.server.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Relationship {
    @Id
    private int id;

    @Column("student_id")
    private int studentId;

    @Column("teacher_id")
    private int teacherId;
}


