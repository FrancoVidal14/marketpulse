package com.buchu.marketpulse.price;

import com.buchu.marketpulse.instrument.Instrument;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_point")
@Getter
@Setter
@NoArgsConstructor
public class PricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal price;

    private BigDecimal volume_24h;

    private BigDecimal change_24hPct;

    @Column(nullable = false)
    private Instant ts;
}

