package com.kerbino.bcpredict.services.report;

import com.kerbino.bcpredict.entity.coin.CoinEntity;
import com.kerbino.bcpredict.repository.coin.DBCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class CSVReportService {

    private final DBCoinRepository dbCoinRepository;

    public byte[] generateCSVReport() {
        List<CoinEntity> coins = dbCoinRepository.findAll();

        StringBuilder csvBuilder = new StringBuilder();
        // Cabeçalho do CSV
        csvBuilder.append("ID,CoinName,VsCurrencies,MarketCap,H24hVol,H24hChange,LastUpdatedAt\n");

        for (CoinEntity coin : coins) {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add(String.valueOf(coin.getUnique_id()));
            joiner.add(coin.getCoin_id_name());
            joiner.add(coin.getVs_currencies());
            joiner.add(String.valueOf(coin.getMarket_cap()));
            joiner.add(String.valueOf(coin.getH24h_vol()));
            joiner.add(String.valueOf(coin.getH24h_change()));
            joiner.add(String.valueOf(coin.getLast_updated_at()));
            csvBuilder.append(joiner.toString()).append("\n");
        }

        return csvBuilder.toString().getBytes();
    }
} 