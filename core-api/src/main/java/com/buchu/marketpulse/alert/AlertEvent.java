package com.buchu.marketpulse.alert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "alert_event")
@Getter
@Setter
@NoArgsConstructor
public class AlertEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    private PriceAlert alert;

    @Column(name = "triggered_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal triggeredPrice;

    @Column(name = "triggered_at", nullable = false)
    private Instant triggeredAt = Instant.now();
}
