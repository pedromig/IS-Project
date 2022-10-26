package uc.mei.is.server.entity;

import lombok.Builder;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Builder
public class Student {
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("birth_date")
    private LocalDateTime birth_date;
    @Column("credits")
    private int credits;
    @Column("average")
    private Float average;

    public Student() {}

    public Student(int id, String name, LocalDateTime birth_date, int credits, Float average) {
        this.id = id;
        this.name = name;
        this.birth_date = birth_date;
        this.credits = credits;
        this.average = average;
    }
}


