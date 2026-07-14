package com.buchu.marketpulse.alert;

import java.math.BigDecimal;
import java.time.Instant;

public record AlertDto(
        Long id,
        String symbol,
        AlertCondition condition,
        BigDecimal targetPrice,
        AlertStatus status,
        Instant createdAt
        ) {
    public static AlertDto from(PriceAlert a){
        return new AlertDto(
                a.getId(),
                a.getInstrument().getSymbol(),
                a.getCondition(),
                a.getTargetPrice(),
                a.getStatus(),
                a.getCreatedAt()
        );
    }
}
