package edu.emory.data_federation.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;


public class DataSourceObject {

    private String datasource, title, key, apiKey;
    private boolean called;
    private List<String> params;
    private MultivaluedMap mvm;
    
    public DataSourceObject() {
        datasource = "";
        title = "";
        key = "";
        called = false;
        params = new ArrayList<String>();
        mvm = new MultivaluedMapImpl();
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public String getDataSource() {
        return datasource;
    }

    public void setDataSource(String dataSource) {
        this.datasource = datasource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getParams() {
        return params;
    }
    
    public void replaceParamsList(List<String> params) {
        this.params = params;
    }
    
    public void addParam(String param) {
        params.add(param);
    }
    
    public void addMVPParam(String key, String val) {
        mvm.add(key, val);
    }
    
    public MultivaluedMap getMVM() {
        return mvm;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getApiKey() {
        return apiKey;
    }
}
