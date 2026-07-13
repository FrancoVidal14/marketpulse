package com.buchu.marketpulse.watchlist;

import java.util.List;

public record WatchlistDto(Long id, String name, List<String> symbols) {
    public static WatchlistDto from(Watchlist w){
        List<String> symbols = w.getItems().stream()
                .map(item -> item.getInstrument().getSymbol())
                .toList();
        return new WatchlistDto(w.getId(), w.getName(), symbols);
    }
}
