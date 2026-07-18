package com.buchu.marketpulse.price;

import com.buchu.marketpulse.alert.AlertEvaluationService;
import com.buchu.marketpulse.instrument.Instrument;
import com.buchu.marketpulse.instrument.InstrumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class PriceService {
    private static final Logger log = LoggerFactory.getLogger(PriceService.class);

    private final PricePointRepository pricePointRepository;
    private final InstrumentRepository instrumentRepository;
    private final StringRedisTemplate redisTemplate;
    private final AlertEvaluationService alertEvaluationService;

    public PriceService(PricePointRepository pricePointRepository,
                        InstrumentRepository instrumentRepository,
                        StringRedisTemplate redisTemplate,
                        AlertEvaluationService alertEvaluationService) {
        this.pricePointRepository = pricePointRepository;
        this.instrumentRepository = instrumentRepository;
        this.redisTemplate = redisTemplate;
        this.alertEvaluationService = alertEvaluationService;
    }

    @Transactional
    public void processPriceUpdate(PriceUpdatedEvent event) {
        Instrument instrument = instrumentRepository
                .findByCoingeckoId(event.coingeckoId())
                .orElse(null);

        if (instrument == null) {
            log.warn("Unknown coingeckoId, skipping: {}", event.coingeckoId());
            return;
        }

        PricePoint point = new PricePoint();
        point.setInstrument(instrument);
        point.setPrice(event.price());
        point.setVolume24h(event.volume24h());
        point.setChange24hPct(event.change24hPct());
        point.setTs(event.ts());
        pricePointRepository.save(point);

        redisTemplate.opsForValue().set(
                "price:" + instrument.getSymbol(),
                event.price().toPlainString(),
                Duration.ofMinutes(5));

        alertEvaluationService.evaluate(instrument, event.price());
    }
}
