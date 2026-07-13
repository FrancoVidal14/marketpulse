package com.buchu.marketpulse.watchlist;

import com.buchu.marketpulse.auth.User;
import com.buchu.marketpulse.auth.UserRepository;
import com.buchu.marketpulse.common.InstrumentNotFoundException;
import com.buchu.marketpulse.common.WatchlistNotFoundException;
import com.buchu.marketpulse.instrument.Instrument;
import com.buchu.marketpulse.instrument.InstrumentRepository;
import io.micrometer.common.util.internal.logging.WarnThenDebugLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final InstrumentRepository instrumentRepository;
    private final UserRepository userRepository;

    public WatchlistService(WatchlistRepository wathclistRepository,
                            InstrumentRepository instrumentRepository,
                            UserRepository userRepository){
        this.watchlistRepository = wathclistRepository;
        this.instrumentRepository = instrumentRepository;
        this.userRepository = userRepository;
    }

    private User getUser (String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + email));
    }

    @Transactional(readOnly = true)
    public List<WatchlistDto> findAll(String email){
        User user = getUser(email);
        return watchlistRepository.findByUserId(user.getId())
                .stream().map(WatchlistDto::from).toList();
    }

    @Transactional
    public WatchlistDto create(String email, CreateWatchlistRequest request){
        User user = getUser(email);
        Watchlist w = new Watchlist();
        w.setUser(user);
        w.setName(request.name());
        return WatchlistDto.from(watchlistRepository.save(w));
    }

    @Transactional
    public WatchlistDto addItem(String email, Long watchlistId, AddItemRequest request){
        User user = getUser(email);
        Watchlist w = watchlistRepository.findByIdAndUserId(watchlistId, user.getId())
                .orElseThrow(() -> new WatchlistNotFoundException(watchlistId));
        Instrument instrument = instrumentRepository.findBySymbol(request.symbol())
                .orElseThrow(() -> new InstrumentNotFoundException(request.symbol()));

        boolean alreadyThere = w.getItems().stream()
                .anyMatch(i -> i.getInstrument().getId().equals(instrument.getId()));
        if (!alreadyThere){
            WatchlistItem item = new WatchlistItem();
            item.setWatchlist(w);
            item.setInstrument(instrument);
            w.getItems().add(item);
        }
        return WatchlistDto.from(watchlistRepository.save(w));
    }

    @Transactional
    public void delete(String email, Long watchlistId){
        User user = getUser(email);
        Watchlist w = watchlistRepository.findByIdAndUserId(watchlistId, user.getId())
                .orElseThrow(()-> new WatchlistNotFoundException(watchlistId));
        watchlistRepository.delete(w);
    }

}
