package uc.mei.is.client;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;
import uc.mei.is.server.entity.Student;
import uc.mei.is.server.entity.Teacher;

public class ReactiveClient {

    public static void main(String[] args) throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");

        Flux<Student> students = client.get().uri("/student/list").retrieve().bodyToFlux(Student.class);
        Flux<Teacher> teachers = client.get().uri("/teacher/list").retrieve().bodyToFlux(Teacher.class);

        System.out.println("Elapsed Time: " + timeit(() -> run(client, students, teachers)));
    }

    public static void run(WebClient client, Flux<Student> students, Flux<Teacher> teachers) {
        try {
            Semaphore lock = new Semaphore(11);
            ReactiveClient.getStudentNamesAndDates(students, lock);
            ReactiveClient.getStudentCount(students, lock);
            ReactiveClient.getActiveStudentsCount(students, lock);
            ReactiveClient.getTotalCompletedCourses(students, lock);
            ReactiveClient.getSortedLastYearStudents(students, lock);
            ReactiveClient.getGradeAverageAndStd(students, lock);
            ReactiveClient.getGradeAverageAndStdFinishedGraduation(students, lock);
            ReactiveClient.getEldestStudentName(students, lock);
            ReactiveClient.getAverageTeachersPerStudent(client, students, lock);
            ReactiveClient.getNameAndNumberOfStudentsPerTeacher(client, teachers, lock);
            ReactiveClient.getStudentData(client, students, lock);
            ReactiveClient.getNameAndNumberOfStudentsPerTeacher(client, teachers, lock);
            ReactiveClient.getStudentData(client, students, lock);
            lock.acquire(11);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void getStudentNamesAndDates(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.map(s -> Tuples.of(s.getName(), s.getBirthDate()))
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getStudentCount(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.count().subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getActiveStudentsCount(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.filter(s -> s.getCredits() < 180)
                .count()
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getTotalCompletedCourses(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.map(s -> s.getCredits() / 6)
                .reduce(Integer::sum)
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getSortedLastYearStudents(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.filter(s -> s.getCredits() >= 120 && s.getCredits() < 180)
                .sort((s1, s2) -> s2.getCredits() - s1.getCredits())
                .map(s -> Tuples.of(s.getName(), s.getBirthDate(), s.getCredits(), s.getGpa()))
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getGradeAverageAndStd(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.map(s -> Tuples.of(1.0, s.getGpa()))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .flatMap(avg -> students.map(s -> Tuples.of(1.0, Math.pow(s.getGpa() - avg, 2)))
                                        .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                                        .map(x -> Tuples.of(avg, Math.sqrt(((x.getT2() / x.getT1()))))))
                .subscribe(System.out::println, System.err::println, lock::release);
    }


    private static void getGradeAverageAndStdFinishedGraduation(Flux<Student> students, Semaphore lock)
        throws InterruptedException {
        lock.acquire();
        students.filter(s -> s.getCredits() == 180)
                .map(s -> Tuples.of(1, s.getGpa()))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .flatMap(avg -> students.filter(s -> s.getCredits() == 180)
                                        .map(s -> Tuples.of(1.0, Math.pow(s.getGpa() - avg, 2)))
                                        .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                                        .map(x -> Tuples.of(avg, Math.sqrt(((x.getT2() / x.getT1()))))))
                .subscribe(System.out::println, System.err::println, lock::release);
    }


    private static void getEldestStudentName(Flux<Student> students, Semaphore lock) throws InterruptedException {
        lock.acquire();
        students.reduce((acc, x) -> acc.getBirthDate().compareTo(x.getBirthDate()) > 0 ? acc : x)
                .map(Student::getName)
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static void getAverageTeachersPerStudent(WebClient client, Flux<Student> students, Semaphore lock)
        throws InterruptedException {
        lock.acquire();
        students.flatMap(s -> client.get()
                                    .uri("/student/" + s.getId() + "/supervisors")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .count())
                .map(s -> Tuples.of(1, s))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .subscribe(System.out::println, System.err::println, lock::release);
    }


    private static void getNameAndNumberOfStudentsPerTeacher(WebClient client, Flux<Teacher> teachers, Semaphore lock)
        throws InterruptedException {
        lock.acquire();
        teachers.flatMap(t -> client.get()
                                    .uri("/teacher/" + t.getId() + "/students")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .flatMap(i -> client.get()
                                                        .uri("/student/" + i)
                                                        .retrieve()
                                                        .bodyToMono(Student.class))
                                    .map(Student::getName)
                                    .reduce(new ArrayList<String>(), (acc, x) -> {acc.add(x); return acc;})
                                    .map(x -> Tuples.of(t.getName(), x)))
                .sort((a, b) -> b.getT2().size() - a.getT2().size())
                .map(x -> Tuples.of(x.getT1(), x.getT2().size(), x.getT2()))
                .subscribe(System.out::println, System.err::println, lock::release);

    }

    private static void getStudentData(WebClient client, Flux<Student> students, Semaphore lock)
        throws InterruptedException {
        lock.acquire();
        students.flatMap(s -> client.get()
                                    .uri("/student/" + s.getId() + "/supervisors")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .flatMap(i -> client.get()
                                                        .uri("/teacher/" + i)
                                                        .retrieve()
                                                        .bodyToMono(Teacher.class))
                                    .map(Teacher::getName)
                                    .reduce(new ArrayList<String>(), (acc, x) -> {acc.add(x); return acc;})
                                    .map(x -> Tuples.of(s.getName(), s.getBirthDate(), s.getCredits(), s.getGpa(), x)))
                .subscribe(System.out::println, System.err::println, lock::release);
    }

    private static <T> void dump(String filePath, T data, Function<T, String> callback) {
        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(callback.apply(data));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static long timeit(Runnable callback) {
        try {
            long start = System.currentTimeMillis(); callback.run(); long end = System.currentTimeMillis();
            return end - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}