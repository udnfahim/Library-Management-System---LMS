package com.libraryhub.service.Interface;

import com.libraryhub.model.Vendor;

import java.util.List;

public interface VendorService {
    public Vendor saveVendor(Vendor vendor);
    public Vendor updateVendor(Vendor vendor);
    public List<Vendor> findAllVendor();
    public boolean deleteVendor(int id);
    public Vendor findById(int id);
    public List<Vendor> findByKeyword(String name);
}
