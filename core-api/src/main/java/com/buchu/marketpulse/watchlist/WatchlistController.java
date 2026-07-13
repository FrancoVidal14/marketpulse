package com.buchu.marketpulse.watchlist;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService service;

    public WatchlistController(WatchlistService service){
        this.service = service;
    }

    @GetMapping
    public List<WatchlistDto> getAll(@AuthenticationPrincipal UserDetails principal){
        return service.findAll(principal.getUsername());
    }

    @PostMapping
    public WatchlistDto create(@AuthenticationPrincipal UserDetails principal,
                            @RequestBody CreateWatchlistRequest request){
        return service.create(principal.getUsername(), request);
    }

    @PostMapping("/{id}/items")
    public WatchlistDto addItem(@AuthenticationPrincipal UserDetails principal,
                                @PathVariable Long id,
                                @RequestBody AddItemRequest request){
        return service.addItem(principal.getUsername(), id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal UserDetails principal,
                       @PathVariable Long id){
        service.delete(principal.getUsername(), id);
    }

}
