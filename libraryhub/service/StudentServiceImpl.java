package com.libraryhub.service;

import com.libraryhub.model.Student;
import com.libraryhub.repository.StudentRepository;
import com.libraryhub.service.Interface.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student findByIdStudent(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteByIdStudent(Integer id) {
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Student> searchStudent(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return findAllStudent();
        }

        String lowerKeyword = keyword.toLowerCase();

        return studentRepository.findAll()
                .stream()
                .filter(s ->
                        s.getStudentId().toLowerCase().contains(lowerKeyword) ||
                                s.getName().toLowerCase().contains(lowerKeyword) ||
                                s.getEmail().toLowerCase().contains(lowerKeyword)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> searchByKeyword(String keyword) {
        return studentRepository.findByStudentIdContainingIgnoreCaseOrNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, keyword);
    }

}
