package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    void getAllStudents() {
        studentService.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent() {
        Student student = new Student("Ivan", "ivan@mail.cru", Gender.MALE);
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        studentService.addStudent(student);

        // Проверка, что метод save был вызван с правильным аргументом
        verify(studentRepository).save(captor.capture());
        Student capturedStudent = captor.getValue();
        Assertions.assertEquals(student, capturedStudent);

    }

    //ДЗ
    @Test
    void deleteStudentExistsInDatabase() {
        Long id = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        //act
        when(studentRepository.existsById(id)).thenReturn(true);
        studentService.deleteStudent(id);

        //assert
        verify(studentRepository).deleteById(captor.capture());
        Long capturedId = captor.getValue();
        assertEquals(id, capturedId);
    }

    @Test
    void deleteStudentAbsentFromDatabase() {
        Long id = 1L;

        //act
        // when(studentRepository.existsById(id)).thenReturn(false);

        //assert
        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudent(id);
        });

        assertEquals("Student with id 1 does not exists", ex.getMessage());
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void SomeTest() {
        String email = "someDifferentEmail@gmail.com";
        when(studentRepository.selectExistsEmail(email)).thenReturn(true);
        Student student = new Student("Test", email, Gender.MALE);

        assertThrows(BadRequestException.class, () -> studentService.addStudent(student));

        verify(studentRepository, never()).save(any(Student.class));
    }
}
