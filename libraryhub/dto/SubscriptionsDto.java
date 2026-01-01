package com.libraryhub.dto;

import com.libraryhub.model.Subscriptions;

public record SubscriptionsDto(Integer id , String name , double amount , int days, String description) {
    public Subscriptions toEntity(){
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.setName(this.name);
        subscriptions.setAmount(this.amount);
        subscriptions.setDays(this.days);
        subscriptions.setDescription(this.description);
        return subscriptions;
    }
}
