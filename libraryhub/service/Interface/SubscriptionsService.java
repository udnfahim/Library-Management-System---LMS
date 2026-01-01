package com.libraryhub.service.Interface;

import com.libraryhub.model.Subscriptions;

import java.util.List;

public interface SubscriptionsService {

    Subscriptions addSubscriptions(Subscriptions subscriptions);
    Subscriptions findByIdSubscriptions(int id);
    List<Subscriptions> findAllSubscriptions();
    Subscriptions updateSubscriptions(Subscriptions subscriptions);
    boolean deleteSubscriptions(int id);
}
