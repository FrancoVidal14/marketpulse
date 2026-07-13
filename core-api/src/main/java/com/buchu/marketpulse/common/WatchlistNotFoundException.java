package com.buchu.marketpulse.common;

public class WatchlistNotFoundException extends RuntimeException {
    public WatchlistNotFoundException(Long id) {
        super("Watchlist not found: " + id);
    }
}
