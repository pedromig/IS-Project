package uc.mei.is.server.entity;

import lombok.Builder;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Builder
public class Student {
    @Id
    private final int id;
    @Column("name")
    private final String name;
    @Column("birth_date")
    private final Date birth_date;
    @Column("credits")
    private final int credits;
    @Column("average")
    private final Float average;
    
}


