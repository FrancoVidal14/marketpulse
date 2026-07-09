package com.buchu.marketpulse.instrument;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstrumentService {

    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository){
        this.repository = repository;
    }

    public List<InstrumentDto> findAll(){
        return repository.findAll()
                .stream()
                .map(InstrumentDto::from)
                .toList();
    }

}

