package uc.mei.is.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class School implements Serializable {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @XmlElement(name = "student")
    private final ArrayList<Student> students;

    @XmlElement(name = "teacher")
    private final ArrayList<Teacher> teachers;

    public School() {
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public School(ArrayList<Student> students, ArrayList<Teacher> teachers) {
        this.students = students; this.teachers = teachers;
    }

    public void addStudent(Student... students) {
        assert students != null;
        this.students.addAll(List.of(students));
    }

    public void addTeacher(Teacher... teachers) {
        assert teachers != null;
        this.teachers.addAll(List.of(teachers));
    }

    public void setSupervisor(Teacher teacher, Student student) {
        teacher.addStudent(student);
        student.setTeacher(teacher);
    }

    public uc.mei.is.model.proto.School toProto() {
        return uc.mei.is.model.proto.School.newBuilder()
                                           .addAllStudent(this.students.stream()
                                                                       .map(Student::toProto)
                                                                       .toList())
                                           .addAllTeacher(this.teachers.stream()
                                                                       .map(Teacher::toProto)
                                                                       .toList())
                                           .build();
    }

    public void writeTo(String filePath) {
        String ext = Optional.ofNullable(filePath)
                             .filter(f -> f.contains("."))
                             .map(f -> f.substring(filePath.indexOf(".") + 1))
                             .orElseThrow();
        switch (ext) {
            case "xml" -> this.writeToXml(filePath);
            case "xml.gz" -> this.writeToXmlGzip(filePath);
            case "bin" -> this.writeToProto(filePath);
            default -> throw new RuntimeException("Invalid file extension!");
        }
    }

    public void writeToXmlGzip(String filePath) {
        try {
            // Marshall
            JAXBContext jaxbContext = JAXBContext.newInstance(School.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(filePath.substring(0, filePath.lastIndexOf(".")));
            jaxbMarshaller.marshal(this, file);

            // Zip
            try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(filePath));
                 FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    gos.write(buffer, 0, length);
                }
            }
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToXml(String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(School.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(this, new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void writeToProto(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            this.toProto().writeTo(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static School parseFrom(String filePath) {
        String ext = Optional.ofNullable(filePath)
                             .filter(f -> f.contains("."))
                             .map(f -> f.substring(filePath.indexOf(".") + 1))
                             .orElseThrow();
        return switch (ext) {
            case "xml" -> School.parseFromXml(filePath);
            case "xml.gz" -> School.parseFromXmlGZip(filePath);
            case "bin" -> School.parseFromProto(filePath);
            default -> throw new RuntimeException("Invalid file extension!");
        };
    }

    public static School parseFromXmlGZip(String filePath) {
        School school = null;
        try {
            // Unzip
            File file = new File(filePath.substring(0, filePath.lastIndexOf(".")));
            try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(filePath));
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];

                int totalSize;
                while ((totalSize = gis.read(buffer)) > 0) {
                    fos.write(buffer, 0, totalSize);
                }
            }
            // Unmarshall
            JAXBContext jaxbContext = JAXBContext.newInstance(School.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            school = (School) jaxbUnmarshaller.unmarshal(file);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }
        return school;
    }

    public static School parseFromXml(String filePath) {
        School school = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(School.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            school = (School) jaxbUnmarshaller.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return school;
    }

    public static School parseFromProto(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Fetch unmarshal data
            uc.mei.is.model.proto.School school = uc.mei.is.model.proto.School.parseFrom(fis);

            // Parse students
            List<Student> students = school.getStudentList().stream().map(s -> {
                Calendar birthDate = Calendar.getInstance();
                Calendar registrationDate = Calendar.getInstance();
                try {
                    birthDate.setTime(FORMATTER.parse(s.getBirthDate()));
                    registrationDate.setTime(FORMATTER.parse(s.getRegistrationDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return new Student(s.getId(), s.getName(), s.getPhoneNumber(), s.getGender(),
                    birthDate, registrationDate, s.getAddress()
                );
            }).toList();

            // Parse teachers
            List<Teacher> teachers = school.getTeacherList().stream().map(t -> {
                Calendar birthDate = Calendar.getInstance();
                try {
                    birthDate.setTime(FORMATTER.parse(t.getBirthDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return new Teacher(t.getId(), t.getName(), birthDate, t.getPhoneNumber(), t.getAddress());
            }).toList();

            // Add relationships between students and teachers
            school.getTeacherList().forEach(t -> {
                Teacher teacher = teachers.stream()
                                          .filter(e -> e.getId() == t.getId())
                                          .findFirst()
                                          .orElse(null);
                assert teacher != null;

                for (Integer studentId : t.getStudentList()) {
                    Student student = students.stream()
                                              .filter(e -> e.getId() == studentId)
                                              .findFirst()
                                              .orElse(null);
                    assert student != null;
                    student.setTeacher(teacher);
                    teacher.addStudent(student);
                }
            });
            return new School(new ArrayList<>(students), new ArrayList<>(teachers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Teachers:\n");
        for (Teacher teacher : teachers) {
            str.append("\t").append(teacher.toString()).append("\n");
        }
        str.append("Students:\n");
        for (Student student : students) {
            str.append("\t").append(student.toString()).append("\n");
        }
        return str.toString();
    }
}
