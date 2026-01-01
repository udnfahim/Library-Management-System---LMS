package com.libraryhub.dto;

import com.libraryhub.model.Vendor;

import java.time.LocalDate;

public record VendorDto(Integer id , String name , String company , LocalDate date, String number) {
    public Vendor toEntity(){
        Vendor vendor = new Vendor();
        vendor.setName(this.name());
        vendor.setCompany(this.company());
        vendor.setDate(this.date());
        vendor.setNumber(this.number());
        return vendor;
    }
}
