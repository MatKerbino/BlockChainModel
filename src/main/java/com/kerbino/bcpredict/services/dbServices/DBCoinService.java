package com.kerbino.bcpredict.services.dbServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.configuration.context.DatabaseContextHolder;
import com.kerbino.bcpredict.entity.coinEntities.CoinEntity;
import com.kerbino.bcpredict.repository.coinRepositories.DBCoinRepository;
import com.kerbino.bcpredict.services.coinServices.CoinCacheService;
import com.kerbino.bcpredict.services.coinServices.CoinStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBCoinService {

    private CoinEntity coinEntity;

    @Autowired
    private CoinCacheService coinCacheService;

    @Autowired
    private CoinStatsService coinStatsService;

    @Autowired
    private DBCoinRepository dbCoinRepository;

    public List<CoinEntity> getAllCoins() {
        DatabaseContextHolder.setCurrentDatabase("coin");
        return dbCoinRepository.findAll();
    }

    @Transactional
    public boolean addALlCoinList() throws IOException {
        DatabaseContextHolder.setCurrentDatabase("coin");

        try {
            List<JsonNode> coinList = coinCacheService.getCoinList();
            List<CoinEntity> coinsToSave = new ArrayList<>();

            for (JsonNode node : coinList) {
                CoinEntity coin = new CoinEntity();

                String id = node.get("id").asText();
                String vsCurrencies = node.get("vs_currencies").asText();
                long marketCap = node.get("market_cap").asLong();
                long h24hVol = node.get("h24h_vol").asLong();
                long h24hChange = node.get("h24h_change").asLong();
                long lastUpdatedAt = node.get("last_updated_at").asLong();

                coin.setId(id);
                coin.setVs_currencies(vsCurrencies);
                coin.setMarket_cap(marketCap > 0 ? marketCap : 0);
                coin.setH24h_vol(h24hVol > 0 ? h24hVol : 0);
                coin.setH24h_change(h24hChange > 0 ? h24hChange : 0);
                coin.setLast_updated_at(lastUpdatedAt > 0 ? lastUpdatedAt : 0);

                coinsToSave.add(coin);
            }

            dbCoinRepository.saveAll(coinsToSave);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add coins", e);
        }
    }
}
