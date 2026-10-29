package com.group7.straytostay.controller;

import com.group7.straytostay.util.DataSourceProvider;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opens the connection pool when the application is deployed and closes it on undeploy.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataSourceProvider.getInstance();
        log.info("StrayToStay started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataSourceProvider.shutdown();
        log.info("StrayToStay stopped");
    }
}
