package com.kerbino.bcpredict.repository.coinRepositories;

import com.kerbino.bcpredict.entity.coinEntities.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBCoinRepository extends JpaRepository<CoinEntity, Long> {
}
