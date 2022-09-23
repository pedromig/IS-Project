package uc.mei.is.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class School {
    private List<Teacher> teachers;
    private List<Student> students;


    public School() {};
    
    public School(List<Teacher> teachers, List<Student> students) {
        this.teachers = teachers;
        this.students = students;
    }

    public List<Teacher> getTeachers() {
		return this.teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

    public List<Student> getStudents() {
		return this.students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

    @Override
    public String toString() {
        String str = "Teachers:\n";
        for(Teacher teacher : teachers) {
            str += "\t" + teacher.toString() + "\n";
        }
        str += "Students:\n";
        for(Student student : students) {
            str += "\t" + student.toString() + "\n";
        }
        return str;
    }
    
}
