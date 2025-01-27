package com.kerbino.bcpredict.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.CoinStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
public class CoinStatsController {

    private final CoinStatsService coinStatsService;


    public CoinStatsController(CoinStatsService coinStatsService) {
        this.coinStatsService = coinStatsService;
        coinStatsService.startMonitoring();
    }

    @GetMapping("/getCoinApiStatus")
    public String getPing() {
        return coinStatsService.isApiIsActive() ? "Is active" : "Error. Shuting down...";
    }

    @GetMapping("/getCoinList")
    public ResponseEntity<List<JsonNode>> getCoinList() throws IOException {
        List<JsonNode> coin = Collections.singletonList(coinStatsService.getCoinList());
        if(coin == null || coin.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(coin);
    }
}
