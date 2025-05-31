package com.EventBrite.repository;


import com.EventBrite.model.EventDisplay;
import com.EventBrite.model.TicketOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketOptionRepository extends JpaRepository<TicketOption, Long> {

    List<TicketOption> findByEvent(EventDisplay event);

}

