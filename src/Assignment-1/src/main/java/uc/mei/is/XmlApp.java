package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import uc.mei.is.model.xml.Teacher;
import uc.mei.is.model.xml.School;
import uc.mei.is.model.xml.Student;

public class XmlApp {

    public static void main(String[] args) {
        test1("xml.xml"); unmarshallData("xml.xml");
    }


    private static void test1(String fileName) {
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        ArrayList<Student> students = new ArrayList<Student>();

        Calendar c1 = Calendar.getInstance();
        teachers.add(new Teacher(1, "João", c1, 919999999, "Coimbra"));
        teachers.add(new Teacher(2, "Diogo", c1, 918888888, "Coimbra"));

        students.add(new Student(1, "Tiago", 911111111, "Male", c1, c1, "Lisboa"));
        students.add(new Student(2, "Maria", 933333333, "Female", c1, c1, "Porto"));
        students.add(new Student(3, "António", 912345678, "Male", c1, c1, "Coimbra"));

        addRelStudentTeacher(students.get(0), teachers.get(0));
        addRelStudentTeacher(students.get(1), teachers.get(1));
        addRelStudentTeacher(students.get(2), teachers.get(0));

        School school = new School(teachers);


        if (marshallData(school, fileName)) {
            System.out.println("All fine");
        } else {
            System.out.println("Error");
        }


    }

    private static void addRelStudentTeacher(Student student, Teacher teacher) {
        student.setTeacherId(teacher.getId());
        teacher.addStudent(student);
    }

    private static boolean marshallData(School school, String fileName) {
        JAXBContext jaxbContext = null;

        try {

            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(new Class[]{ School.class }, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // output to a xml file
            jaxbMarshaller.marshal(school, new File(fileName));


            // output to console
            //jaxbMarshaller.marshal(school, System.out);

            return true;

        } catch (JAXBException e) {
            e.printStackTrace();
        } return false;
    }

    private static boolean unmarshallData(String fileName) {
        JAXBContext jaxbContext = null;

        try {

            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(new Class[]{ School.class }, null);

            File file = new File(fileName);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            School o = (School) jaxbUnmarshaller.unmarshal(file);

            System.out.println(o); return true;

        } catch (JAXBException e) {
            e.printStackTrace();
        } return false;

    }
}