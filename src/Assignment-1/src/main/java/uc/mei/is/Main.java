package uc.mei.is;

import uc.mei.is.model.School;
import uc.mei.is.model.Student;
import uc.mei.is.model.Teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        long seed = 1000;
        int supervisorChange = 95;  // 0 to 100 %
        School school = generateCases(seed, 200, 500, "src\\main\\resources\\addresses.csv", "src\\main\\resources\\names.csv",supervisorChange);
    
        /*Calendar calendar = Calendar.getInstance();

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
        school.setSupervisor(diogo, antonio);*/

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

    public static School generateCases(Long seed, Integer nTeachers, Integer nStudents, String filenameAdresses, String filenameNames, Integer supervisorChange) {

        // read files
        ArrayList<String> addresses = loadFile(filenameAdresses);
        ArrayList<String> names = loadFile(filenameNames);

        // random generator
        Random rnd = new Random();
        rnd.setSeed(seed);

        ArrayList<Integer> idsOutT = generateIds(rnd, nTeachers);
        ArrayList<Integer> idsOutS = generateIds(rnd, nStudents);

        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        ArrayList<Student> students = new ArrayList<Student>();

        for(int i = 0; i < nTeachers; i++) {
            teachers.add(new Teacher(idsOutT.remove(0), names.get(rnd.nextInt(names.size())), 
                                    generateCalendar(rnd), randomNumberSize(rnd, 9), 
                                    addresses.get(rnd.nextInt(addresses.size()))));
        }

        for(int i = 0; i < nStudents; i++) {
            String gender = "";
            if(rnd.nextBoolean()) {
                gender = "Male";
            } else { gender = "Female";}

            students.add(new Student(idsOutS.remove(0), names.get(rnd.nextInt(names.size())), 
                                    randomNumberSize(rnd, 9), gender, generateCalendar(rnd), 
                                    generateCalendar(rnd), addresses.get(rnd.nextInt(addresses.size()))));
        }

        School school = new School(students, teachers);

        // add supervisors
        for(Student s : students) {
            if(rnd.nextInt(100) < supervisorChange) {
                Teacher t = teachers.get(rnd.nextInt(teachers.size()));
                school.setSupervisor(t, s);
            }
        }

        return school;
    }

    public static ArrayList<String> loadFile(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        try (Scanner s = new Scanner(new File(fileName))) {
            while (s.hasNextLine()){
                lines.add(s.nextLine());
            }
            s.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return lines;
    }

    public static ArrayList<Integer> generateIds(Random rnd, Integer number) {
        Set<Integer> idsOut = new HashSet<>();

        while(idsOut.size() != number) {
            idsOut.add(Math.abs(rnd.nextInt()));
        }

        return new ArrayList<Integer>(idsOut);
    }

    public static Calendar generateCalendar(Random rnd) {
        Calendar gc = Calendar.getInstance();

        int year = rnd.nextInt(1900, 2010);
        int dayOfYear = rnd.nextInt(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return gc;
    }

    public static Integer randomNumberSize(Random rnd, int digCount) {
        StringBuilder sb = new StringBuilder(digCount);
        for(int i=0; i < digCount; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return Integer.parseInt(sb.toString());
    }

}
