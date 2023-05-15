package com.roojai.salesforce.ApexConnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.force.api.ApiConfig;
import com.force.api.ApiException;
import com.force.api.ForceApi;
import com.force.api.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SalesforceService {

    private final ForceApi forceApi;

    @Autowired
    private ObjectMapper mapper;

    public SalesforceService(
            @Value("${salesforce.clientId}") String clientId,
            @Value("${salesforce.clientSecret}") String clientSecret,
            @Value("${salesforce.username}") String username,
            @Value("${salesforce.password}") String password,
            @Value("${salesforce.url}") String url) {
        ApiConfig config = new ApiConfig()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setUsername(username)
                .setPassword(password)
                .setForceURL(url);
        this.forceApi = new ForceApi(config);
    }

    public void performSalesforceOperation() {
        // Use the forceApi instance to interact with Salesforce REST API
        // Example: Query data, create records, update records, etc.
        try {
            QueryResult response = forceApi.query("SELECT Id, Name FROM Account LIMIT 10");
            String nextUrl = null;
            do {
                nextUrl = printRecords(response);
            } while (nextUrl != null);
            System.out.println(response);
        } catch (ApiException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    private String printRecords(QueryResult<Map<String, Object>> result) {
        result.getRecords().forEach(map -> {
            try {
                log.info(mapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return result.getNextRecordsUrl();
    }
}