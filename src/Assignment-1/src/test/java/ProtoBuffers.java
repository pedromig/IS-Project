import uc.mei.is.model.proto.School;
import uc.mei.is.model.proto.Student;
import uc.mei.is.model.proto.Teacher;

import java.util.ArrayList;
import java.util.Calendar;

public class ProtoBuffers {

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

        School.writeTo(school, "test.bin");
        School sameSchool = School.parseFrom("test.bin");

        System.out.println(sameSchool.toProto());
    }
}
