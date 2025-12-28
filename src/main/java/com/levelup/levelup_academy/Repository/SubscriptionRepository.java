package com.levelup.levelup_academy.Repository;

import com.levelup.levelup_academy.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription findSubscriptionById(Integer id);
    
    @Query("SELECT s FROM Subscription s JOIN FETCH s.user")
    List<Subscription> findAllWithUser();
}
