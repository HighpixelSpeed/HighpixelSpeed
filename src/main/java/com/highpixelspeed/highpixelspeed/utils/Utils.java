package com.highpixelspeed.highpixelspeed.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.highpixelspeed.highpixelspeed.data.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

public class Utils {

    static CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
    public static ArrayList<HttpGet> httpGets = new ArrayList<>();

    public static void sendChat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b" + message));
    }

    /**
     * Sends a request to an http API
     *
     * @param domain The API host, such as "api.hypixel.net"
     * @param get The GET call to make, such as "counts", "resources/games", "leaderboards", etc
     *
     * @return The response as json/application
     */
    public static JsonObject httpGet(String domain, String get) {
        return httpGet(domain, "", get, "", "");
    }

    /**
     * Sends a GET request to an http API, with one parameter
     *
     * @param domain The API host, such as "api.hypixel.net"
     * @param get The GET call to make, such as "player", "recentgames", etc
     * @param apiKey Authentication key
     * @param key The query parameter for the GET, such as "uuid", "player", or "name"
     * @param value The value of the key
     *
     * @return The response as json/application
     */
    public static JsonObject httpGet(String domain, String apiKey, String get, String key, String value) {
        String response;
        String url = String.format("https://%s/%s?key=%s", domain, get, apiKey);
        if (key.length() > 0){
            url = url + String.format("&%s=%s", key, value);
        }
        try {
            response = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JsonParser().parse(response).getAsJsonObject();
    }

    /**
     * Sends a GET request to an http API, with one parameter. This is not thread-blocking
     *
     * @param domain The API host, such as "api.hypixel.net"
     * @param get The GET call to make, such as "player", "recentgames", etc
     * @param apiKey Authentication key
     * @param key The query parameter for the GET, such as "uuid", "player", or "name"
     * @param value The value of the key
     * @param consumer A function that is run after the request succeeds. Input a lambda function with one parameter, which is the response as json/application
     */
    public static void asyncHttpGet(String domain, String apiKey, String get, String key, String value, Consumer<JsonObject> consumer) {
        String url = String.format("https://%s/%s?key=%s", domain, get, apiKey);
        if (key.length() > 0){
            url = url + String.format("&%s=%s", key, value);
        }
        if (!httpClient.isRunning()) httpClient.start();
        HttpGet request = new HttpGet(url);
        httpGets.add(request);
        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    consumer.accept(new JsonParser().parse(new Scanner(result.getEntity().getContent(), "UTF-8").useDelimiter("\\A").next()).getAsJsonObject());
                    request.abort();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void failed(Exception e) {}

            @Override
            public void cancelled() {}

        });
    }

    /**
     * Sends a POST request to an http API
     *
     * @param domain The API host, such as "api.hypixel.net"
     * @param payload The POST json array payload to send
     *
     * @return The response as json/application
     */
    public static JsonElement httpPOST(String domain, JsonArray payload){
        return httpPOST(domain,"", payload);
    }

    /**
     * Sends a POST request to an http API with authentication
     *
     * @param domain The API host, such as "api.hypixel.net"
     * @param payload The POST json array payload to send
     * @param apiKey Authentication key
     *
     * @return The response as json/application
     */
    public static JsonElement httpPOST(String domain, String apiKey, JsonArray payload){
        HttpURLConnection connection;
        try {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            connection = (HttpURLConnection) new URL(domain).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            if (!apiKey.equals("")) {
                connection.setRequestProperty("key", apiKey); //I don't know if this works. Test later (or never)
            }
            connection.getOutputStream().write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return new JsonParser().parse(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidHypixelAPIKey(String apiKey) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL("https://api.hypixel.net/key?key=" + apiKey).openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public static boolean isOnePointer(String message) {
        message = StringUtils.stripControlCodes(message);
        if(!(message.startsWith("NEXT TASK")))
            return GameData.isOnePointer;
        message = message.toLowerCase();
        if(message.contains("press")){//press the button
            return true;
        }
        if(message.contains("throw eggs")){//throw eggs at other players
            return true;
        }
        if(message.contains("throw snowball")){//throw snowball at other players
            return true;
        }
        if(message.contains("give rud")){//give rudolph his shiny red nose
            return true;
        }
        if(message.contains("get into a minecart")){//get into a minecart
            return true;
        }
        if(message.contains("nether portal")){//enter the nether portal
            return true;
        }
        if(message.contains("end portal")){//enter the end portal
            return true;
        }
        if(message.contains("water safely without")){//jump into the water safely without hitting solid blocks
            return true;
        }
        if(message.contains("play a song")){//play a song with the noteblocks
            return true;
        }
        if(message.contains("grandma")){//give grandma a flower
            return true;
        }
        if(message.contains("on the spot")){//jump into the air on the spot
            return true;
        }
        if(message.contains("look at a") && message.contains("block")){//look at a block
            return true;
        }
        if(message.contains("extinguish yourself")){//extinguish yourself
            return true;
        }
        if(message.contains("bed")){//get in a bed
            return true;
        }
        if(message.contains("remove the poison")){//remove the poison effect
            return true;
        }
        if(message.contains("eggnog")){//drink the eggnog
            return true;
        }
        if(message.contains("completely still")){//stand completely still
            return true;
        }
        if(message.contains("santa's hat")){//wear santa's hat
            return true;
        }
        if(message.contains("look at the sky")){//look at the sky
            return true;
        }
        if(message.contains("throw a diamond")){//throw a diamond at a player
            return true;
        }
        if(message.contains("nod by")){//nod by looking up and down
            return true;
        }
        if(message.contains("look at a player")){//look at a player's head
            return true;
        }
        if(message.contains("wrapped present")){//give a player a wrapped present
            return true;
        }
        if(message.contains("on the cobblestone")){//stand on the cobblestone
            return true;
        }
        if(message.contains("spin around")){//spin around in a full circle
            return true;
        }
        if(message.contains("the horse")){//tame the horse by feeding it
            return true;
        }
        if(message.contains("open a present")){//open a present under the tree
            return true;
        }
        if(message.contains("give santa")){//give santa milk and cookies
            return true;
        }
        if(message.contains("on the ice")){//don't stand on the ice
            return true;
        }
        if(message.contains("remove the coal")){//remove the coal from your inventory
            return true;
        }
        return false;
    }

    /**
     * Adds an element to an array.
     *
     * @param oldArray The array to be added to
     * @param element The object to add to the array
     *
     * @return The modified array
     */
    public static <T> T[] append(T[] oldArray, T element) {
        T[] array = Arrays.copyOf(oldArray, oldArray.length + 1);
        array[array.length - 1] = element;
        return array;
    }

    /**
     * Removes a String from an array
     *
     * @param oldArray The array to be added to
     * @param element The object to remove from the array
     *
     * @return The modified array, unless the String is not in the array, in which case the original array
     *
     * @author <a href="https://stackoverflow.com/a/12812638">FThompson</a>
     */
    public static String[] remove(String[] oldArray, String element) {
        if (oldArray.length > 0) {
            int index = -1;
            for (int i = 0; i < oldArray.length; i++) {
                if (oldArray[i].equalsIgnoreCase(element)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                String[] copy = (String[]) Array.newInstance(oldArray.getClass()
                        .getComponentType(), oldArray.length - 1);
                if (copy.length > 0) {
                    System.arraycopy(oldArray, 0, copy, 0, index);
                    System.arraycopy(oldArray, index + 1, copy, index, copy.length - index);
                }
                return copy;
            }
        }
        return oldArray;
    }
}
