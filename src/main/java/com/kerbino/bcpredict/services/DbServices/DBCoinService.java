package com.kerbino.bcpredict.services.DbServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.entity.CoinEntities.CoinEntity;
import com.kerbino.bcpredict.repository.CoinRepositories.DBCoinRepository;
import com.kerbino.bcpredict.services.CoinServices.CoinCacheService;
import com.kerbino.bcpredict.services.CoinServices.CoinStatsService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBCoinService {
    @Autowired
    private CoinCacheService coinCacheService;

    @Autowired
    private CoinStatsService coinStatsService;

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistance-unit");

    @Autowired
    private DBCoinRepository dbCoinRepository;

    public List<CoinEntity> getAllCoins() {
        return dbCoinRepository.findAll();
    }

    @Transactional
    public boolean addALlCoinList() throws IOException {
        try {
            List<JsonNode> coinList = coinCacheService.getCoinList(coinStatsService);
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
