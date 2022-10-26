package uc.mei.is.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
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

    /*@Bean
    CommandLineRunner fillDatabase (WebClient client) {
        return args -> { 
            client.post().uri("/teacher/add/Maria").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/teacher/add/Pedro").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/teacher/add/Marcia").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/teacher/add/António").retrieve().bodyToMono(Teacher.class).subscribe();
            System.out.println("Teachers added");

            client.post().uri("/student/add/João/01-01-2000/30/12").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/student/add/Rodrigo/01-01-2000/150/17").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/student/add/Miguel/01-01-2000/120/19.5").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/student/add/Sofia/01-01-2000/180/17.2").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/student/add/Bruno/01-01-2000/174/12.3").retrieve().bodyToMono(Teacher.class).subscribe();
            System.out.println("Students added");
            
            // wait for all requests to complete
            TimeUnit.SECONDS.sleep(1);

            client.post().uri("/relationship/add/1/1").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/relationship/add/3/1").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/relationship/add/4/4").retrieve().bodyToMono(Teacher.class).subscribe();
            client.post().uri("/relationship/add/5/3").retrieve().bodyToMono(Teacher.class).subscribe();
            System.out.println("Relationships added\nAll data added");
        };
    }*/

    @Bean
    CommandLineRunner ex1 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .map(s -> s.getName() + " ==> " + 
                            s.getBirth_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .reduce((total, cur) -> total + "\n" + cur)
                    .subscribe(s -> writeToFile("outputs/ex1.txt", s));
                    
            System.out.println("Ex1 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }

    @Bean
    CommandLineRunner ex2 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .count()
                    .subscribe(s -> writeToFile("outputs/ex2.txt", String.valueOf(s)));
            System.out.println("Ex2 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }

    @Bean
    CommandLineRunner ex3 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .filter(s -> s.getCredits() < 180)
                    .count()
                    .subscribe(s -> writeToFile("outputs/ex3.txt", String.valueOf(s)));
            System.out.println("Ex3 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }

    @Bean
    CommandLineRunner ex4 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .map(s -> s.getCredits() / 6)
                    .reduce((counter, cur) -> counter + cur)
                    .subscribe(s -> writeToFile("outputs/ex4.txt", String.valueOf(s)));
            System.out.println("Ex4 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }

    @Bean
    CommandLineRunner ex5 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .filter(s -> 120 <= s.getCredits() && s.getCredits() < 180)
                    .sort((s1, s2) -> s2.getCredits() - s1.getCredits())
                    
                    .map(s -> s.getName() + " ==> " + s.getCredits())
                    .reduce((total, cur) -> total + "\n" + cur)
                    .subscribe(s -> writeToFile("outputs/ex5.txt", String.valueOf(s)));
            System.out.println("Ex5 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }

    /*@Bean
    CommandLineRunner ex6 (WebClient client) {
        return args -> {
            Instant start = Instant.now();
            client.get()
                    .uri("/student/all")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    //.filter(s -> s.getCredits() == 180)
                    .concatMap(null)
                    
                    .map(s -> s.getName() + " ==> " + s.getCredits())
                    .reduce((total, cur) -> total + "\n" + cur)
                    .subscribe(s -> writeToFile("outputs/ex6.txt", String.valueOf(s)));
            System.out.println("Ex6 done: " + durationFormat(Duration.between(start, Instant.now()))); 
        };
    }*/

    public static void main(String [] args) {
        new SpringApplicationBuilder(ReactiveClient.class)
            .properties(Collections.singletonMap("server.port", "8081"))    
            .run(args);
        
    }

    public Boolean writeToFile(String filePath, String content) {
        try {
            new File(filePath.substring(0, filePath.lastIndexOf("/"))).mkdirs();

            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(content);
            myWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String durationFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

}


/// test functionalities 
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

        /*@Bean
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
        }*/

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
                    .uri("/student/upd/2/marcia/2001-06-12/60/19.5")
                    .retrieve()
                    .bodyToMono(Student.class)
                    .subscribe(System.out::println);
            };

        }*/

        // Relationship tests

        /*@Bean     // works
        CommandLineRunner addRelationship (WebClient client) {
            return args -> {

                client
                    .post()
                    .uri("/relationship/add/4/3")
                    .retrieve()
                    .bodyToMono(Relationship.class)
                    .subscribe(System.out::println);
            };
        }*/

        /*@Bean       // works
        CommandLineRunner getRelationship (WebClient client) {
            return args -> {

                client
                    .get()
                    .uri("/relationship/1")
                    .retrieve()
                    .bodyToMono(Relationship.class)
                    .subscribe(System.out::println);
            };
        }*/

        /*@Bean       // works
        CommandLineRunner getTeachers (WebClient client) {
            return args -> {

                client
                    .get()
                    .uri("/relationship/findTeachers/4")
                    .retrieve()
                    .bodyToFlux(Integer.class)
                    .subscribe(System.out::println);
            };
        }*/

        /*@Bean       // works
        CommandLineRunner getStudents (WebClient client) {
            return args -> {

                client
                    .get()
                    .uri("/relationship/findStudents/3")
                    .retrieve()
                    .bodyToFlux(Integer.class)
                    .subscribe(System.out::println);
            };
        }*/

        /*@Bean       // works
        CommandLineRunner deleteRelationship (WebClient client) {
            return args -> {

                client
                    .delete()
                    .uri("/relationship/del/4/3")
                    .retrieve()
                    .bodyToFlux(Void.class)
                    .subscribe(System.out::println);
            };
        }*/

