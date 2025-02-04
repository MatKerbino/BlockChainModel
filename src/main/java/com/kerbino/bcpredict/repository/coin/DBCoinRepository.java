package com.kerbino.bcpredict.repository.coin;

import com.kerbino.bcpredict.entity.coin.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBCoinRepository extends JpaRepository<CoinEntity, Long> {
}
