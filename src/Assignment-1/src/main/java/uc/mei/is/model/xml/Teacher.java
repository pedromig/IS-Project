package uc.mei.is.model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Teacher {
    @XmlAttribute
    private int id;

    private String name;
    private Calendar birthDate;
    private int phoneNumber;
    private String address;
    private ArrayList<Student> students;

    public Teacher() {}

    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.students = new ArrayList<Student>();
    }

    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address, ArrayList<Student> students) {
        this(id, name, birthDate, phoneNumber, address);
        this.students = students;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(this.id + " _ " + this.name + "\n\tStudents:\n");
        for (Student student : students) {
            str.append("\t\t").append(student.toString()).append("\n");
        }
        return str.toString();
    }

}
