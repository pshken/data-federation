package edu.emory.data_federation.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public final class ConfigLoader {
    private static volatile ConfigLoader instance = null;
    private static List<DataSourceObject> listOfDataSource = null;
    private static String mapper = "";
    private static String reducer = "";
    private static String finalizer = "";
    private static String pri_key = "";
    private static ArrayList list = new ArrayList();
    
    private static String mapFile = "templates/map.vm";
    private static String reduceFile = "templates/reduce.vm";
    private static String finalizerFile = "templates/finalizer.vm";
    private static String dataSourceFile = "templates/datasource.json";

    
    // private constructor
    private ConfigLoader() {
        loadListOfDataSource();
        loadMapper();
        loadReducer();
        loadFinalizer();
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                instance = new ConfigLoader();
            }
        }
        return instance;
    }
    
    private static void loadMapper() {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            VelocityContext context = new VelocityContext();
            context.put("datasourceList", list);
            context.put("dataKey", pri_key);
            Template t = ve.getTemplate(mapFile);
            StringWriter writer = new StringWriter();
            t.merge( context, writer );
            mapper = writer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   
    private static void loadReducer() {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            VelocityContext context = new VelocityContext();
            context.put("datasourceList", list);
            Template t = ve.getTemplate(reduceFile);
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            reducer = writer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void loadFinalizer() {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            ve.init();
            VelocityContext context = new VelocityContext();
            context.put("datasourceList", list);
            Template t = ve.getTemplate(finalizerFile);
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            finalizer = writer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void loadListOfDataSource() {
        Gson gson = new Gson();
        String rawJson = "";
        InputStream inputStream =  ConfigLoader.class.getClassLoader().getResourceAsStream(dataSourceFile);
        rawJson = convertStreamToString(inputStream);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(rawJson).getAsJsonArray();
        listOfDataSource = new ArrayList<DataSourceObject>();
        for (int i = 0; i < array.size(); i++) {
            DataSourceObject dsObject = gson.fromJson(array.get(i), DataSourceObject.class);
            listOfDataSource.add(dsObject);
            pri_key = dsObject.getKey();
        }
        
        for (int i = 0; i < listOfDataSource.size(); i++) {
            list.add(listOfDataSource.get(i).getTitle());
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    public static List<DataSourceObject> getListOfDataSource() {
        return listOfDataSource;
    }
    
    public static String getMapper() {
        return mapper;
    }
    
    public static String getReducer() {
        return reducer;
    }
    
    public static String getFinalizer() {
        return finalizer;
    }

}
