package uc.mei.is.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.reactive.function.client.WebClient;

import uc.mei.is.server.entity.Relationship;
import uc.mei.is.server.entity.Student;

import uc.mei.is.server.entity.Teacher;

public class ReactiveDatabaseLoader {
    private static final int nStudents = 10;
    private static final int nTeachers = 10;
    private static final int maxSupervisors = 3;
    private static final int maxStudents = 3;
    private static final int seed = 42;

    public static void main(String[] args) {
        WebClient client = WebClient.create("http://localhost:8080");
        datasetToDB(client, seed);
    }

    private static void datasetToDB(WebClient client, long seed) {
        Random rng = new Random(seed);

        ArrayList<String> names = loadData(Paths.get("src", "main", "resources", "names.txt").toString());

        ArrayList<Teacher> teachers = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        for (int i = 0; i < nStudents; i++) {
            int credits = randomCredits(rng, 0.3f);
            float gpa = randomGpa(rng, credits);
            students.add(createStudent(
                client,
                names.get(rng.nextInt(names.size())),
                randomCalendar(rng),
                credits,
                gpa
            ));
        }

        for (int i = 0; i < nTeachers; i++) {
            teachers.add(
                createTeacher(
                    client,
                    names.get(rng.nextInt(names.size()))
                ));
        }


        for (Teacher teacher : teachers) {
            int n = rng.nextInt(maxStudents + 1);
            HashSet<Integer> studentSet = new HashSet<>();
            for (int i = 0; i < n; ++i) {
                studentSet.add(1 + rng.nextInt(students.size() - 1));
            }
            for (Integer studentId : studentSet) {
                createRelationship(client, teacher.getId(), studentId);
            }
        }

        for (Student student : students) {
            int n = rng.nextInt(maxSupervisors + 1);
            HashSet<Integer> teacherSet = new HashSet<>();
            for (int i = 0; i < n; ++i) {
                teacherSet.add(1 + rng.nextInt(teachers.size() - 1));
            }
            for (Integer teacherId : teacherSet) {
                createRelationship(client, teacherId, student.getId());
            }
        }
    }


    public static Teacher createTeacher(WebClient client, String name) {
        return client.post()
                     .uri("/teacher")
                     .bodyValue(Teacher.builder()
                                       .name(name)
                                       .build())
                     .retrieve()
                     .bodyToMono(Teacher.class)
                     .block();
    }

    public static Student createStudent(WebClient client, String name, LocalDateTime birthDate, int credits,
                                        float gpa) {
        return client.post()
                     .uri("/student")
                     .bodyValue(Student.builder().name(name)
                                       .birthDate(birthDate)
                                       .credits(credits)
                                       .gpa(gpa)
                                       .build())
                     .retrieve()
                     .bodyToMono(Student.class)
                     .block();
    }

    public static void createRelationship(WebClient client, int teacher, int student) {
        client.post()
              .uri("/relationship")
              .bodyValue(Relationship.builder()
                                     .teacherId(teacher)
                                     .studentId(student)
                                     .build())
              .retrieve()
              .bodyToMono(Relationship.class)
              .block();
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

    private static LocalDateTime randomCalendar(Random rng) {
        Calendar gc = Calendar.getInstance();

        int max = gc.getActualMaximum(Calendar.DAY_OF_YEAR);
        gc.set(Calendar.YEAR, 1900 + rng.nextInt(2010 - 1900));
        gc.set(Calendar.DAY_OF_YEAR, 1 + rng.nextInt(max - 1));

        return LocalDateTime.ofInstant(gc.toInstant(), gc.getTimeZone().toZoneId());
    }

    private static int randomCredits(Random rng, float completed) {
        return rng.nextFloat() < completed ? 180 : rng.nextInt(180);
    }

    private static float randomGpa(Random rng, int credits) {
        ArrayList<Float> grades = new ArrayList<>();
        for (int i = 0; i < credits / 6; ++i) {
            grades.add((float) (9.5 + rng.nextFloat() * 10));
        }
        float sum = 0;
        for (Float f : grades) {
            sum += f;
        }
        return grades.size() == 0 ? 0 : sum / grades.size();
    }
}
