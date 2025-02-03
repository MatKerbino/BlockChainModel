package com.kerbino.bcpredict.services.CoinServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.DataManipulation.JsonManipulation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CoinCacheService {
    private List<JsonNode> coinList;
    private Map<String, List<JsonNode>> coinPrice;
    private final CoinStatsService coinStatsService;
    private static long lastUpdated;
    private final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24);

    public CoinCacheService() {
        this.coinStatsService = new CoinStatsService();
    }

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
        long unixTimestamp = pastDays.toEpochMilli();
        List<JsonNode> result = JsonManipulation.filterCoinJsonByTimestamp(
                coinPrice.get(id),
                pastDays.toEpochMilli(),
                now.toEpochMilli()
        );

        if (coinPrice.get(id).isEmpty() || result.isEmpty()) {
            coinPrice.put(id, coinStatsService.getPriceInTime(id, typeCurrency, timesSpan, dailyOr));
        }

        coinPrice.put(id, result);
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
