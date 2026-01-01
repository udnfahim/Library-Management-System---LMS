package com.libraryhub.service;

import com.libraryhub.model.Subscriptions;
import com.libraryhub.repository.SubscriptionsRepository;
import com.libraryhub.service.Interface.SubscriptionsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionsRepository subscriptionsRepository;

    public SubscriptionsServiceImpl(SubscriptionsRepository subscriptionsRepository) {
        this.subscriptionsRepository = subscriptionsRepository;
    }

    @Override
    public Subscriptions addSubscriptions(Subscriptions subscriptions) {
        return subscriptionsRepository.save(subscriptions);
    }
    @Override
    public Subscriptions findByIdSubscriptions(int id) {
        Optional<Subscriptions> optional = subscriptionsRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Subscriptions> findAllSubscriptions() {
        return subscriptionsRepository.findAll();
    }

    @Override
    public Subscriptions updateSubscriptions(Subscriptions subscriptions) {
        return subscriptionsRepository.save(subscriptions);
    }

    @Override
    public boolean deleteSubscriptions(int id) {
        if (subscriptionsRepository.existsById(id)) {
            subscriptionsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
