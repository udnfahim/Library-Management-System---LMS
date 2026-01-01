package com.libraryhub.dto;

import com.libraryhub.model.Publications;

public record PublicationsDto(Integer id ,String name , String address, String description) {

    public Publications toEntity(){
        Publications publications = new Publications();
        publications.setName(this.name());
        publications.setAddress(this.address());
        publications.setDescription(this.description());
        return publications;
    }
}
