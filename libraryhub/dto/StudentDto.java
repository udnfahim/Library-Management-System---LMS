package com.libraryhub.dto;

import com.libraryhub.model.Student;

import java.time.LocalDate;

public record StudentDto(Integer id, String studentId, String name, String email, String mobile, String studentClass, LocalDate registrationDate) {
    public Student toSave() {
        Student student = new Student();
        student.setStudentId(this.studentId);
        student.setName(this.name);
        student.setEmail(this.email);
        student.setMobile(this.mobile);
        student.setStudentClass(this.studentClass);
        student.setRegistrationDate(this.registrationDate);
        return student;
    }
}
