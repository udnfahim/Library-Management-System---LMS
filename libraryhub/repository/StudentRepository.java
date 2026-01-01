package com.libraryhub.repository;

import com.libraryhub.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByStudentId(String studentId);

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    List<Student> findByStudentIdContainingIgnoreCaseOrNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String studentId, String name, String email);

}
