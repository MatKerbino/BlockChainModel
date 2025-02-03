package com.kerbino.bcpredict.configuration;

import com.kerbino.bcpredict.configuration.context.DatabaseContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey(){
        return DatabaseContextHolder.getCurrentDatabase();
    }
}
