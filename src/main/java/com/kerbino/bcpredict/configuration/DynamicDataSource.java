package com.kerbino.bcpredict.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey(){
        return DatabaseContextHolder.getCurrentDatabase();
    }
}
