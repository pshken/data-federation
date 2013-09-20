package edu.emory.data_federation.util;

import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mongodb.DB;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class DataSourceRetrievalThread implements Runnable {

    private DataSourceObject dataSourceObject = null;
    private ClientConfig config = null;
    private Client client = null;
    private DB db = null;
    private String collectionName;
    
    public DataSourceRetrievalThread(DataSourceObject dataSourceObject, String collectionName, DB db) {
        this.db = db;
        this.dataSourceObject = dataSourceObject;
        this.collectionName = collectionName;

        config = new DefaultClientConfig();
        client = Client.create(config);
    }

    @Override
    public void run() {
        try {
            MultivaluedMap mvm = dataSourceObject.getMVM();
            mvm.add("api_key", dataSourceObject.getApiKey());
            WebResource wr = client.resource(dataSourceObject.getDataSource());
            String rawJsonArray =  wr.queryParams(mvm).get(String.class);
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = (JsonArray) parser.parse(rawJsonArray);
            MongoConnectorUtil.insertDocs(db, collectionName, jsonArray, dataSourceObject.getTitle());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
