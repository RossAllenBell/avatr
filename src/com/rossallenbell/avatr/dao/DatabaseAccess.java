package com.rossallenbell.avatr.dao;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.couchbase.client.CouchbaseClient;

public class DatabaseAccess {
    
    @SuppressWarnings("serial")
    public final static Properties DATABASE_PROPERTIES = new Properties() {
        {
            try {
                load(new FileInputStream("etc/database.properties"));
            } catch (Exception e) {
                System.err.println("Error getting database properties: " + e.getMessage());
                System.exit(0);
            }
        }
    };
    
    private static CouchbaseClient cbClient = null;
    
    @SuppressWarnings("serial")
    public synchronized static CouchbaseClient getDatabaseClient(){
        if(cbClient != null){
            return cbClient;
        }
        
        List<URI> uris = new ArrayList<URI>(){{
            add(URI.create(String.format("http://%s:%s/pools", DATABASE_PROPERTIES.getProperty("url"), DATABASE_PROPERTIES.getProperty("port"))));
        }};
        try {
            cbClient = new CouchbaseClient(uris, DATABASE_PROPERTIES.getProperty("bucket"), DATABASE_PROPERTIES.getProperty("password"));
        } catch (Exception e) {
            System.err.println("Error connecting to Couchbase: " + e.getMessage());
            System.exit(0);
        }
        return cbClient;
    }
    
    public synchronized static void shutdown(){
        cbClient.flush();
        cbClient.shutdown();
    }
    
}
