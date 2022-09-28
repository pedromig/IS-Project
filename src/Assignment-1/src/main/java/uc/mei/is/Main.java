package uc.mei.is;

import uc.mei.is.model.School;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class Main {
    public static void main(String[] args) {
        Dataset a = new Dataset("A", 800, 200);
        Dataset b = new Dataset("B", 200, 800);
        Dataset c = new Dataset("C", 500, 500);
        test("data.csv", 100, a, b, c);
    }

    private static void test(String filePath, int runs, Dataset... dataset) {

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.write("dataset,type,function,run,time\n");
            for (Dataset d : List.of(dataset)) {
                School school = d.school;
                System.err.println("=> Running dataset " + d.name + ":");
                for (int run = 1; run <= runs; ++run) {
                    System.err.print(" -> Run " + run + " ");

                    // Marshall ProtoBuf
                    out.write(csv(d.name, "bin", "write", String.valueOf(run),
                        String.valueOf(timeit(() -> school.writeToProto("a.bin")))
                    ));

                    // UnMarshall ProtoBuf
                    out.write(csv(d.name, "bin", "read", String.valueOf(run),
                        String.valueOf(timeit(() -> School.parseFromProto("a.bin")))
                    ));

                    // Marshall XML
                    out.write(csv(d.name, "xml", "write", String.valueOf(run),
                        String.valueOf(timeit(() -> school.writeToXml("b.xml")))
                    ));

                    // UnMarshall XML
                    out.write(csv(d.name, "xml", "read", String.valueOf(run),
                        String.valueOf(timeit(() -> School.parseFromXml("b.xml")))
                    ));

                    // Marshall XML + GZIP
                    out.write(csv(d.name, "xml.gz", "write", String.valueOf(run),
                        String.valueOf(timeit(() -> school.writeToXmlGzip("c.xml.gz")))
                    ));

                    // UnMarshall XML + GZIP
                    out.write(csv(d.name, "xml.gz", "read", String.valueOf(run), String.valueOf(
                        timeit(() -> School.parseFromXmlGZip("c.xml.gz")))
                    ));

                    System.err.println("DONE!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String csv(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : List.of(args)) {
            sb.append(arg).append(",");
        }
        sb.setCharAt(sb.length() - 1, '\n');
        return sb.toString();
    }

    private static long timeit(Runnable callback) {
        try {
            long start = System.currentTimeMillis();
            callback.run();
            long end = System.currentTimeMillis();
            return end - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class Dataset {

        protected String name;
        protected School school;
        protected int teacherCount;
        protected int studentCount;

        protected long seed = 42;

        protected int maxSupervisorStudents = 2;

        public Dataset(String name, int teacherCount, int studentsCount) {
            this.school = School.from(
                teacherCount,
                studentsCount,
                maxSupervisorStudents,
                Paths.get("src", "main", "resources", "addresses.csv").toString(),
                Paths.get("src", "main", "resources", "names.csv").toString(),
                seed
            );
            this.name = name;
            this.teacherCount = teacherCount;
            this.studentCount = studentsCount;
        }
    }
}

