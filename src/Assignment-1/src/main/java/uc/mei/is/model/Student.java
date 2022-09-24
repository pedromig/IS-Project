package uc.mei.is.model;

import jakarta.xml.bind.annotation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// @XmlType(propOrder = {"name", "age"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Student implements Serializable {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final int NO_TEACHER = -1;

    @XmlAttribute
    private int id;

    @XmlElement
    private String name;

    @XmlElement
    private String gender;

    @XmlElement
    private String address;

    @XmlElement
    private int phoneNumber;

    @XmlElement
    private Calendar birthDate;

    @XmlElement
    private Calendar registrationDate;

    @XmlElement(name = "teacherId")
    private int teacher;

    public Student() {}

    public Student(int id, String name, int phoneNumber, String gender,
                   Calendar birthDate, Calendar registrationDate, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
        this.teacher = NO_TEACHER;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher.getId();
    }

    public int getId() {
        return this.id;
    }

    public uc.mei.is.model.proto.Student toProto() {
        return uc.mei.is.model.proto.Student.newBuilder()
                                            .setId(this.id)
                                            .setName(this.name)
                                            .setPhoneNumber(this.phoneNumber)
                                            .setGender(this.gender)
                                            .setBirthDate(FORMATTER.format(this.birthDate.getTime()))
                                            .setRegistrationDate(FORMATTER.format(this.registrationDate.getTime()))
                                            .setAddress(this.address)
                                            .setTeacher(this.teacher)
                                            .build();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Student(");
        sb.append("id = ").append(this.id);
        sb.append(", name = '").append(this.name).append('\'');
        sb.append(", gender = '").append(this.gender).append('\'');
        sb.append(", address = '").append(this.address).append('\'');
        sb.append(", phoneNumber = ").append(this.phoneNumber);
        sb.append(", birthDate = ").append(FORMATTER.format(this.birthDate.getTime()));
        sb.append(", registrationDate = ").append(FORMATTER.format(this.registrationDate.getTime()));
        sb.append(", teacherId = ").append(this.teacher);
        sb.append(')');
        return sb.toString();
    }
}
