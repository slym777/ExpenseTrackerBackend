package com.example.expensetracker.repository;

import com.example.expensetracker.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findGroupById(Long id);
    List<Trip> findAll();

    @Modifying
    @Transactional
    @Query(value = "delete from user_trip ut where user_Id=:userId and hub_Id=:tripId",
            nativeQuery = true)
    void deleteUserFromHubById(Long userId, Long tripId);

//    @Modifying
//    @Transactional
//    @Query(value = "update Trip t set h.name=:hubName, h.description=:hubDescription, h.avatarUri=:hubAvatarUri, h.userInitiatorId=:initiatorId, h.currency=:newCurrency where h.id=:hubId")
//    void updateHubById(String hubName, String hubDescription, String hubAvatarUri, Long initiatorId, Currency newCurrency, Long hubId);
//

}