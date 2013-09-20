package edu.emory.data_federation;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MapReduceCommand;

import edu.emory.data_federation.util.ConfigLoader;
import edu.emory.data_federation.util.MongoConnectorUtil;

public class MapReduceEngine {

    public static void mapReduce(final String databaseName, final String collectionName, 
            final ConfigLoader configLoader) {
        final String map = ConfigLoader.getMapper();
        final String reduce = ConfigLoader.getReducer();
        final String finalizer = ConfigLoader.getFinalizer();

        try {
            final DBCollection collection = MongoConnectorUtil.getCollection(
                    databaseName, collectionName);
            final MapReduceCommand mrc = new MapReduceCommand(collection, map,
                    reduce, collectionName, MapReduceCommand.OutputType.REPLACE, null);
            mrc.setFinalize(finalizer);
            collection.mapReduce(mrc);
        } catch (final UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
}
