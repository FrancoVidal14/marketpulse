package com.buchu.marketpulse.instrument;
import com.buchu.marketpulse.price.PricePointDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/instruments")
public class InstrumentController {

    private final InstrumentService service;

    public InstrumentController(InstrumentService service){
        this.service = service;
    }

    @GetMapping
    public List<InstrumentDto> getAll(){
        return service.findAll();
    }

    @GetMapping("/{symbol}/history")
    public Page<PricePointDto> getHistory(
            @PathVariable String symbol,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.getHistory(symbol, pageable);
    }
}
