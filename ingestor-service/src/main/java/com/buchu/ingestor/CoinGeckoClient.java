package com.buchu.ingestor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CoinGeckoClient {

    private final RestClient restClient;
    private final String ids;

    public CoinGeckoClient(@Value("${ingestor.coingecko.base-url}") String baseUrl,
                           @Value("${ingestor.coingecko.ids}") String ids){
        this.restClient = RestClient.create(baseUrl);
        this.ids = ids;
    }


    public Map<String, Map<String, BigDecimal>> fetchPrices() {
        return restClient.get()
                .uri(uri -> uri.path("/simple/price")
                        .queryParam("ids", ids)
                        .queryParam("vs_currencies", "usd")
                        .queryParam("include_24hr_vol", "true")
                        .queryParam("include_24hr_change", "true")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}

