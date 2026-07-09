package com.buchu.marketpulse.price;

import java.math.BigDecimal;
import java.time.Instant;

public record PricePointDto(
        Long id,
        BigDecimal price,
        BigDecimal volume24h,
        BigDecimal change24hPct,
        Instant ts
) {
    public static PricePointDto from(PricePoint p) {
        return new PricePointDto(p.getId(), p.getPrice(), p.getVolume24h(), p.getChange24hPct(), p.getTs());
    }
}