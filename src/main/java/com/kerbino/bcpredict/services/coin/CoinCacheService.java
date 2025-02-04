package com.kerbino.bcpredict.services.coin;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CoinCacheService {

    private static final Logger logger = LoggerFactory.getLogger(CoinCacheService.class);
    private List<JsonNode> coinList;
    private long coinListTimestamp;
    private final long CACHE_DURATION_MS = TimeUnit.HOURS.toMillis(24);
    private final Map<String, CacheEntry<List<JsonNode>>> coinPriceCache = new HashMap<>();

    private final CoinStatsService coinStatsService;

    /**
     * Retorna a lista de moedas a partir do cache ou, se o cache estiver expirado, consulta a API.
     *
     * @return Lista de dados das moedas.
     * @throws IOException Se ocorrer erro na consulta à API.
     */
    public List<JsonNode> getCoinList() throws IOException {
        long now = System.currentTimeMillis();
        if (coinList == null || (now - coinListTimestamp) > CACHE_DURATION_MS) {
            logger.info("Fetching fresh coin list from API.");
            coinList = coinStatsService.getCoinList();
            coinListTimestamp = now;
        } else {
            logger.info("Returning cached coin list.");
        }
        return coinList;
    }

    /**
     * Retorna dados de preços para uma moeda específica a partir do cache ou consulta a API se necessário.
     *
     * @param id             Identificador da moeda.
     * @param typeCurrency   Tipo de moeda.
     * @param timeSpanInDays Intervalo de tempo (em dias) para os dados históricos.
     * @param dailyOr        Indicador para dados diários.
     * @return Lista de dados de preços.
     * @throws IOException Se ocorrer erro na consulta à API.
     */
    public List<JsonNode> getPriceList(String id, String typeCurrency, int timeSpanInDays, int dailyOr) throws IOException {
        long now = System.currentTimeMillis();
        CacheEntry<List<JsonNode>> entry = coinPriceCache.get(id);

        if (entry == null || (now - entry.timestamp) > CACHE_DURATION_MS) {
            logger.info("Cache miss for coin id {}. Fetching price data from API.", id);
            List<JsonNode> priceData = coinStatsService.getPriceInTime(id, typeCurrency, timeSpanInDays, dailyOr);
            coinPriceCache.put(id, new CacheEntry<>(priceData, now));
            return priceData;
        } else {
            logger.info("Returning cached price data for coin id {}.", id);
            return entry.data;
        }
    }

    /**
     * Classe interna para armazenar os dados de cache e o timestamp associado.
     */
    private static class CacheEntry<T> {
        private final T data;
        private final long timestamp;

        CacheEntry(T data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }
}
