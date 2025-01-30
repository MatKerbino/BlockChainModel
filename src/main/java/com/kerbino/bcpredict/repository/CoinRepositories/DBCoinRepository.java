package com.kerbino.bcpredict.repository.CoinRepositories;

import com.kerbino.bcpredict.entity.CoinEntities.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBCoinRepository extends JpaRepository<CoinEntity, Long> {
}
