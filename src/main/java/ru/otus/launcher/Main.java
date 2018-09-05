package ru.otus.launcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.catalina.startup.Tomcat;

public class Main {

    private static final String PORT_PROPERTY_NAME = "port";
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH_PROPERTY_NAME = "contextPath";
    private static final String DEFAULT_CONTEXT_PATH = "/Totalizator";
    private static final String WAR_LOCATION_PROPERTY_NAME = "war";
    private static final String DEFAULT_WAR_LOCATION = "target/lottery.war";

    private static final String CURRENT_LOCATION = ".";

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(CURRENT_LOCATION);
        tomcat.getHost().setAppBase(CURRENT_LOCATION);

        PropertyHelper propertyHelper = new PropertyHelper();
        tomcat.setPort(propertyHelper.port);
        tomcat.addWebapp(propertyHelper.contextPath, propertyHelper.warLocation);

        tomcat.getConnector(); // Trigger the creation of the default connector

        tomcat.start();
        tomcat.getServer().await();
    }

    @Data
    @AllArgsConstructor
    private static class PropertyHelper {

        private int port = DEFAULT_PORT;
        private String contextPath = DEFAULT_CONTEXT_PATH;
        private String warLocation = DEFAULT_WAR_LOCATION;

        public PropertyHelper(){
            String portInt = System.getProperty(PORT_PROPERTY_NAME);
            if (portInt != null && !portInt.isEmpty()) {
                setPort(Integer.decode(portInt));
            }
            String contextPath = System.getProperty(CONTEXT_PATH_PROPERTY_NAME);
            if (contextPath != null && !contextPath.isEmpty()) {
                setContextPath(contextPath);
            }
            String warFilePath = System.getProperty(WAR_LOCATION_PROPERTY_NAME);
            if (warFilePath != null && !warFilePath.isEmpty()) {
                setWarLocation(warFilePath);
            }
        }
    }
}
