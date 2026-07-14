package com.buchu.marketpulse.alert;

import com.buchu.marketpulse.auth.User;
import com.buchu.marketpulse.auth.UserRepository;
import com.buchu.marketpulse.common.AlertNotFoundException;
import com.buchu.marketpulse.common.InstrumentNotFoundException;
import com.buchu.marketpulse.instrument.Instrument;
import com.buchu.marketpulse.instrument.InstrumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertService {

    private final PriceAlertRepository alertRepository;
    private final InstrumentRepository instrumentRepository;
    private final UserRepository userRepository;

    public AlertService(PriceAlertRepository alertRepository,
                        InstrumentRepository instrumentRepository,
                        UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.instrumentRepository = instrumentRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + email));
    }

    @Transactional(readOnly = true)
    public List<AlertDto> findAll(String email){
        User user = getUser(email);
        return alertRepository.findByUserId(user.getId())
                .stream().map(AlertDto::from).toList();
    }

    @Transactional
    public AlertDto create(String email, CreateAlertRequest request){
        User user = getUser(email);
        Instrument instrument = instrumentRepository.findBySymbol(request.symbol())
                .orElseThrow(() -> new InstrumentNotFoundException(request.symbol()));

        PriceAlert alert = new PriceAlert();
        alert.setUser(user);
        alert.setInstrument(instrument);
        alert.setCondition(request.condition());
        alert.setTargetPrice(request.targetPrice());
        alert.setStatus(AlertStatus.ACTIVE);
        return AlertDto.from(alertRepository.save(alert));
    }

    @Transactional
    public void delete(String email, Long alertId){
        User user = getUser(email);
        PriceAlert alert = alertRepository.findByIdAndUserId(alertId, user.getId())
                .orElseThrow(() -> new AlertNotFoundException(alertId));
        alertRepository.delete(alert);
    }
}
