package com.buchu.marketpulse.price;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PricePointRepository extends JpaRepository<PricePoint, Long> {
    Page<PricePoint> findByInstrumentSymbolOrderByTsDesc(String symbol, Pageable pageable);
}
