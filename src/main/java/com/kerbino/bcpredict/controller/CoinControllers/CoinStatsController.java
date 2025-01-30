package com.kerbino.bcpredict.controller.CoinControllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.CoinServices.CoinCacheService;
import com.kerbino.bcpredict.services.CoinServices.CoinStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/coin")
@AllArgsConstructor
public class CoinStatsController {

    private final CoinStatsService coinStatsService;
    private final CoinCacheService coinCacheService;

    private void CoinStatsService (){
        coinStatsService.startMonitoring();
    }

    @GetMapping("/status")
    public String getPing() {
        return coinStatsService.apiIsActive ? "Is active" : "Error. Shuting down...";
    }

    @GetMapping("/list")
    public ResponseEntity<List<JsonNode>> getCoinList() throws IOException {
        List<JsonNode> coin = coinCacheService.getCoinList(coinStatsService);
        return ResponseEntity.ok(coin);
    }

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
