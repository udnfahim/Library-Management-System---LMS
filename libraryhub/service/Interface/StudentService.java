package com.libraryhub.service.Interface;

import com.libraryhub.model.Student;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);
    List<Student> findAllStudent();
    Student findByIdStudent(Integer id);
    boolean deleteByIdStudent(Integer id);
    List<Student> searchStudent(String keyword);
    public List<Student> searchByKeyword(String keyword);
}
