package com.alertservice.repository;

import com.alertservice.entity.alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface alertRepository extends JpaRepository<alert,Integer> {
    static List<alert> findTop10ByOrderByCreatedAtDesc() {
        return null;
    }
}
