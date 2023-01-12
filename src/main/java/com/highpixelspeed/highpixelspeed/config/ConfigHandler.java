package com.highpixelspeed.highpixelspeed.config;

import com.google.gson.*;
import com.highpixelspeed.highpixelspeed.HighpixelSpeed;
import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Inspired by NovaViper/ZeroQuest config layout
public class ConfigHandler {

    public static Configuration config;
    public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
    public static final String CATEGORY_AUTODODGE = "autododge";
    public static final String CATEGORY_BLACKLIST = "blacklist";

    public static void init(File file) {
        config = new Configuration(file);
        config.addCustomCategoryComment(CATEGORY_GENERAL, I18n.format("gui.config.general.tooltip"));
        config.addCustomCategoryComment(CATEGORY_AUTODODGE, I18n.format("gui.config.autododge.tooltip"));
        config.addCustomCategoryComment(CATEGORY_BLACKLIST, I18n.format("gui.config.blacklist.tooltip"));

        List<String> propOrder = new ArrayList<>();
        config.get(CATEGORY_GENERAL, "Enabled", false, "Enable the whole mod");
        propOrder.add("Enabled");
        config.get(CATEGORY_GENERAL, "Forty Point Mode", false, "Cancel requeuing if you can get 40 points");
        propOrder.add("Forty Point Mode");
        config.get(CATEGORY_GENERAL, "Forty Point Only", false, "If Forty Point Mode is true, requeue if you cannot get 40 points");
        propOrder.add("Forty Point Only");
        config.get(CATEGORY_GENERAL, "Leave Empty Queue", false, "Requeue if there aren't enough players to start");
        propOrder.add("Leave Empty Queue");
        config.get(CATEGORY_GENERAL, "Queue On Loss", false, "Requeue if you cannot win");
        propOrder.add("Queue On Loss");
        config.get(CATEGORY_GENERAL, "Hypixel API Key Mode", "Manual" , "If set to manual, put your key in the field below. " +
                "If set to automatic, you don't have to do anything, but your key will be reset each time you restart the game. " +
                "If you use your API key for something else, use manual mode", new String[] {"Manual", "Automatic"});
        propOrder.add("Hypixel API Key Mode");
        config.get(CATEGORY_GENERAL, "Hypixel API Key", "", "Your Hypixel API key. Beware the fact that it is saved on your computer");
        propOrder.add("Hypixel API");
        config.getCategory(CATEGORY_GENERAL).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_AUTODODGE, "Enabled", false, "Queue dodge people with a certain number of wins");
        propOrder.add("Enabled");
        config.get(CATEGORY_AUTODODGE, "Wins Threshold", 100, "The number of wins the player must have");
        propOrder.add("Wins Threshold");
        config.getCategory(CATEGORY_AUTODODGE).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_BLACKLIST, "Enabled", false, "Enable queue dodging blacklisted players");
        propOrder.add("Enabled");
        config.get(CATEGORY_BLACKLIST, "Blacklisted Players", new String[] {}, "Queue dodge these players");
        propOrder.add("Blacklisted Players");
        config.get(CATEGORY_BLACKLIST, "Blacklisted UUIDs", new String[] {"{\"id\":\"154d46fe8e2c41918243b0c3b2391d2a\",\"name\":\"Aethalops\"}"},
        "Actual blacklisted players. \"Blacklisted Players\" is simply a display list to be edited in the config GUI. Each entry is a JSON object with the schema:\n" +
                "{\n" +
                "    \"id\": { \"type\": \"string\", \"format\": \"uuid\" },    (dashless UUID)\n" +
                "    \"name\": { \"type\": \"string\", \"minLength\": 3, \"maxLength\": 16 }\n" +
                "}").setShowInGui(false);
        propOrder.add("Blacklisted UUIDs");
        config.getCategory(CATEGORY_BLACKLIST).setPropertyOrder(propOrder);

        if (config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList().length > 0) {

            //Update cached usernames in case they changed
            config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Arrays.stream(config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList())
                    .map(entry -> {
                        JsonObject newEntry = Utils.httpGet("sessionserver.mojang.com/session/minecraft/profile",
                                new JsonParser().parse(entry).getAsJsonObject().getAsJsonPrimitive("id").toString().replace("\"", ""));
                        newEntry.remove("properties");
                        return newEntry.toString();
                    }).toArray(String[]::new));
            reloadBlacklist();
        }
    }

    public static void toggle(String category, String property) {
        config.getCategory(category).get(property)
            .set(!config.getCategory(category).get(property).getBoolean());
    }

    public static void reloadBlacklist() {
        config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted Players").set(Arrays.stream(config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList())
            .map(uuid -> new JsonParser().parse(uuid).getAsJsonObject().getAsJsonPrimitive("name").toString().replace("\"", "")).toArray(String[]::new));
        config.save();
    }

    //Add usernames in the GUI config to the list of UUIDs
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent e) {
        if (e.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_BLACKLIST).get("Hypixel API Key Mode").hasChanged() && config.getCategory(CATEGORY_BLACKLIST).get("Hypixel API Key Mode").getString().equals("Automatic")) {
            Utils.sendChat("/api new");
        }
        if (e.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted Players").hasChanged()) {
            String[] names = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList();

            //Find names not already in the list of UUIDs
            for (int i = 0; i <= names.length / 10; i++) { //Only request up to ten names at a time
                JsonArray payload = new JsonArray();
                for (int j = 0; j < 10 && i * 10 + j < names.length; j++) {
                    boolean exists = false;
                    for (String uuid : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList()) {
                        if (StringUtils.containsIgnoreCase(uuid, "\"" + names[i * 10 + j] + "\"")) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        payload.add(new JsonPrimitive(names[i * 10 + j]));
                    }
                }
                //Add the names to the list of UUIDs
                try {
                    if (payload.size() > 0) {
                        for (JsonElement jsonElement : (JsonArray) Utils.httpPOST("https://api.mojang.com/profiles/minecraft", payload)) {
                            ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.append(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList(), jsonElement.toString()));
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            config.save();
            for (String uuid : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList()) {

                //Find UUIDs that were removed from the list of names
                boolean exists = false;
                for (String name : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList()) {
                    if (StringUtils.containsIgnoreCase(uuid, "\"" + name + "\"")) {
                        exists = true;
                        break;
                    }
                }

                //Remove the UUIDs
                if (!exists) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.remove(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList(), uuid));
                }
            }
            reloadBlacklist();
        }
    }
}
