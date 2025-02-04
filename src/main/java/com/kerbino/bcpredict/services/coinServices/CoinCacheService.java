package com.kerbino.bcpredict.services.coinServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.coinServices.CoinStatsService;
import com.kerbino.bcpredict.services.dataManipulation.JsonManipulation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CoinCacheService {
    private List<JsonNode> coinList;
    private final Map<String, List<JsonNode>> coinPrice = new HashMap<>();
    private static long lastUpdated;
    private final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24);

    private final CoinStatsService coinStatsService;

    public List<JsonNode> getCoinList() throws IOException {
        if (coinList == null || (System.currentTimeMillis() - lastUpdated) > CACHE_DURATION) {
            coinList = coinStatsService.getCoinList();
            lastUpdated = System.currentTimeMillis();
        }
        return coinList;
    }

    public List<JsonNode> getPriceList(String id, String typeCurrency, Integer timesSpan, Integer dailyOr) throws IOException {
        Instant now = Instant.now();
        Instant pastDays = now.minus(timesSpan, ChronoUnit.DAYS);
        List<JsonNode> result = JsonManipulation.filterCoinJsonByTimestamp(
                coinPrice.getOrDefault(id, new ArrayList<>()),
                pastDays.toEpochMilli(),
                now.toEpochMilli()
        );

        if (coinPrice.get(id) == null || coinPrice.get(id).isEmpty() || result.isEmpty()) {
            coinPrice.put(id, coinStatsService.getPriceInTime(id, typeCurrency, timesSpan, dailyOr));
        } else {
            coinPrice.put(id, result);
        }
        return coinPrice.get(id);
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
