package com.libraryhub.repository;

import com.libraryhub.model.Publications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Repository
public interface PublicationsRepository extends JpaRepository<Publications,Integer> {

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Query("SELECT p FROM Publications p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Publications> search(String keyword);
}
