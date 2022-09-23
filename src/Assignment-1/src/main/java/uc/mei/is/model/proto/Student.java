package uc.mei.is.model.proto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Student {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

    private final int id;
    private final String name;
    private final String gender;
    private final String address;
    private final int phoneNumber;
    private final Calendar birthDate;
    private final Calendar registrationDate;

    private Teacher teacher;


    public Student(int id, String name, int phoneNumber, String gender,
                   Calendar birthDate, Calendar registrationDate, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
        this.teacher = null;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public uc.mei.is.model.proto.prototypes.Student toProto() {
        return uc.mei.is.model.proto.prototypes.Student.newBuilder()
                                                       .setId(this.id)
                                                       .setName(this.name)
                                                       .setPhoneNumber(this.phoneNumber)
                                                       .setGender(this.gender)
                                                       .setBirthDate(FORMATTER.format(this.birthDate.getTime()))
                                                       .setRegistrationDate(FORMATTER.format(this.registrationDate.getTime()))
                                                       .setAddress(this.address)
                                                       .setTeacher(this.teacher.getId())
                                                       .build();
    }

    public int getId() {
        return this.id;
    }
}
