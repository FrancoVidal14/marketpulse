package com.buchu.marketpulse.instrument;

import com.buchu.marketpulse.common.InstrumentNotFoundException;
import com.buchu.marketpulse.price.PricePointDto;
import com.buchu.marketpulse.price.PricePointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final PricePointRepository pricePointRepository;

    public InstrumentService(InstrumentRepository instrumentRepository,
                             PricePointRepository pricePointRepository){
        this.instrumentRepository = instrumentRepository;
        this.pricePointRepository = pricePointRepository;

    }

    public List<InstrumentDto> findAll(){
        return instrumentRepository.findAll()
                .stream()
                .map(InstrumentDto::from)
                .toList();
    }

    public Page<PricePointDto> getHistory(String symbol, Pageable pageable) {
        if (instrumentRepository.findBySymbol(symbol).isEmpty()) {
            throw new InstrumentNotFoundException(symbol);
        }
        return pricePointRepository
                .findByInstrumentSymbolOrderByTsDesc(symbol, pageable)
                .map(PricePointDto::from);
    }

}

