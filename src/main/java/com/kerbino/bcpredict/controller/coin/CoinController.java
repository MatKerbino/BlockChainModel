package com.kerbino.bcpredict.controller.coin;
import com.fasterxml.jackson.databind.JsonNode;
import com.kerbino.bcpredict.services.coin.CoinCacheService;
import com.kerbino.bcpredict.services.coin.CoinStatsService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(coinStatsService.isApiActive() ? "API is active" : "API is not active");
    }

    @GetMapping("/list")
    public ResponseEntity<List<JsonNode>> getCoinList() throws IOException {
        List<JsonNode> coinList = coinCacheService.getCoinList();
        return ResponseEntity.ok(coinList);
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
