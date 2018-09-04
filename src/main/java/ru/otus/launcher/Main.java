package ru.otus.launcher;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(".");
        tomcat.setPort(8081);
        String contextPath = "/Totalizator";
        String warFilePath = "target/lottery.war";
        tomcat.getHost().setAppBase(".");
        Context context = tomcat.addWebapp(contextPath, warFilePath);

        tomcat.getConnector(); // Trigger the creation of the default connector

        tomcat.start();
        tomcat.getServer().await();
    }
}
