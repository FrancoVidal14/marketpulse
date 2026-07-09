package com.buchu.marketpulse.common;

public class InstrumentNotFoundException extends RuntimeException {
    public InstrumentNotFoundException(String symbol) {
        super("Instrument not found: " + symbol);
    }
}
