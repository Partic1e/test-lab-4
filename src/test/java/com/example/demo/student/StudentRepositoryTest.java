package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {

    @AfterEach
    void clearAll(){
        studentRepository.deleteAll();
    }

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void ifStudentExistThenReturnTrue() {
        Student student = new Student("Test", "TestEmail@data.com", Gender.MALE);
        studentRepository.save(student);

        Boolean result = studentRepository.selectExistsEmail("TestEmail@data.com");

        assertEquals(true, result);

    }

    @Test
    void ifStudentDoesNotExistThenReturnFalse() {
        //arrange

        //act
        Boolean result = studentRepository.selectExistsEmail("t@mail.ru");

        //assert
        assertFalse(result);
    }
}
