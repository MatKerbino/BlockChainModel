package com.kerbino.bcpredict.controller.CoinControllers;

import com.kerbino.bcpredict.entity.CoinEntities.CoinEntity;
import com.kerbino.bcpredict.services.DbServices.DBCoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/database")
public class DBCoinController {

    private DBCoinService dbCoinService;

    @GetMapping("/")
    public ResponseEntity<List<CoinEntity>> getAllCoins () {
        return ResponseEntity.ok(dbCoinService.getAllCoins());
    }

    @PutMapping("/")
    public ResponseEntity<Boolean> putAllCoins() throws IOException {
        return ResponseEntity.ok(dbCoinService.addALlCoinList());
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<List<JsonNode>> findCoinByName (@PathVariable String id){
        return ResponseEntity.ok(dbCoinService.findCoinByName(id));
    }
    */

    //@PutMapping("/{id}")

}
