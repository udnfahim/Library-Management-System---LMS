package com.libraryhub.repository;

import com.libraryhub.model.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions,Integer> {
}
