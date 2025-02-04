package com.kerbino.bcpredict.services.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.configuration.context.DatabaseContextHolder;
import com.kerbino.bcpredict.entity.coin.CoinEntity;
import com.kerbino.bcpredict.repository.coin.DBCoinRepository;
import com.kerbino.bcpredict.services.coin.CoinCacheService;
import com.kerbino.bcpredict.services.coin.CoinStatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBCoinService {

    private CoinEntity coinEntity;

    private CoinCacheService coinCacheService;
    private CoinStatsService coinStatsService;
    private DBCoinRepository dbCoinRepository;

    public List<CoinEntity> getAllCoins() {
        DatabaseContextHolder.setCurrentDatabase("coin");
        return dbCoinRepository.findAll();
    }

    @Transactional
    public boolean addALlCoinList() throws IOException {
        DatabaseContextHolder.setCurrentDatabase("coin");

        List<JsonNode> coinList = coinCacheService.getCoinList();
        List<CoinEntity> coinsToSave = new ArrayList<>();

        for (JsonNode node : coinList) {
            if (node.hasNonNull("id") && node.hasNonNull("symbol") && node.hasNonNull("name")) {
                CoinEntity coin = new CoinEntity();
                coin.setCoin_id_name(node.get("id").asText());
                coin.setVs_currencies(node.get("symbol").asText());
                coin.setMarket_cap(node.has("market_cap") ? node.get("market_cap").asLong() : 0L);
                coin.setH24h_vol(node.has("h24h_vol") ? node.get("h24h_vol").asLong() : 0L);
                coin.setH24h_change(node.has("h24h_change") ? node.get("h24h_change").asLong() : 0L);
                coin.setLast_updated_at(node.has("last_updated_at") ? node.get("last_updated_at").asLong() : 0L);

                coinsToSave.add(coin);
            }
        }

        dbCoinRepository.saveAll(coinsToSave);
        return true;
    }
}
