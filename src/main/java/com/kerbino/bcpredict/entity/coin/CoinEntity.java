package com.kerbino.bcpredict.entity.coin;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidade que representa uma moeda.
 */
@Entity
@Table(name = "coin")
@Data
public class CoinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unique_id;

    @Column(name = "coin_id_name")
    private String coin_id_name;

    @Column(name = "vs_currencies")
    private String vs_currencies;

    @Column(name = "market_cap")
    private Long market_cap;

    @Column(name = "h24h_vol")
    private Long h24h_vol;

    @Column(name = "h24h_change")
    private Long h24h_change;

    @Column(name = "last_updated_at")
    private Long last_updated_at;
}
