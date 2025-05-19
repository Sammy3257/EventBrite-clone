package com.EventBrite.repository;

import com.EventBrite.model.EventDisplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDisplayRepository extends JpaRepository<EventDisplay, Long> {
}
