package com.libraryhub.service;

import com.libraryhub.model.Publications;
import com.libraryhub.model.PurchaseBook;
import com.libraryhub.repository.PublicationsRepository;
import com.libraryhub.service.Interface.PublicationsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationsServiceImpl implements PublicationsService {

    private final PublicationsRepository publicationsRepository;
    private final PurchaseBookServiceImpl purchaseBookService;

    public PublicationsServiceImpl(PublicationsRepository publicationsRepository, PurchaseBookServiceImpl purchaseBookService){
        this.publicationsRepository=publicationsRepository;
        this.purchaseBookService = purchaseBookService;
    }

    @Override
    public Publications addPublications(Publications publication) {
        return publicationsRepository.save(publication);
    }

    @Override
    public Publications updatePublications(Publications publication) {
        return publicationsRepository.save(publication);
    }

    @Override
    public boolean deletePublications(int id) {
        if (!publicationsRepository.existsById(id)) {
            return false;
        }

        if (purchaseBookService.existsByPublicationId(id)) {
            return false;
        }
        publicationsRepository.deleteById(id);
        return true;
    }

    @Override
    public Publications findByIdPublications(int id) {
        Optional<Publications> pub = publicationsRepository.findById(id);
        return pub.orElse(null);
    }

    @Override
    public List<Publications> findAllPublications() {
        return publicationsRepository.findAll();
    }

    @Override
    public List<Publications> searchPublications(String keyword) {
        return publicationsRepository.search(keyword);
    }
}
