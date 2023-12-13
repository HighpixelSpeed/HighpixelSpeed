package com.highpixelspeed.highpixelspeed.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.data.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class Utils {

    static CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultCookieStore(new BasicCookieStore()).setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    public static ArrayList<HttpGet> httpGets = new ArrayList<>();
    static int myWins;

    public static void sendChat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b" + message));
    }

    /**
     * Sends a request to an http API
     *
     * @param domain The API host, such as {@code "api.hypixel.net"}
     * @param get The GET call to make, such as {@code "player"}, {@code "recentgames"}, {@code "leaderboards"}, etc
     *
     * @return The response as json/application
     */
    public static JsonObject httpGet(String domain, String get) {
        return httpGet(domain, "", get, "", "");
    }

    /**
     * Sends a GET request to an http API, with one parameter
     *
     * @param domain The API host, such as {@code "api.hypixel.net"}
     * @param get The GET call to make, such as {@code "player"}, {@code "recentgames"}, etc
     * @param apiKey Authentication key
     * @param key The query parameter for the GET, such as {@code "uuid"}, {@code "player"}, or {@code "name"}
     * @param value The value of the key
     *
     * @return The response as json/application
     */
    public static JsonObject httpGet(String domain, String apiKey, String get, String key, String value) {
        String response;
        String url = String.format("https://%s/%s?key=%s", domain, get, apiKey);
        if (!key.isEmpty()){
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
     * Sends a GET request to an http API. This is not thread-blocking
     *
     * @param domain The API host, such as {@code "api.hypixel.net"}
     * @param get The GET call to make, such as {@code "resources/games"}, {@code "boosters"}, etc
     * @param consumer A function that is run after the request succeeds. Input a lambda function with one parameter, which is the response as json/application
     */
    public static void asyncHttpGet(String domain, String get, Consumer<JsonObject> consumer) {
        asyncHttpGet(domain, get, new HashMap<>(), consumer);
    }

    /**
     * Sends a GET request to an http API, with one parameter. This is not thread-blocking
     *
     * @param domain The API host, such as {@code "api.hypixel.net"}
     * @param get The GET call to make, such as {@code "player"}, {@code "recentgames"}, etc
     * @param params A {@link java.util.HashMap} of query parameters, such as {@code "uuid"}, {@code "player"}, or {@code "name"}, and their values
     * @param consumer A function that is run after the request succeeds. Input a lambda function with one parameter, which is the response as json/application
     */
    public static void asyncHttpGet(String domain, String get, HashMap<String, String> params, Consumer<JsonObject> consumer) {
        StringBuilder url = new StringBuilder(String.format("https://%s/%s", domain, get));
        String separator = "?";
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            url.append(String.format("%s%s=%s", separator, entry.getKey(), entry.getValue()));
            separator = "&";
        }
        if (!httpClient.isRunning()) httpClient.start();
        HttpGet request = new HttpGet(url.toString());
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
            public void failed(Exception ignored) {}

            @Override
            public void cancelled() {}

        });
    }

    /**
     * Sends a POST request to an http API with authentication
     *
     * @param domain The API host, such as {@code "api.hypixel.net"}
     * @param payload The POST json array payload to send
     *
     * @return The response as json/application
     */
    public static JsonElement httpPOST(String domain, JsonArray payload){
        HttpURLConnection connection;
        try {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            connection = (HttpURLConnection) new URL(domain).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
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

    public static boolean isOnePointer(String message) {
        message = StringUtils.stripControlCodes(message);
        if(!(message.startsWith("NEXT TASK")))
            return GameData.isOnePointer;
        message = message.toLowerCase();
        if(message.contains("press")){//press the button
            return true;
        } else if (message.contains("throw eggs")){//throw eggs at other players
            return true;
        } else if (message.contains("throw snowball")){//throw snowball at other players
            return true;
        } else if (message.contains("give rud")){//give rudolph his shiny red nose
            return true;
        } else if (message.contains("get into a minecart")){//get into a minecart
            return true;
        } else if (message.contains("nether portal")){//enter the nether portal
            return true;
        } else if (message.contains("end portal")){//enter the end portal
            return true;
        } else if (message.contains("water safely without")){//jump into the water safely without hitting solid blocks
            return true;
        } else if (message.contains("play a song")){//play a song with the noteblocks
            return true;
        } else if (message.contains("grandma")){//give grandma a flower
            return true;
        } else if (message.contains("on the spot")){//jump into the air on the spot
            return true;
        } else if (message.contains("look at a") && message.contains("block")){//look at a block
            return true;
        } else if (message.contains("extinguish yourself")){//extinguish yourself
            return true;
        } else if (message.contains("bed")){//get in a bed
            return true;
        } else if (message.contains("remove the poison")){//remove the poison effect
            return true;
        } else if (message.contains("eggnog")){//drink the eggnog
            return true;
        } else if (message.contains("completely still")){//stand completely still
            return true;
        } else if (message.contains("santa's hat")){//wear santa's hat
            return true;
        } else if (message.contains("look at the sky")){//look at the sky
            return true;
        } else if (message.contains("throw a diamond")){//throw a diamond at a player
            return true;
        } else if (message.contains("nod by")){//nod by looking up and down
            return true;
        } else if (message.contains("look at a player")){//look at a player's head
            return true;
        } else if (message.contains("wrapped present")){//give a player a wrapped present
            return true;
        } else if (message.contains("on the cobblestone")){//stand on the cobblestone
            return true;
        } else if (message.contains("spin around")){//spin around in a full circle
            return true;
        } else if (message.contains("the horse")){//tame the horse by feeding it
            return true;
        } else if (message.contains("open a present")){//open a present under the tree
            return true;
        } else if (message.contains("give santa")){//give santa milk and cookies
            return true;
        } else if (message.contains("on the ice")){//don't stand on the ice
            return true;
        } else if (message.contains("remove the coal")){//remove the coal from your inventory
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


    /**
     * Returns the lesser of two values. If one value is zero, the other will be returned
     *
     * @param a One long to compare
     * @param b The other long to compare
     *
     * @throws IllegalArgumentException if both values are zero
     *
     * @return The lesser non-zero value
     */
    public static long notZeroMin(long a, long b) {
        if (a == 0 && b == 0) throw new IllegalArgumentException("Both values are zero");
        else if (a == 0) return b;
        else if (b == 0) return a;
        else return Math.min(a, b);
    }

    public static void tagWins(EntityPlayer player) {
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Tag Wins").getBoolean()) {
            try {
                drawTag(player, GameData.hsWins.get(player.getUniqueID().toString()));
            } catch (NullPointerException e) {
                try {
                    asyncHttpGet("www.highpixelspeed.com", "player", new HashMap<String, String>() {{put("uuid", player.getUniqueID().toString());}}, response -> {
                        if (response.get("wins_simon_says") != null) {
                            int wins = response.get("wins_simon_says").getAsInt();
                            if (player.equals(Minecraft.getMinecraft().thePlayer)) myWins = wins;
                            GameData.hsWins.put(player.getUniqueID().toString(), wins);
                            drawTag(player, wins);
                        }
                    });
                } catch (Exception ignored) {}
            }
        } else {
            player.getPrefixes().clear();
        }
    }

    static void drawTag(EntityPlayer player, int wins) {
        player.getPrefixes().clear();
        player.addPrefix(new ChatComponentText(String.format("\u00A7b[%s%s\u00A7b] ",
                wins == 0 ?
                        "\u00A77" : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean() && wins >= ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt() ?
                        "\u00A76" : wins > myWins ?
                        "\u00A7a" :
                        "\u00A7e",
                wins)));
    }

    public static void redrawSessionStats() {
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean() && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Enabled").getBoolean()) {
            Minecraft.getMinecraft().ingameGUI.getTabList().setFooter(new ChatComponentText(String.format("\u00A7b\u00A7nHypixel Says Session Stats\n\u00A7r\u00A7bGames Played: \u00A7e%s \u00A7bWins: \u00A7e%s \u00A7bWLR: \u00A7e%s\n\u00A7bPoints: \u00A7e%s \u00A7bPoints per Win: \u00A7e%s",
                    GameData.sessionGamesPlayed,
                    GameData.sessionWins,
                    GameData.sessionWins > 0 && GameData.sessionGamesPlayed == GameData.sessionWins ? "\u221E" : Math.round((float) GameData.sessionWins / (GameData.sessionGamesPlayed - GameData.sessionWins) * 100f) / 100f,
                    GameData.sessionPoints + GameData.score,
                    Math.round((float) GameData.sessionWinRoundPoints / GameData.sessionWins * 100f) / 100f)));
        } else {
            Minecraft.getMinecraft().ingameGUI.getTabList().setFooter(new ChatComponentText("\u00A7aRanks, Boosters & More! \u00A7c\u00A7lSTORE.HYPIXEL.NET"));
        }
    }

    /**
     * @param instant Number of milliseconds
     * @return Time in the format mm:ss.SSS
     */
    public static String formatTime(long instant) {
        return (new SimpleDateFormat("mm:ss.SSS")).format(new Date(instant));
    }

    /**
     * @param instant Number of seconds since the Unix Epoch
     * @return Date and time in the format yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(long instant) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.ofEpochSecond(instant));
    }
}
