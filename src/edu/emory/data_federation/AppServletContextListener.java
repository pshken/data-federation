package edu.emory.data_federation;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener{
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Data Federation services destroyed");
    }
 
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Data Federation services started");
    }
}