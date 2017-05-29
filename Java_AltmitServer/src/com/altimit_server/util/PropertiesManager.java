package com.altimit_server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private Properties prop = new Properties();

    public PropertiesManager(Class workingClass){
        InputStream inputStream = null;

        try{
            String fileName = "config.properties";
            inputStream = workingClass.getClassLoader().getResourceAsStream(fileName);

            prop.load(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int getServerPort(){
        return Integer.parseInt(prop.getProperty("serverPort", "1024"));
    }

    public String getHazelcastIp(){
        return prop.getProperty("hazelcastIp", "127.0.0.1");
    }

    public int getHazelcastPort(){
        return Integer.parseInt(prop.getProperty("hazelcastPort", "5701"));
    }

    public boolean useRestService(){
        return "true".equalsIgnoreCase(prop.getProperty("useRestService", "true"));
    }

    public int getHttpPort(){
        return  Integer.parseInt(prop.getProperty("httpPort", "3000"));
    }
}
