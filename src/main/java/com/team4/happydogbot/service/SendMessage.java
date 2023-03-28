package com.team4.happydogbot.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class SendMessage {

    private static final ResourceBundle resource = ResourceBundle.getBundle("application");

    public static void sendToTelegram(Long chatId, String textToSend) {

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        String apiToken = resource.getString("botToken");

        urlString = String.format(urlString, apiToken, chatId, textToSend);

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
