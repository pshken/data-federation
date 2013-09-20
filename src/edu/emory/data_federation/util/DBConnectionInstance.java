package edu.emory.data_federation.util;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.mongodb.DB;

public class DBConnectionInstance {
    private static volatile HashMap<String, DB> map =  new HashMap<String, DB>();

    // If there is no such instance in the map then it will create 
    // otherwise it will return the connection based on dbName.
    public static DB getInstance(String dbName) {
        if (map.containsKey(dbName)){
            return map.get(dbName);
        } else {
            synchronized (DBConnectionInstance.class) {
                try {
                    DB db = MongoConnectorUtil.getDatabaseConnection(dbName);
                    map.put(dbName, db);
                    return db;
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public DB getDB(String dbName) {
        if (map.containsKey(dbName)){
            return map.get(dbName);
        } else {
           return null;
        }
    }
}
