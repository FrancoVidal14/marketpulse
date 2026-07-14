package com.buchu.marketpulse.instrument;

import com.buchu.marketpulse.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InstrumentRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Test
    void seededInstruments_shouldBeLoadedByFlyway() {
        // Los 8 instrumentos vienen de la migración V2 (seed)
        long count = instrumentRepository.count();
        assertThat(count).isEqualTo(8);
    }

    @Test
    void findBySymbol_shouldReturnInstrument_whenExists() {
        Optional<Instrument> btc = instrumentRepository.findBySymbol("BTC");

        assertThat(btc).isPresent();
        assertThat(btc.get().getName()).isEqualTo("Bitcoin");
        assertThat(btc.get().getCoingeckoId()).isEqualTo("bitcoin");
    }

    @Test
    void findBySymbol_shouldReturnEmpty_whenNotExists() {
        Optional<Instrument> result = instrumentRepository.findBySymbol("NOPE");

        assertThat(result).isEmpty();
    }
}