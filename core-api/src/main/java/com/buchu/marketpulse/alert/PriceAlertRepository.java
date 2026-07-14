package com.buchu.marketpulse.alert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUserId(Long userId);
    Optional<PriceAlert> findByIdAndUserId(Long id, Long userId);
}
