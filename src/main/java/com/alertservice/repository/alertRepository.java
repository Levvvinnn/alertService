package com.alertservice.repository;

import com.alertservice.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface alertRepository extends JpaRepository<Alert,Integer> {
    List<Alert> findTop10ByOrderByCreatedAtDesc();
}
