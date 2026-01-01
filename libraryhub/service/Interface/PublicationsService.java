package com.libraryhub.service.Interface;

import com.libraryhub.model.Publications;

import java.util.List;

public interface PublicationsService {
    Publications addPublications(Publications publication);
    Publications updatePublications(Publications publication);
    boolean deletePublications(int id);
    Publications findByIdPublications(int id);
    List<Publications> findAllPublications();
    List<Publications> searchPublications(String keyword);
}
