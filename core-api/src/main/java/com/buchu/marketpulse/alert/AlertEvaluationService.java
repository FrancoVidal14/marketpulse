package com.buchu.marketpulse.alert;

import com.buchu.marketpulse.instrument.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AlertEvaluationService {

    private static final Logger log = LoggerFactory.getLogger(AlertEvaluationService.class);

    private final PriceAlertRepository alertRepository;
    private final AlertEventRepository alertEventRepository;

    public AlertEvaluationService(PriceAlertRepository alertRepository,
                                  AlertEventRepository alertEventRepository){
        this.alertRepository = alertRepository;
        this.alertEventRepository = alertEventRepository;

    }

    @Transactional
    public void evaluate(Instrument instrument, BigDecimal currentPrice){
        List<PriceAlert> activeAlerts = alertRepository
                .findByInstrumentIdAndStatus(instrument.getId(), AlertStatus.ACTIVE);

        for (PriceAlert alert : activeAlerts){
            if (isTriggered(alert, currentPrice)){
                trigger(alert, currentPrice);
            }
        }
    }

    private boolean isTriggered(PriceAlert alert, BigDecimal currentPrice){
        int comparison = currentPrice.compareTo(alert.getTargetPrice());
        return switch (alert.getCondition()){
          case ABOVE -> comparison >= 0;
          case BELOW -> comparison <= 0;
        };
    }

    private void trigger(PriceAlert alert, BigDecimal currentPrice){
        alert.setStatus(AlertStatus.TRIGGERED);
        alertRepository.save(alert);

        AlertEvent event = new AlertEvent();
        event.setAlert(alert);
        event.setTriggeredPrice(currentPrice);
        alertEventRepository.save(event);

        log.info("ALERT TRIGGERED: {} {} {} — current price: {}",
                alert.getInstrument().getSymbol(),
                alert.getCondition(),
                alert.getTargetPrice(),
                currentPrice);
    }
}
