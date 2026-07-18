package com.buchu.marketpulse.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PriceConsumer {

    private static final Logger log = LoggerFactory.getLogger(PriceConsumer.class);

    private final PriceService priceService;

    public PriceConsumer(PriceService priceService) {
        this.priceService = priceService;
    }

    @KafkaListener(topics = "price-updates", groupId = "core-api")
    public void onPriceUpdate(PriceUpdatedEvent event) {
        log.info("Consumed price update: {} = {}", event.coingeckoId(), event.price());
        priceService.processPriceUpdate(event);
    }
}