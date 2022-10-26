package uc.mei.is.server.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Student;
import uc.mei.is.server.repository.StudentRepository;


@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentRepository studentRepository;

    // this can also be used to update students
    // date format yyyy-mm-dd
    @PostMapping (value = "/add/{name}/{birth_date}/{credits}/{average}")
    public Mono<Student> addStudent(@PathVariable("name") String name, @PathVariable("birth_date") String birth_date,
            @PathVariable("credits") int credits,  @PathVariable("average") Float average) {

        Student s = Student.builder()
                            .name(name)
                            .birth_date(LocalDate.parse(birth_date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay())
                            .credits(credits)
                            .average(average)
                            .build();
        studentRepository.save2(s.getName(), s.getBirth_date(), s.getCredits(), s.getAverage()).subscribe();
        return Mono.just(s);   
                 
    }

    @PutMapping(value = "/upd/{id}/{name}/{birth_date}/{credits}/{average}")
    public Mono<Student> updStudent(@PathVariable("id") int id,
            @PathVariable("name") String name, @PathVariable("birth_date") String birth_date,
            @PathVariable("credits") int credits,  @PathVariable("average") Float average) {

 
        //LocalDateTime dateTime = LocalDateTime.parse(birth_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Student s = Student.builder()
                            .id(id)
                            .name(name)
                            .birth_date(LocalDate.parse(birth_date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay())
                            .credits(credits)
                            .average(average)
                            .build();
        studentRepository.update(s.getId(), s.getName(), s.getBirth_date(), s.getCredits(), s.getAverage()).subscribe();
        return Mono.just(s);   

    }

    @DeleteMapping (value = "/del/{id}")
    public Mono<Void> deleteStudent(@PathVariable("id") int id) {
        return studentRepository.deleteById(Integer.toString(id));
        
    }

    @GetMapping (value = "/{id}")
    public Mono<Student> getAllStudents(@PathVariable("id") int id) {
        Mono<Student> s = studentRepository.findById(Integer.toString(id));
        return s;
    }

    @GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/all")
    public Flux<Student> students () {
        Flux<Student> students = studentRepository.findAll();

        return students;
    }

    
}
