package uc.mei.is.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
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
    private List<Integer> studentsIds;


    public Teacher() {}

    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.studentsIds = new ArrayList<Integer>();
    }

    public Teacher(int id, String name, Calendar birthDate, int phoneNumber, String address, List<Integer> studentsIds) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.studentsIds = studentsIds;
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

    public List<Integer> getStudentsIds() {
        return this.studentsIds;
    }

    public void setStudentsIds(List<Integer> studentsIds) {
        this.studentsIds = studentsIds;
    }

    public void addStudent(Student student) {
        this.studentsIds.add(student.getId());
    }

    @Override
    public String toString() {
        String str = this.id + " _ " + this.name;
        return str;
    }

}
