package uc.mei.is;

import uc.mei.is.model.School;
import uc.mei.is.model.Student;
import uc.mei.is.model.Teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        long seed = 1000;
        int supervisorChance = 95;  // 0 to 100 %
        int nTeachers = 200;
        int nStudents = 500;
        School school = generateCases(seed, nTeachers, nStudents, "src\\main\\resources\\addresses.csv", "src\\main\\resources\\names.csv", supervisorChance);

        int repeat = 50;

        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();

        results.add(loopTest(repeat, () -> school.writeToProto("a.bin"), "True,Proto"));
        results.add(loopTest(repeat, () -> School.parseFromProto("a.bin"), "False,Proto"));
        results.add(loopTest(repeat, () -> school.writeToXml("b.xml"), "True,XML"));
        results.add(loopTest(repeat, () -> School.parseFromXml("b.xml"), "False,XML"));
        results.add(loopTest(repeat, () -> school.writeToXmlGzip("c.xml.gz"), "True,XML + GZIP"));
        results.add(loopTest(repeat, () -> School.parseFromXmlGZip("c.xml.gz"), "False,XML + GZIP"));

        writeFile("res.csv", results, nTeachers, nStudents, seed);
    }

    public static void writeFile(String path, ArrayList<ArrayList<String>> results, int nTeachers, int nStudents, long seed) {
        try {
            PrintWriter out = new PrintWriter(path);
            out.write("Marshall,Technology,Number Teachers,Number Students,Seed,Run,Time\n");
            for (ArrayList<String> line : results) {
                for (int i = 1; i < line.size(); i++) {
                    out.write(line.get(0) + "," + nTeachers + "," + nStudents + "," + seed + "," + i + "," + line.get(i) + "\n");
                }
            }
             out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> loopTest(Integer repeat, Runnable callback, String title) {
        ArrayList<String> arr = new ArrayList<String>(List.of(title));
        for (int i = 0; i < repeat; i++) {
            long start = System.currentTimeMillis();
            callback.run();
            long end = System.currentTimeMillis();
            arr.add(String.valueOf(end - start));
        }
        return arr;
    }
    

    public static long timeit(Runnable callback) {
        long start = System.currentTimeMillis();
        callback.run();
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static School generateCases(Long seed, Integer nTeachers, Integer nStudents, String filenameAdresses, String filenameNames, Integer supervisorChance) {

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
            if(rnd.nextInt(100) < supervisorChance) {
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
