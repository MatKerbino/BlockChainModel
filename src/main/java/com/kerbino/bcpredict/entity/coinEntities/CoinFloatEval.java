package com.kerbino.bcpredict.entity.coinEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CoinsFloatData")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoinFloatEval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long value;
    private long timestamp;
}
