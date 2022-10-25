package uc.mei.is.client;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import uc.mei.is.server.entity.Student;

//import org.springframework.test.context.junit4.SpringRunner;
//import org.junit.runner.RunWith;

import uc.mei.is.server.entity.Teacher;


//@RunWith(SpringRunner.class)
@SpringBootApplication
public class ReactiveClient {
    
    @Bean
    WebClient client () {
        return WebClient.create("http://localhost:8080");
    }

    // Teacher tests
    /*@Bean
    CommandLineRunner ListAllTeachers (WebClient client) {
        return args -> {

            client
                .get()
                .uri("/teacher/all")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Teacher.class)
                .subscribe(System.out::println);
        };
    }*/

    /*@Bean
    CommandLineRunner addTeacher (WebClient client) {
        return args -> {

            client
                .post()
                .uri("/teacher/add/4/dionisio")
                .retrieve()
                .bodyToMono(Teacher.class)
                .subscribe(System.out::println);
        };

    }*/

    /*@Bean
    CommandLineRunner deleteTeacher (WebClient client) {
        return args -> {

            client
                .delete()
                .uri("/teacher/del/4")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
        };

    }*/

    /*@Bean
    CommandLineRunner updateTeacher (WebClient client) {
        return args -> {

            client
                .delete()
                .uri("/teacher/upd/5/dionisio")
                .retrieve()
                .bodyToMono(Teacher.class)
                .subscribe(System.out::println);
        };

    }*/
    
    // Student tests

    @Bean
    CommandLineRunner ListAllStudents (WebClient client) {
        return args -> {

            client
                .get()
                .uri("/student/all")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Student.class)
                .subscribe(System.out::println);
        };
    }

    /*@Bean     // works
    CommandLineRunner addStudent (WebClient client) {
        return args -> {

            client
                .post()
                .uri("/student/add/4/pereira/2001-03-12/60/15.2")
                .retrieve()
                .bodyToMono(Student.class)
                .subscribe(System.out::println);
        };
    }*/

    /*@Bean       // works
    CommandLineRunner deleteStudent (WebClient client) {
        return args -> {

            client
                .delete()
                .uri("/student/del/4")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
        };

    }*/

    /*@Bean     // works
    CommandLineRunner updateStudent (WebClient client) {
        return args -> {

            client
                .put()
                .uri("/student/upd/1/marcia/2001-6-12/60/19.5")
                .retrieve()
                .bodyToMono(Student.class)
                .subscribe(System.out::println);
        };

    }*/



    public static void main(String [] args) {
        new SpringApplicationBuilder(ReactiveClient.class)
            .properties(Collections.singletonMap("server.port", "8081"))    
            .run(args);
    }

}
