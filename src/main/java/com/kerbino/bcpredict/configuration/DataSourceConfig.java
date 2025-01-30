package com.kerbino.bcpredict.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name = "coinDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.money")
    public DataSource DBCoinConfig() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource UserConfig() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource dynamicDataSource(
            @Qualifier("coinDataSource") DataSource coinDataSource,
            @Qualifier("userDataSource") DataSource userDataSource
    ) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("coin", coinDataSource);
        targetDataSources.put("user", userDataSource);

        DynamicDataSource routingDataSource = new DynamicDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(coinDataSource);
        return routingDataSource;
    }
}
