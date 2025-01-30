package com.kerbino.bcpredict.services.CoinServices;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CoinCacheService {
    private List<JsonNode> coinList;
    //private Set<String> coinPrice;
    private static long lastUpdated;
    private final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24); // 24 horas

    public List<JsonNode> getCoinList(CoinStatsService coinStatsService) throws IOException {
        if (coinList == null || (System.currentTimeMillis() - lastUpdated) > CACHE_DURATION) {
            coinList = coinStatsService.getCoinList();
            lastUpdated = System.currentTimeMillis();
        }
        return coinList;
    }

    /*
    Fazer o sistema de cache para o preço das moedas
    public List<JsonNode> getCoinPrice (CoinStatsService coinStatsService, String id, String typeCurrency) throws IOException {

        if (coinPrice.contains(id)){
            return
        }
    }
    */
}
