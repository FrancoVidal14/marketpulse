package com.buchu.ingestor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
public class PriceIngestionService {

    private static final Logger log = LoggerFactory.getLogger(PriceIngestionService.class);
    private static final String TOPIC = "price-updates";

    private final CoinGeckoClient coinGeckoClient;
    private final KafkaTemplate<String, PriceUpdatedEvent> kafkaTemplate;

    public PriceIngestionService(CoinGeckoClient coinGeckoClient,
                                 KafkaTemplate<String, PriceUpdatedEvent> kafkaTemplate) {
        this.coinGeckoClient = coinGeckoClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${ingestor.interval-ms}")
    public void ingestPrices() {
        try {
            Map<String, Map<String, BigDecimal>> prices = coinGeckoClient.fetchPrices();
            Instant now = Instant.now();

            prices.forEach((coingeckoId, data) -> {
                var event = new PriceUpdatedEvent(
                        coingeckoId,
                        data.get("usd"),
                        data.get("usd_24h_vol"),
                        data.get("usd_24h_change"),
                        now
                );
                kafkaTemplate.send(TOPIC, coingeckoId, event);
            });

            log.info("Published {} price updates", prices.size());
        } catch (Exception e) {
            log.error("Failed to ingest prices: {}", e.getMessage());
        }
    }
}