package com.kerbino.bcpredict.controller.coin;
import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.coin.CoinCacheService;
import com.kerbino.bcpredict.services.coin.CoinStatsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CoinController {

    private final CoinStatsService coinStatsService;
    private final CoinCacheService coinCacheService;

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    /**
     * Endpoint para retornar o status da API.
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        String message = coinStatsService.isApiActive() ? "API is active" : "API is not active";
        logger.info("Status requested: {}", message);
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint para retornar a lista de moedas.
     *
     * @return Lista de moedas em formato JSON.
     * @throws IOException Se houver problema ao acessar a API.
     */
    @GetMapping("/list")
    public ResponseEntity<List<JsonNode>> getCoinList() throws IOException {
        List<JsonNode> coinList = coinCacheService.getCoinList();
        return ResponseEntity.ok(coinList);
    }


    /**
     * Endpoint para retornar o preço de uma moeda.
     *
     * @param id           Identificador da moeda.
     * @param typeCurrency Tipo de moeda (vs_currencies).
     * @return Dados de preço em JSON.
     * @throws IOException Se houver problema ao acessar a API.
     */
    @GetMapping("/pricing/{id}/{typeCurrency}")
    public ResponseEntity<List<JsonNode>> getCoingPricing (@PathVariable String id, @PathVariable String typeCurrency) throws IOException {
        //Na api, é separado por vírgula cada requisição. Ao fazer o get, adicione %2C entre cada pedido
        //Também é obrigatório pedir o tipo de moeda usada "&" e "vs_currencies=usd" por exemplo.
        //Ainda é possível pedir mais de um tipo de moeda com vírgula, ou %2C
        //Ainda é possível customizar mais os parâmetros da requisição
        List<JsonNode> coins = Collections.singletonList(coinStatsService.getCoinPrice(id, typeCurrency));
        return ResponseEntity.ok(coins);
    }
}
