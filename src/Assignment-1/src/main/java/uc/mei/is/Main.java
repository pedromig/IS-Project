package uc.mei.is;

import uc.mei.is.model.School;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        Dataset a = new Dataset("A", 10, 50, 2, 42);
        Dataset b = new Dataset("B", 800, 200, 2, 42);
        Dataset c = new Dataset("C", 200, 800, 2, 42);
        Dataset d = new Dataset("D", 500, 500, 2, 42);
        test("data.csv", 50, a, b, c, d);
    }

    private static void test(String filePath, int runs, Dataset... dataset) {
        try (PrintWriter out = new PrintWriter(filePath)) {
            // Header
            out.write("dataset,type,function,run,time,size\n");

            // Tmp files
            Path bin = Paths.get("test.bin");
            Path xml = Paths.get("test.xml");
            Path xmlGz = Paths.get("test.xml.gz");

            // Generate Data
            for (Dataset d : List.of(dataset)) {
                School school = d.school;
                System.err.println("=> Running dataset " + d.name + ":");

                // Runs
                for (int run = 1; run <= runs; ++run) {
                    System.err.print(" -> Run " + run + " ");

                    // ProtoBuf
                    long writeBin = timeit(() -> school.writeToProto(bin.toString()));
                    out.write(csv(d.name, "bin", "write", String.valueOf(run),
                        String.valueOf(writeBin),
                        String.valueOf(toKiB(Files.size(bin)))
                    ));

                    long readBin = timeit(() -> School.parseFromProto(bin.toString()));
                    out.write(csv(d.name, "bin", "read", String.valueOf(run),
                        String.valueOf(readBin),
                        String.valueOf(toKiB(Files.size(bin)))
                    ));

                    // XML
                    long writeXml = timeit(() -> school.writeToXml(xml.toString()));
                    out.write(csv(d.name, "xml", "write", String.valueOf(run),
                        String.valueOf(writeXml),
                        String.valueOf(toKiB(Files.size(xml)))
                    ));

                    long readXml = timeit(() -> School.parseFromXml(xml.toString()));
                    out.write(csv(d.name, "xml", "read", String.valueOf(run),
                        String.valueOf(readXml),
                        String.valueOf(toKiB(Files.size(xml)))
                    ));

                    // XML + GZIP
                    long readXmlGz = timeit(() -> school.writeToXmlGzip(xmlGz.toString()));
                    out.write(csv(d.name, "xml.gz", "write", String.valueOf(run),
                        String.valueOf(readXmlGz),
                        String.valueOf(toKiB(Files.size(xmlGz)))
                    ));

                    long writeXmlGz = timeit(() -> School.parseFromXmlGZip(xmlGz.toString()));
                    out.write(csv(d.name, "xml.gz", "read", String.valueOf(run),
                        String.valueOf(writeXmlGz),
                        String.valueOf(toKiB(Files.size(xmlGz)))
                    ));
                    System.err.println("DONE!");
                }
            }

            // Cleanup...
            Files.deleteIfExists(bin);
            Files.deleteIfExists(xml);
            Files.deleteIfExists(xmlGz);
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

    private static long toKiB(long size) {
        return size / 1024;
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

        public Dataset(String name, int teacherCount, int studentsCount, int maxSupervisorStudents, int seed) {
            this.name = name;
            this.teacherCount = teacherCount;
            this.studentCount = studentsCount;
            this.seed = seed;
            this.maxSupervisorStudents = maxSupervisorStudents;
            this.school = School.from(
                teacherCount,
                studentsCount,
                maxSupervisorStudents,
                Paths.get("src", "main", "resources", "addresses.csv").toString(),
                Paths.get("src", "main", "resources", "names.csv").toString(),
                seed
            );
        }
    }
}

