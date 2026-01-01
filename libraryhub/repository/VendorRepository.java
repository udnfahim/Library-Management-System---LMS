package com.libraryhub.repository;

import com.libraryhub.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface VendorRepository extends JpaRepository<Vendor,Integer> {

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    List<Vendor> findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCase(String name, String company);
}
