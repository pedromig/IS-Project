package uc.mei.is.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.entity.Student;

//import org.springframework.test.context.junit4.SpringRunner;
//import org.junit.runner.RunWith;

import uc.mei.is.server.entity.Teacher;


//@RunWith(SpringRunner.class)
@SpringBootApplication
public class ReactiveClientLoader {
    private static int nStudents = 100;
    private static int nTeachers = 100;
    private static int nRelationships = 500;

    private static ArrayList<Teacher> teachers;
    private static ArrayList<Student> students;
    private static ArrayList<Relationship> relationships;
    private static int maxNSupervisors = 10;
    private static int maxNStudents = 10;
    private static int seed = 42;

    
    public static void main(String [] args) throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");

        fillDatabase(client);  
    }

    private static void fillDatabase (WebClient client) {
        generateDataset(5, 2, 10, "src/main/java/uc/mei/is/client/resources/names.txt", 42);

        for (Teacher t : teachers) { 
            client.post().uri("/teacher/add/" + t.getName()).retrieve().bodyToMono(Teacher.class).subscribe();
        }
        System.out.println("Teachers added");

        for (Student s : students) {
            client.post().uri("/student/add/" + s.getName() + "/" + s.getBirth_date() + "/" + s.getCredits() + "/" + s.getAverage()).retrieve().bodyToMono(Student.class).subscribe();
        }
        System.out.println("Students added");

        // wait for all requests to complete
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException detected: " + e.getMessage());
            return;
        }

        for (Relationship r : relationships) {
            client.post().uri("/relationship/add/" + r.getStudent_id() + "/" + r.getTeacher_id()).retrieve().bodyToMono(Relationship.class).subscribe();
        }
        System.out.println("Relationships added\nAll data added");
    }


    private static void generateDataset(int nStudents, int nTeachers, int nRelationships, String namesPath, long seed) {
        Random rng = new Random(seed);

        ArrayList<String> names = loadData(namesPath);

        ArrayList<Integer> teacherIds = randomIds(nTeachers, rng);
        ArrayList<Integer> studentIds = randomIds(nStudents, rng);

        students = new ArrayList<Student>();
        for (int i = 0; i < nStudents; i++) {
            students.add(new Student(studentIds.get(i), names.get(rng.nextInt(nStudents)), randomCalendar(rng), randomCredits(rng, 0.3), randomAverage(rng, 0.7)));
        }
        
        teachers = new ArrayList<Teacher>();
        for (int i = 0; i < nTeachers; i++) {
            teachers.add(new Teacher(teacherIds.get(i), names.get(rng.nextInt(nStudents))));
        }

        relationships = new ArrayList<Relationship>();
        for (int i = 0; i < nRelationships; i++) {
            int tId = rng.nextInt(nTeachers);
            while(testMaxRelationships(teachers.get(tId).getId(), relationships, true, maxNStudents)) {
                System.out.println(tId);
                tId = rng.nextInt(nTeachers);
            }

            int sId = rng.nextInt(nStudents);
            while(testMaxRelationships(students.get(sId).getId(), relationships, false, maxNSupervisors)) {
                sId = rng.nextInt(nStudents);
            }

            if(testRelationship(sId, tId, relationships)) {
                relationships.add(new Relationship(students.get(sId).getId(), teachers.get(tId).getId()));
            } else {
                i -= 1;
                System.out.println("repeated");
            }
        }
        for (Relationship r : relationships) { 		      
            System.out.println(r.toString()); 		
        }

    }

    private static Boolean testRelationship(int sId, int tId, ArrayList<Relationship> array) {
        
        if(Arrays.stream(array.toArray(new Relationship[array.size()])).anyMatch(e -> e.getStudent_id() == sId && e.getTeacher_id() == tId)) {
            return false;
        }
        return true;
    }

    private static Boolean testMaxRelationships(int id, ArrayList<Relationship> array, Boolean searchTeacher, int maxAmount) {
        int count = 0;
        for (Relationship r : array) {
            if(searchTeacher) {
                if(r.getTeacher_id() == id) {
                    count += 1;
                }
            } else {
                if(r.getStudent_id() == id) {
                    count += 1;
                }
            }
        }
        if(count > maxAmount) {
            return true;
        }
        return false;
    }

    private static ArrayList<String> loadData(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner s = new Scanner(new File(filePath))) {
            while (s.hasNextLine()) {
                lines.add(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private static ArrayList<Integer> randomIds(int size, Random rng) {
        Set<Integer> idsOut = new HashSet<>();
        while (idsOut.size() != size) {
            idsOut.add(Math.abs(rng.nextInt()));
        }
        return new ArrayList<>(idsOut);
    }

    private static LocalDateTime randomCalendar(Random rng) {
        Calendar gc = Calendar.getInstance();

        int max = gc.getActualMaximum(Calendar.DAY_OF_YEAR);
        gc.set(Calendar.YEAR, rng.nextInt(1900, 2010));
        gc.set(Calendar.DAY_OF_YEAR, rng.nextInt(1, max));

        return LocalDateTime.ofInstant(gc.toInstant(), gc.getTimeZone().toZoneId());
    }

    private static int randomCredits(Random rng, Double percentFinished) {
        if(rng.nextFloat() > percentFinished) {
            return 180;
        }
        return rng.nextInt(0,180);
    }
    private static float randomAverage(Random rng, Double percentPositive) {
        if(rng.nextFloat() > percentPositive) {
            return Math.round(10 + rng.nextFloat() * 10 * 100) / 100;
        }
        return Math.round(rng.nextFloat() * 10 * 100) / 100;
    }

}


