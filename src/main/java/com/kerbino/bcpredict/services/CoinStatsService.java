package com.kerbino.bcpredict.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CoinStatsService {

    @Getter
    public boolean apiIsActive;
    private Request request;
    private OkHttpClient client = new OkHttpClient();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public CoinStatsService(){
        this.apiIsActive = false;
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

    public void getConnectionStatus() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/ping");
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                this.apiIsActive = true;
                System.out.println("Está ativa");
            } else {
                throw new IOException("Unexpected response: " + response);
            }
        }
    }

    public JsonNode getCoinList() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/coins/list");
        Response response = client.newCall(this.request).execute();
        if (response.isSuccessful()) {
            assert response.body() != null;
            String jsonResponse = response.body().string();  // Obtendo o corpo da resposta como string
            ObjectMapper mapper = new ObjectMapper();  // Convertendo para JsonArray
            return mapper.readTree(jsonResponse);
        } else {
            throw new IOException("Erro ao obter a lista de moedas");
        }
    }

    public void startMonitoring () {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Verificando Api");
                getConnectionStatus();
            } catch (IOException e) {
                System.err.println("Erro ao acessar a api " + e.getMessage());
                shutdownApplication();
            }
        }, 0, 60, TimeUnit.MINUTES);
    }

    private void shutdownApplication(){
        System.err.println("Api não funcionando do jeito esperado...");
        scheduler.shutdown();
        System.exit(1);
    }


    public JsonNode getCoinPrice(String id, String typeCurrency) throws IOException {
        changeUrl(
                "https://api.coingecko.com/api/v3/simple/price?ids="
                        + id
                        + "&vs_currencies="
                        + typeCurrency
        );
        //O id deve ser sem espaços
        Response response = client.newCall(this.request).execute();
        if (response.isSuccessful()){
            assert response.body() != null;
            String jsonResponse = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonResponse);
        } else {
            throw new IOException("Erro ao obter preço");
        }
    }
}
