package uc.mei.is.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    private int id;

    @Column
    private String name;

    @Column("birth_date")
    private LocalDateTime birthDate;

    @Column
    private int credits;

    @Column
    private float gpa;
}


