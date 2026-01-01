package com.libraryhub.service;

import com.libraryhub.model.Vendor;
import com.libraryhub.repository.VendorRepository;
import com.libraryhub.service.Interface.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository ;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor saveVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor updateVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> findAllVendor() {
        return vendorRepository.findAll();
    }

    @Override
    public boolean deleteVendor(int id) {
        if(vendorRepository.existsById(id)) {
            vendorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Vendor findById(int id) {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        return vendor.orElse(null);
    }
    @Override
    public List<Vendor> findByKeyword(String keyword) {
        return vendorRepository.findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCase(keyword, keyword);
    }

}
