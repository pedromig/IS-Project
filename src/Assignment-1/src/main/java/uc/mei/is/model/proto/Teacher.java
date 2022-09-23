package uc.mei.is.model.proto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class Teacher {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

    private final int id;

    private final String name;
    private final int phoneNumber;
    private final String address;
    private final Calendar birthDate;
    private final ArrayList<Student> students;


    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address) {
        this.id = id; this.name = name; this.birthDate = birthDate; this.phoneNumber = phoneNumber;
        this.address = address; this.students = new ArrayList<>();
    }

    public void addStudent(Student... students) {
        assert students != null;
        this.students.addAll(List.of(students));
    }


    public int getId() {
        return this.id;
    }

    public uc.mei.is.model.proto.prototypes.Teacher toProto() {
        return uc.mei.is.model.proto.prototypes.Teacher.newBuilder()
                                                       .setId(this.id)
                                                       .setName(this.name)
                                                       .setBirthDate(FORMATTER.format(this.birthDate.getTime()))
                                                       .setPhoneNumber(this.phoneNumber)
                                                       .setAddress(this.address)
                                                       .addAllStudent(students.stream()
                                                                              .map(Student::getId)
                                                                              .toList())
                                                       .build();
    }
}
