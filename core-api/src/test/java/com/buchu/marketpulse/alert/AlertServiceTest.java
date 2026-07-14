package com.buchu.marketpulse.alert;

import com.buchu.marketpulse.auth.User;
import com.buchu.marketpulse.auth.UserRepository;
import com.buchu.marketpulse.common.InstrumentNotFoundException;
import com.buchu.marketpulse.instrument.Instrument;
import com.buchu.marketpulse.instrument.InstrumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock private PriceAlertRepository alertRepository;
    @Mock private InstrumentRepository instrumentRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private AlertService alertService;

    @Test
    void create_shouldSaveAlert_whenInstrumentExists() {
        // given
        String email = "test@test.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Instrument btc = new Instrument();
        btc.setId(10L);
        btc.setSymbol("BTC");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(instrumentRepository.findBySymbol("BTC")).thenReturn(Optional.of(btc));
        when(alertRepository.save(any(PriceAlert.class))).thenAnswer(inv -> inv.getArgument(0));

        var request = new CreateAlertRequest("BTC", AlertCondition.ABOVE, new BigDecimal("100000"));

        // when
        AlertDto result = alertService.create(email, request);

        // then
        assertThat(result.symbol()).isEqualTo("BTC");
        assertThat(result.condition()).isEqualTo(AlertCondition.ABOVE);
        assertThat(result.status()).isEqualTo(AlertStatus.ACTIVE);
    }

    @Test
    void create_shouldThrow_whenInstrumentDoesNotExist() {
        // given
        String email = "test@test.com";
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(instrumentRepository.findBySymbol("NOPE")).thenReturn(Optional.empty());

        var request = new CreateAlertRequest("NOPE", AlertCondition.ABOVE, new BigDecimal("100000"));

        // when / then
        assertThatThrownBy(() -> alertService.create(email, request))
                .isInstanceOf(InstrumentNotFoundException.class);
    }
}