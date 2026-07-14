package com.buchu.marketpulse.alert;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService service;

    public AlertController(AlertService service){
        this.service = service;
    }

    @GetMapping
    public List<AlertDto> getAll(@AuthenticationPrincipal UserDetails principal){
        return service.findAll(principal.getUsername());
    }

    @PostMapping
    public AlertDto create(@AuthenticationPrincipal UserDetails principal,
                           @RequestBody CreateAlertRequest request){
        return service.create(principal.getUsername(), request);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal UserDetails principal,
                       @PathVariable Long id){
        service.delete(principal.getUsername(), id);
    }
}
