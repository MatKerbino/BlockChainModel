package com.kerbino.bcpredict.services.coin;

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

    public CoinStatsService() {
        this.apiActive = false;
        this.client = new OkHttpClient();
        this.request = new Request.Builder()
                .url("https://api.coingecko.com/api/v3/ping")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-iHYAPd1UiizWuciy3hEMasNS")
                .build();
    }

    /**
     * Atualiza a URL da requisição.
     *
     * @param newUrl Nova URL para a requisição.
     */
    public void changeUrl(String newUrl) {
        this.request = this.request.newBuilder().url(newUrl).build();
    }

    /**
     * Task agendada para monitorar a API a cada 1 hora.
     */
    @Scheduled(fixedRate = 3600000)
    public void monitorApi() {
        try {
            getConnectionStatus();
            logger.info("CoinGecko API is active.");
        } catch (IOException e) {
            logger.error("Error accessing API: {}", e.getMessage());
        }
    }

    /**
     * Checa o status da conexão com a API.
     *
     * @throws IOException Se a conexão falhar.
     */
    public void getConnectionStatus() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/ping");
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                this.apiActive = true;
            } else {
                this.apiActive = false;
                throw new IOException("Unexpected response: " + response);
            }
        }
    }

    /**
     * Consulta a lista de moedas na API.
     *
     * @return Lista contendo o JSON da lista de moedas.
     * @throws IOException Se a consulta falhar.
     */
    public List<JsonNode> getCoinList() throws IOException {
        changeUrl("https://api.coingecko.com/api/v3/coins/list");
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return Collections.singletonList(mapper.readTree(jsonResponse));
            } else {
                throw new IOException("Error retrieving coin list.");
            }
        }
    }

    /**
     * Consulta o preço de uma moeda na API.
     *
     * @param id           Identificador da moeda.
     * @param typeCurrency Tipo de moeda.
     * @return Dados de preço em JSON.
     * @throws IOException Se a consulta falhar.
     */
    public JsonNode getCoinPrice(String id, String typeCurrency) throws IOException {
        changeUrl(String.format("https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s", id, typeCurrency));
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return mapper.readTree(jsonResponse);
            } else {
                throw new IOException("Error retrieving coin price.");
            }
        }
    }

    /**
     * Consulta os dados históricos de preços de uma moeda na API.
     *
     * @param id           Identificador da moeda.
     * @param typeCurrency Tipo de moeda.
     * @param timeSpan     Intervalo de tempo (em dias).
     * @param dailyOr      Indicador para dados diários.
     * @return Lista contendo o JSON dos dados históricos.
     * @throws IOException Se a consulta falhar.
     */
    public List<JsonNode> getPriceInTime(String id, String typeCurrency, int timeSpan, int dailyOr) throws IOException {
        String url = String.format(
                "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=%d%s&precision=full",
                id, typeCurrency, timeSpan, (dailyOr > 0 ? "&interval=daily" : "") + "&precision=full",
                id, typeCurrency, timeSpan
        );
        changeUrl(url);
        try (Response response = client.newCall(this.request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return Collections.singletonList(mapper.readTree(jsonResponse));
            } else {
                throw new IOException("Error retrieving price data.");
            }
        }
    }
}
