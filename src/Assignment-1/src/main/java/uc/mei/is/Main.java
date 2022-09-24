package uc.mei.is;

import uc.mei.is.model.School;
import uc.mei.is.model.Student;
import uc.mei.is.model.Teacher;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        School school = new School();

        Student tiago = new Student(1, "Tiago", 911111111, "Male", calendar, calendar, "Lisboa");
        Student maria = new Student(2, "Maria", 933333333, "Female", calendar, calendar, "Porto");
        Student antonio = new Student(3, "António", 912345678, "Male", calendar, calendar, "Coimbra");

        Teacher joao = new Teacher(1, "João", calendar, 919999999, "Coimbra");
        Teacher diogo = new Teacher(2, "Diogo", calendar, 918888888, "Coimbra");

        school.addStudent(tiago, maria, antonio);
        school.addTeacher(joao, diogo);

        school.setSupervisor(joao, tiago);
        school.setSupervisor(joao, maria);
        school.setSupervisor(diogo, antonio);

        System.out.println("Marshall Proto: " + timeit(() -> school.writeToProto("a.bin")));
        System.out.println("Unmarshall Proto: " + timeit(() -> School.parseFromProto("a.bin")));

        System.out.println("Marshall XML: " + timeit(() -> school.writeToXml("b.xml")));
        System.out.println("Unmarshall XML: " + timeit(() -> School.parseFromXml("b.xml")));

        System.out.println("Marshall XML + GZIP: " + timeit(() -> school.writeToXmlGzip("c.xml.gz")));
        System.out.println("Marshall XML + GZIP: " + timeit(() -> School.parseFromXmlGZip("c.xml.gz")));
    }

    public static long timeit(Runnable callback) {
        long start = System.currentTimeMillis();
        callback.run();
        long end = System.currentTimeMillis();
        return end - start;
    }
}
