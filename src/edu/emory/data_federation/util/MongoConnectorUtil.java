package edu.emory.data_federation.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoConnectorUtil {

    /**
     * Wrapper for creating connection to MongoDB's database with given database name.
     * 
     * @return Connection to MongoDB's connection.
     * @throws UnknownHostException
     */
    public static DB getDatabaseConnection(String dbName) throws UnknownHostException {
        return getDatabaseConnection(dbName, "localhost", 27017);
    }

    /**
     * Wrapper for creating connection to MongoDB's database with given database name,
     * MongoDB address and port.
     * 
     * @return Connection to MongoDB's connection.
     * @throws UnknownHostException
     */
    public static DB getDatabaseConnection(String dbName, String address, int port) 
            throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("localhost" , 27017 );
        DB db = mongoClient.getDB(dbName);
        return db;
    }

    /**
     * Wrapper for getting all the collections name in a database;
     * 
     * @return List<String> of collections name in a database.
     */
    public static List<String> getListOfCollections(DB dbName) {
        Set<String> set = dbName.getCollectionNames();
        List<String> listOfCollectionNames = new ArrayList<String>();
        for(String name: set) {
            listOfCollectionNames.add(name);
        }
        return listOfCollectionNames;
    }

    /**
     * Wrapper for getting collection.
     * 
     * @return DBCollection to the database.
     */
    public static DBCollection getCollection(DB dbName, String collectionName) {
        DBCollection collection = dbName.getCollection(collectionName);
        return collection;
    }

    /**
     * Wrapper for getting collection.
     * 
     * @return DBCollection to the database.
     * @throws UnknownHostException 
     */
    public static DBCollection getCollection(String dbName, String collectionName)
            throws UnknownHostException {
        DB db = getDatabaseConnection(dbName, "localhost", 27017);
        DBCollection collection = db.getCollection(collectionName);
        return collection;
    }
    
    /**
     * Insert document into collection.
     */
    public static void insertDoc(DB dbName, String collectionName, BasicDBObject dbObject) {
        insertDoc(dbName.getCollection(collectionName), dbObject);
    }

    /**
     * Insert document into collection.
     */
    public static void insertDoc(DBCollection collection, BasicDBObject dbObject) {
        collection.insert(dbObject);
    }
    
    /**
     * Insert list of documents into collection.
     */
    public static void insertDocs(DB dbName, String collectionName, BasicDBObject dbObject, 
    		List<BasicDBObject> listOfDBObject) {
        insertDocs(dbName.getCollection(collectionName), listOfDBObject);
    }
    
    /**
     * Insert list of documents into collection.
     */
    public static void insertDocs(DB dbName, String collectionName, JsonArray jsonArray, String id) {
        for (int i = 0; i < jsonArray.size(); i++) {
            BasicDBObject dbObject = (BasicDBObject) JSON.parse(jsonArray.get(i).toString());
            dbObject.append("_dataType", id);
            dbName.getCollection(collectionName).insert(dbObject);
        }
    }
    
    /**
     * Insert list of documents into collection.
     */
    public static void insertDocs(DBCollection collection, List<BasicDBObject> listOfDBObject) {
        for(BasicDBObject dbObject: listOfDBObject) {
        	collection.insert(dbObject);
        }
    }

    /**
     * Get the number of documents in a collection.
     * 
     * @return Count of documents in a collection.
     */
    public static Long getDoucmentCount(DB dbName, String collectionName) {
        return getDoucmentCount(dbName.getCollection(collectionName));
    }

    /**
     * Get the number of documents in a collection.
     * 
     * @return Count of documents in a collection.
     */
    public static Long getDoucmentCount(DBCollection collection) {
        return collection.getCount();
    }
    
    public static MapReduceOutput runMapReduce(DBCollection collection, String map, String reduce) {
    	MapReduceOutput out = collection.mapReduce(map, reduce, null, MapReduceCommand.OutputType.INLINE, null);
    	return out;
    }
    
    public static String find(DBCollection collection, BasicDBObject query) {
    	DBCursor cursor = collection.find(query);
    	String res = "";
    	try {
    		while(cursor.hasNext()) {
    			res += cursor.next();
    		}
    	} finally {
    		cursor.close();
    	}
    	return res;
    }
}
