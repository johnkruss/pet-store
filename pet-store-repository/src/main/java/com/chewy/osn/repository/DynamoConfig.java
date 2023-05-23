package com.chewy.osn.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamoConfig {

    private String serviceUrl;
    private String region = "us-east-1";
    private String tableName = "tableName";

    public DynamoConfig(String serviceUrl,
                        String tableName) {
        this.serviceUrl = serviceUrl;
        this.tableName = tableName;
    }
}

