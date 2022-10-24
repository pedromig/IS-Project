package uc.mei.is.server.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Builder
public class Teacher {
    @Id
    private final int id;
    @Column("name")
    private final String name;

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
