package com.rmd.wms.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        Object obj = DataSourceHolder.getDataSource();
        DataSourceHolder.clearDataSource();
        return obj;
    }
}
