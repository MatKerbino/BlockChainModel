package com.kerbino.bcpredict.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CoinCacheService {
    private List<JsonNode> coinList;
    private long lastUpdated;
    private final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24); // 1 hora

    public List<JsonNode> getCoinList(CoinStatsService coinStatsService) throws IOException {
        if (coinList == null || (System.currentTimeMillis() - lastUpdated) > CACHE_DURATION) {
            coinList = (List<JsonNode>) coinStatsService.getCoinList();
            lastUpdated = System.currentTimeMillis();
        }
        return coinList;
    }
}
