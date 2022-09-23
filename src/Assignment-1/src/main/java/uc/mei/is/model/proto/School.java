package uc.mei.is.model.proto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class School {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    private final ArrayList<Student> students;
    private final ArrayList<Teacher> teachers;

    public School() {
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public School(ArrayList<Student> students, ArrayList<Teacher> teachers) {
        this.students = students; this.teachers = teachers;
    }


    public void addStudent(Student... students) {
        assert students != null;
        this.students.addAll(List.of(students));
    }

    public void addTeacher(Teacher... teachers) {
        assert teachers != null;
        this.teachers.addAll(List.of(teachers));
    }

    public void setSupervisor(Teacher teacher, Student student) {
        teacher.addStudent(student);
        student.setTeacher(teacher);
    }

    public uc.mei.is.model.proto.prototypes.School toProto() {
        return uc.mei.is.model.proto.prototypes.School.newBuilder()
                                                      .addAllStudent(this.students.stream()
                                                                                  .map(Student::toProto)
                                                                                  .toList())
                                                      .addAllTeacher(this.teachers.stream()
                                                                                  .map(Teacher::toProto)
                                                                                  .toList())
                                                      .build();
    }

    public static void writeTo(School school, String filePath) {
        try {
            // Marshall data
            FileOutputStream fos = new FileOutputStream(filePath);
            school.toProto().writeTo(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static School parseFrom(String filePath) {

        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Fetch unmarshal data
            uc.mei.is.model.proto.prototypes.School school = uc.mei.is.model.proto.prototypes.School.parseFrom(fis);

            // Parse students
            List<Student> students = school.getStudentList().stream().map(s -> {
                Calendar birthDate = Calendar.getInstance();
                Calendar registrationDate = Calendar.getInstance();
                try {
                    birthDate.setTime(FORMATTER.parse(s.getBirthDate()));
                    registrationDate.setTime(FORMATTER.parse(s.getRegistrationDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return new Student(s.getId(), s.getName(), s.getPhoneNumber(), s.getGender(),
                    birthDate, registrationDate, s.getAddress()
                );
            }).toList();

            // Parse teachers
            List<Teacher> teachers = school.getTeacherList().stream().map(t -> {
                Calendar birthDate = Calendar.getInstance();
                try {
                    birthDate.setTime(FORMATTER.parse(t.getBirthDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return new Teacher(t.getId(), t.getName(), birthDate, t.getPhoneNumber(), t.getAddress());
            }).toList();

            // Add relationships between students and teachers
            school.getTeacherList().forEach(t -> {
                Teacher teacher = teachers.stream()
                                          .filter(e -> e.getId() == t.getId())
                                          .findFirst()
                                          .orElse(null);
                assert teacher != null;

                for (Integer studentId : t.getStudentList()) {
                    Student student = students.stream()
                                              .filter(e -> e.getId() == studentId)
                                              .findFirst()
                                              .orElse(null);
                    assert student != null;
                    student.setTeacher(teacher);
                    teacher.addStudent(student);
                }
            });
            return new School(new ArrayList<>(students), new ArrayList<>(teachers));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
