package com.kerbino.bcpredict.services.coinServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CoinStatsService {

    private static final Logger logger = LoggerFactory.getLogger(CoinStatsService.class);

    @Getter
    private boolean apiActive;
    private Request request;
    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public CoinStatsService(){
        this.apiActive = false;
        this.client = new OkHttpClient();
        this.request = new Request.Builder()
                .url("https://api.coingecko.com/api/v3/ping")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-iHYAPd1UiizWuciy3hEMasNS")
                .build();
    }

    public void changeUrl(String newUrl) {
        this.request = this.request.newBuilder()
                .url(newUrl)
                .build();
    }

    @Scheduled(fixedRate = 3600000) // a cada 1 hora
    public void monitorApi() {
        try {
            getConnectionStatus();
            logger.info("API da coingecko está ativa");
        } catch (IOException e) {
            logger.error("Erro ao acessar a API: {}", e.getMessage());
            // Poderia tratar o erro de forma personalizada, sem encerrar a aplicação de forma abrupta
        }
    }

    public void getConnectionStatus() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/ping");
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                this.apiActive = true;
            } else {
                this.apiActive = false;
                throw new IOException("Resposta inesperada: " + response);
            }
        }
    }

    public List<JsonNode> getCoinList() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/coins/list");
        Response response = client.newCall(this.request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String jsonResponse = response.body().string();
            return Collections.singletonList(mapper.readTree(jsonResponse));
        } else {
            throw new IOException("Erro ao obter a lista de moedas");
        }
    }

    public JsonNode getCoinPrice(String id, String typeCurrency) throws IOException {
        changeUrl(String.format(
                "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s", id, typeCurrency
        ));
        Response response = client.newCall(this.request).execute();
        if (response.isSuccessful() && response.body() != null){
            String jsonResponse = response.body().string();
            return mapper.readTree(jsonResponse);
        } else {
            throw new IOException("Erro ao obter preço");
        }
    }

    public List<JsonNode> getPriceInTime(String id, String typeCurrency, Integer timeSpan, Integer dailyOr) throws IOException {
        changeUrl(String.format(
                "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=%d"
                        + (dailyOr > 0 ? "&interval=daily" : "")
                        + "&precision=full",
                id, typeCurrency, timeSpan
        ));
        Response response = client.newCall(this.request).execute();
        if (response.isSuccessful() && response.body() != null){
            String jsonResponse = response.body().string();
            return Collections.singletonList(mapper.readTree(jsonResponse));
        } else {
            throw new IOException("Erro ao obter preços");
        }
    }
}
