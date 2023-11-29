import org.maxmalts.key_value_storage_database.Student;
import org.maxmalts.key_value_storage_database.StudentKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class SpringConfig {
    @Bean
    public StudentKey studentKey1() {
        return new StudentKey(591, "Vasya Pukin");
    }

    @Bean
    public Student student1() {
        return new Student(
            studentKey1().getGroupId(),
            studentKey1().getName(),
            "Vasyuki",
            new Date(1996, 4, 14),
            true,
            7.8
        );
    }

    @Bean
    public StudentKey studentKey2() {
        return new StudentKey(591, "Ahmad Ben Hafiz");
    }

    @Bean
    public Student student2() {
        return new Student(
            studentKey2().getGroupId(),
            studentKey2().getName(),
            "Cairo",
            new Date(1432, 9, 2),
            false,
            3.3
        );
    }

    @Bean
    public StudentKey studentKey3() {
        return new StudentKey(599, "John Smith");
    }

    @Bean
    public Student student3() {
        return new Student(
            studentKey2().getGroupId(),
            studentKey2().getName(),
            "Glasgow",
            new Date(1874, 3, 8),
            true,
            9.1
        );
    }
}
