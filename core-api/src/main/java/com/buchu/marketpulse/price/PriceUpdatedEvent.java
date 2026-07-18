package com.buchu.marketpulse.price;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceUpdatedEvent(
        String coingeckoId,
        BigDecimal price,
        BigDecimal volume24h,
        BigDecimal change24hPct,
        Instant ts
) {}