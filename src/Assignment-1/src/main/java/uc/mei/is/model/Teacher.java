package uc.mei.is.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Stream;

@XmlAccessorType(XmlAccessType.FIELD)
public class Teacher implements Serializable {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @XmlAttribute
    private int id;

    @XmlElement
    private String name;

    @XmlElement
    private int phoneNumber;

    @XmlElement
    private String address;

    @XmlElement
    private Calendar birthDate;

    @XmlElement(name = "studentId")
    private ArrayList<Integer> students;

    public Teacher() {}

    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student... students) {
        assert students != null;
        this.students.addAll(Stream.of(students).map(Student::getId).toList());
    }


    public int getId() {
        return this.id;
    }

    public uc.mei.is.model.proto.Teacher toProto() {
        return uc.mei.is.model.proto.Teacher.newBuilder()
                                            .setId(this.id)
                                            .setName(this.name)
                                            .setBirthDate(FORMATTER.format(this.birthDate.getTime()))
                                            .setPhoneNumber(this.phoneNumber)
                                            .setAddress(this.address)
                                            .addAllStudent(this.students)
                                            .build();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Teacher(");
        sb.append("id = ").append(this.id);
        sb.append(", name = '").append(this.name).append('\'');
        sb.append(", phoneNumber = ").append(this.phoneNumber);
        sb.append(", address = '").append(this.address).append('\'');
        sb.append(", studentIds = ").append(this.students).append(')');
        return sb.toString();
    }
}
