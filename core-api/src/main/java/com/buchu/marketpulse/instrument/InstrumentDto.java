package com.buchu.marketpulse.instrument;

public record InstrumentDto(
        Long id,
        String symbol,
        String name,
        boolean active
) {
    public static InstrumentDto from (Instrument instrument){
        return new InstrumentDto(
                instrument.getId(),
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getActive()
        );
    }
}
