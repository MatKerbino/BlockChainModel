package com.kerbino.bcpredict.controller.coin;

import com.kerbino.bcpredict.entity.coin.CoinEntity;
import com.kerbino.bcpredict.services.db.DBCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/database")
@RequiredArgsConstructor
public class DBCoinController {

    private final DBCoinService dbCoinService;

    @GetMapping("/coins")
    public ResponseEntity<List<CoinEntity>> getAllCoins() {
        return ResponseEntity.ok(dbCoinService.getAllCoins());
    }

    @PutMapping("/coins")
    public ResponseEntity<Boolean> updateCoinData() throws IOException {
        boolean success = dbCoinService.addAllCoinList();
        return ResponseEntity.ok(success);
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<List<JsonNode>> findCoinByName (@PathVariable String id){
        return ResponseEntity.ok(dbCoinService.findCoinByName(id));
    }
    */

    //@PutMapping("/{id}")

}
