package uc.mei.is.server.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Builder
public class Teacher {
    @Id
    @JsonProperty("id")
    private final int id;
    @JsonProperty("name")
    private final String name;

    @JsonCreator
    public Teacher(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

}
