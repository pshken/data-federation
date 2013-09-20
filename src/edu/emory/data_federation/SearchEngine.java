package edu.emory.data_federation;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.emory.data_federation.util.ConfigLoader;
import edu.emory.data_federation.util.DBConnectionInstance;
import edu.emory.data_federation.util.DataSourceObject;
import edu.emory.data_federation.util.DataSourceRetrievalThread;
import edu.emory.data_federation.util.MongoConnectorUtil;

@Path("/search")
public class SearchEngine {
    ConfigLoader congfigLoader = ConfigLoader.getInstance();
    
    @GET
    @Path("/debug")
    @Produces(MediaType.TEXT_PLAIN)
    public String debug() {
        List<DataSourceObject> datasources = congfigLoader.getListOfDataSource();
        final String map = ConfigLoader.getMapper();
        final String reduce = ConfigLoader.getReducer();
        final String finalizer = ConfigLoader.getFinalizer();
        
        return "Map Function: " + map + "\nReduce Function: " + 
            reduce + "\nFinalizer Function: " + finalizer;
    }
    
    // This method is called if HTML is request
    @GET
    @Path("/query")
    @Produces(MediaType.TEXT_PLAIN)
    public String search(@Context UriInfo param) {
        final String databaseName = "mapReduceCombine";
        final String collectionName = getUUID();
        List<DataSourceObject> datasources = congfigLoader.getListOfDataSource();
    
        // Create DB connection based.
        DB db = DBConnectionInstance.getInstance(databaseName);
    
        // This for loop will fill in all dataSource Object with GET parameters.
        for (DataSourceObject dataSourceObject: datasources) {
            List<String> dataSourceParams = dataSourceObject.getParams();
            for (String objParam: dataSourceParams) {
                try {
                    String paramVal = param.getQueryParameters().getFirst(objParam.toString()).trim();
                    dataSourceObject.addMVPParam(objParam, paramVal);
                    dataSourceObject.setCalled(true);
                } catch(NullPointerException ex) {
                    continue;
                }
            }
        }

        // This will put each retrieval from each data source into thread-based.
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (DataSourceObject dataSourceObject: datasources) {
            if (dataSourceObject.isCalled() == true) {
                Runnable worker =  new DataSourceRetrievalThread(dataSourceObject, collectionName, db);
                executor.execute(worker);
            }
        }
        
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // This will run till all threads are finished.
        while (!executor.isTerminated()) {}
    
        MapReduceEngine.mapReduce(databaseName, collectionName, congfigLoader);
        
        final BasicDBObject query = new BasicDBObject("value.intersection", true);
        DBCollection coll = null;;
        try {
            coll = MongoConnectorUtil.getCollection(databaseName, collectionName);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        DBCursor cursor = coll.find(query);
        final StringBuffer res = new StringBuffer ("[");
        try {
            while(cursor.hasNext()) {
                res.append(cursor.next());
                if (cursor.hasNext()){
                    res.append(",");
                }
            }
         } finally {
            cursor.close();
         }
        res.append("]");
        
        return res.toString();
    }

    public static String getUUID() {
        UUID id = UUID.randomUUID();
        return "emory" + id.toString().replaceAll("-", "");
    }

}