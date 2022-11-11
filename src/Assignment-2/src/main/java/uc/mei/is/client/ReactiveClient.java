package uc.mei.is.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import reactor.util.retry.Retry;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;
import uc.mei.is.server.entity.Student;
import uc.mei.is.server.entity.Teacher;

public class ReactiveClient {

    private static final String REPORT_DIR = "output";

    public static void main(String[] args) throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");

        /*
         * dataset A: Students: 20	Teachers: 20
         * dataset B: Students: 250	Teachers: 20
         * dataset C: Students: 20	Teachers: 250
         * dataset D: Students: 250	Teachers: 250
         * Max Relations: 5
         */

        /*try {
            Path filePath = Paths.get("data.csv");
            PrintWriter writer = new PrintWriter(new FileOutputStream(filePath.toString(), true));
            writer.write("dataset,type,time\n");
            writer.flush();
            for (int i = 0; i < 30; i++) {*/

                Flux<Student> students = client.get().uri("/student/list").retrieve().bodyToFlux(Student.class).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                                            //.publishOn(Schedulers.boundedElastic())
                                            ;
                Flux<Teacher> teachers = client.get().uri("/teacher/list").retrieve().bodyToFlux(Teacher.class).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                                                    //.publishOn(Schedulers.boundedElastic())
                                                    ;

                System.out.println("Elapsed Time: " + timeit(() -> run(client, students, teachers)));
                /*writer.write("C,block," + timeit(() -> run(client, students, teachers)) + "\n");
                writer.flush();
                System.out.println("Run " + i);
                TimeUnit.SECONDS.sleep(3);
            }
            writer.close();
        } catch (Exception e) {
            
        }*/
        
    }

    public static void run(WebClient client, Flux<Student> students, Flux<Teacher> teachers) {
        ArrayList<PrintWriter> reportFiles = ReactiveClient.openReportFiles();
        try {
            Semaphore lock = new Semaphore(1);
            ReactiveClient.getStudentNamesAndDates(students, lock, reportFiles.get(0));
            ReactiveClient.getStudentCount(students, lock, reportFiles.get(1));
            ReactiveClient.getActiveStudentsCount(students, lock, reportFiles.get(2));
            ReactiveClient.getTotalCompletedCourses(students, lock, reportFiles.get(3));
            ReactiveClient.getSortedLastYearStudents(students, lock, reportFiles.get(4));
            ReactiveClient.getGradeAverageAndStd(students, lock, reportFiles.get(5));
            ReactiveClient.getGradeAverageAndStdFinishedGraduation(students, lock, reportFiles.get(6));
            ReactiveClient.getEldestStudentName(students, lock, reportFiles.get(7));
            ReactiveClient.getAverageTeachersPerStudent(client, students, lock, reportFiles.get(8));
            ReactiveClient.getNameAndNumberOfStudentsPerTeacher(client, teachers, lock, reportFiles.get(9));
            ReactiveClient.getStudentData(client, students, lock, reportFiles.get(10));
            lock.acquire(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        closeReportFiles(reportFiles);
    }

    private static void getStudentNamesAndDates(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("name,birthdate");
        students.map(s -> Tuples.of(s.getName(), s.getBirthDate()))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getStudentCount(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("total");
        students.count().subscribe(report::println, System.err::println, lock::release);
    }

    private static void getActiveStudentsCount(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("active");
        students.filter(s -> s.getCredits() < 180)
                .count()
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getTotalCompletedCourses(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("total");
        students.map(s -> s.getCredits() / 6)
                .reduce(Integer::sum)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getSortedLastYearStudents(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("name,birthdate,credits,gpa");
        students.filter(s -> s.getCredits() >= 120 && s.getCredits() < 180)
                .sort((s1, s2) -> s2.getCredits() - s1.getCredits())
                .map(s -> Tuples.of(s.getName(), s.getBirthDate(), s.getCredits(), s.getGpa()))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getGradeAverageAndStd(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("avg,stddev");
        students.map(s -> Tuples.of(1.0, s.getGpa()))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .flatMap(avg -> students.map(s -> Tuples.of(1.0, Math.pow(s.getGpa() - avg, 2)))
                                        .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                                        .map(x -> Tuples.of(avg, Math.sqrt(((x.getT2() / x.getT1()))))))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getGradeAverageAndStdFinishedGraduation(Flux<Student> students, Semaphore lock,
                                                                PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("avg,stddev");
        students.filter(s -> s.getCredits() == 180)
                .map(s -> Tuples.of(1, s.getGpa()))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .flatMap(avg -> students.filter(s -> s.getCredits() == 180)
                                        .map(s -> Tuples.of(1.0, Math.pow(s.getGpa() - avg, 2)))
                                        .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                                        .map(x -> Tuples.of(avg, Math.sqrt(((x.getT2() / x.getT1()))))))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);
    }


    private static void getEldestStudentName(Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("name");
        students.reduce((acc, x) -> acc.getBirthDate().compareTo(x.getBirthDate()) > 0 ? acc : x)
                .map(Student::getName)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static void getAverageTeachersPerStudent(WebClient client, Flux<Student> students, Semaphore lock,
                                                     PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("avg");
        students.flatMap(s -> client.get()
                                    .uri("/student/" + s.getId() + "/supervisors")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                                    .count())
                .map(s -> Tuples.of(1, s))
                .reduce((acc, x) -> acc.mapT1(a -> a + x.getT1()).mapT2(b -> b + x.getT2()))
                .map(x -> x.getT2() / x.getT1())
                .subscribe(report::println, System.err::println, lock::release);
    }


    private static void getNameAndNumberOfStudentsPerTeacher(WebClient client, Flux<Teacher> teachers, Semaphore lock
        , PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("teacher,total,students");
        teachers.flatMap(t -> client.get()
                                    .uri("/teacher/" + t.getId() + "/students")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                                    .flatMap(i -> client.get()
                                                        .uri("/student/" + i)
                                                        .retrieve()
                                                        .bodyToMono(Student.class))
                                    .map(Student::getName)
                                    .reduce(new ArrayList<String>(), (acc, x) -> {acc.add(x); return acc;})
                                    .map(x -> Tuples.of(t.getName(), x)))
                .sort((a, b) -> b.getT2().size() - a.getT2().size())
                .map(x -> Tuples.of(x.getT1(), x.getT2().size(), x.getT2()))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);

    }

    private static void getStudentData(WebClient client, Flux<Student> students, Semaphore lock, PrintWriter report)
        throws InterruptedException {
        lock.acquire();
        report.println("name,birthdate,credits,gpa,teachers");
        students.flatMap(s -> client.get()
                                    .uri("/student/" + s.getId() + "/supervisors")
                                    .retrieve()
                                    .bodyToFlux(Integer.class)
                                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                                    .flatMap(i -> client.get()
                                                        .uri("/teacher/" + i)
                                                        .retrieve()
                                                        .bodyToMono(Teacher.class))
                                    .map(Teacher::getName)
                                    .reduce(new ArrayList<String>(), (acc, x) -> {acc.add(x); return acc;})
                                    .map(x -> Tuples.of(s.getName(), s.getBirthDate(), s.getCredits(), s.getGpa(), x)))
                .map(ReactiveClient::tupleToCsv)
                .subscribe(report::println, System.err::println, lock::release);
    }

    private static long timeit(Runnable callback) {
        try {
            long start = System.currentTimeMillis();
            callback.run();
            long end = System.currentTimeMillis();
            return end - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<PrintWriter> openReportFiles() {
        ArrayList<PrintWriter> files = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(REPORT_DIR))) {
                Files.createDirectories(Paths.get(REPORT_DIR));
            }

            for (int i = 1; i <= 11; ++i) {
                Path filePath = Paths.get(REPORT_DIR, "ex" + i + ".csv");
                files.add(new PrintWriter(new FileOutputStream(filePath.toString())));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    private static void closeReportFiles(ArrayList<PrintWriter> files) {
        for (PrintWriter file : files) {
            file.close();
        }
    }

    public static <Tuple extends Iterable<Object>> String tupleToCsv(Tuple tuple) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : tuple) {
            if (obj instanceof ArrayList) {
                for (Object o : ((ArrayList<?>) obj)) {
                    sb.append(o.toString()).append("|");
                }
            } else {
                sb.append(obj.toString()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}