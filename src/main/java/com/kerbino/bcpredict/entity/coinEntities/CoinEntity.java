package com.kerbino.bcpredict.entity.coinEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CoinsData")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull private Long unique_id;
    @NonNull private String coin_id_name;
    @NonNull private String vs_currencies;
    private long market_cap;
    private long h24h_vol;
    private long h24h_change;
    private long last_updated_at;
}
