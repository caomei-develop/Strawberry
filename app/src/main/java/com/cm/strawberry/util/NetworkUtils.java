package com.cm.strawberry.util;


import java.net.HttpURLConnection;

public class NetworkUtils {

    public static final String AGENT_TAG = " cm";

    public static void addUrlConnectionAgent(HttpURLConnection connection) {
        if (connection != null) {
            connection.setRequestProperty("User-Agent", System.getProperty("http.agent") + AGENT_TAG);
        }
    }
}
