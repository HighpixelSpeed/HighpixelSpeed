package com.highpixelspeed.highpixelspeed.config;

import com.google.gson.*;
import com.highpixelspeed.highpixelspeed.HighpixelSpeed;
import com.highpixelspeed.highpixelspeed.feature.Speedrun;
import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
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
    public static final String CATEGORY_SPEEDRUN = "speedrun";
    public static final String CATEGORY_STATS = "stats";

    public static void init(File file) {
        config = new Configuration(file);
        config.addCustomCategoryComment(CATEGORY_GENERAL, I18n.format("gui.config.general.tooltip"));
        config.addCustomCategoryComment(CATEGORY_AUTODODGE, I18n.format("gui.config.autododge.tooltip"));
        config.addCustomCategoryComment(CATEGORY_BLACKLIST, I18n.format("gui.config.blacklist.tooltip"));
        config.addCustomCategoryComment(CATEGORY_SPEEDRUN, I18n.format("gui.config.speedrun.tooltip"));
        config.addCustomCategoryComment(CATEGORY_STATS, I18n.format("gui.config.stats.tooltip"));

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
        config.get(CATEGORY_GENERAL, "Queue With Party", false, "Requeue normally even if you are in a party");
        propOrder.add("Queue With Party");
        config.get(CATEGORY_GENERAL, "Tag Wins", false, "Show Hypixel Says win count above players' heads");
        propOrder.add("Tag Wins");
        config.getCategory(CATEGORY_GENERAL).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_AUTODODGE, "Enabled", false, "Queue dodge people with a certain number of wins");
        propOrder.add("Enabled");
        config.get(CATEGORY_AUTODODGE, "Wins Threshold", 1000, "The number of wins the player must have");
        propOrder.add("Wins Threshold");
        config.getCategory(CATEGORY_AUTODODGE).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_BLACKLIST, "Enabled", false, "Enable queue dodging blacklisted players");
        propOrder.add("Enabled");
        config.get(CATEGORY_BLACKLIST, "Blacklisted Players", new String[] {}, "Queue dodge these players");
        propOrder.add("Blacklisted Players");
        config.get(CATEGORY_BLACKLIST, "Blacklisted UUIDs", new String[] {"{\"id\":\"baf9e4c582cd43c4b997f215f77a6dac\",\"name\":\"RIPBiffed\"}"},
        "Actual blacklisted players. \"Blacklisted Players\" is simply a display list to be edited in the config GUI. Each entry is a JSON object with the schema:\n" +
                "{\n" +
                "    \"id\": { \"type\": \"string\", \"format\": \"uuid\" },    (dashless UUID)\n" +
                "    \"name\": { \"type\": \"string\", \"minLength\": 3, \"maxLength\": 16 }\n" +
                "}").setShowInGui(false);
        propOrder.add("Blacklisted UUIDs");
        config.getCategory(CATEGORY_BLACKLIST).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_SPEEDRUN, "Enabled", false, "Enable Speedrun mode");
        propOrder.add("Enabled");
        config.get(CATEGORY_SPEEDRUN, "Level", "Full Game", "Full Game times the whole game, and Individual Rounds times each of the following rounds:\n" +
                "Anvil\n" +
                "Iron Golem\n" +
                "Parkour\n" +
                "Jump Off Platform\n" +
                "Pig Off Platform\n" +
                "Full Inventory", new String[] {"Full Game", "Individual Rounds"});
        propOrder.add("Level");
        config.get(CATEGORY_SPEEDRUN, "Time All Rounds", false, "If the level mode is set to Individual Rounds, time every round, not only the speedrun category rounds");
        propOrder.add("Time All Rounds");
        config.get(CATEGORY_SPEEDRUN, "Requeue Slow Run", false, "Requeue if your run time has surpassed your personal best");
        propOrder.add("Requeue Slow Run");
        config.get(CATEGORY_SPEEDRUN, "Speedrun.com Username", "", "Your username on Speedrun.com");
        propOrder.add("Speedrun.com Username");
        config.get(CATEGORY_SPEEDRUN, "Win", 0, "Your personal best for a whole game").setShowInGui(false);
        propOrder.add("Win");
        config.get(CATEGORY_SPEEDRUN, "Complete%", 0, "Your personal best for a whole game with a party").setShowInGui(false);
        propOrder.add("Complete%");
        config.get(CATEGORY_SPEEDRUN, "Anvil", 0, "Your personal best for the Anvil round").setShowInGui(false);
        propOrder.add("Anvil");
        config.get(CATEGORY_SPEEDRUN, "Iron Golem", 0, "Your personal best for the Iron Golem round").setShowInGui(false);
        propOrder.add("Iron Golem");
        config.get(CATEGORY_SPEEDRUN, "Parkour", 0, "Your personal best for the Parkour round").setShowInGui(false);
        propOrder.add("Parkour");
        config.get(CATEGORY_SPEEDRUN, "Jump off Platform", 0, "Your personal best for the Jump off Platform round").setShowInGui(false);
        propOrder.add("Jump off Platform");
        config.get(CATEGORY_SPEEDRUN, "Pig off Platform", 0, "Your personal best for the Pig off Platform round").setShowInGui(false);
        propOrder.add("Pig off Platform");
        config.get(CATEGORY_SPEEDRUN, "Full Inventory", 0, "Your personal best for the Full Inventory round").setShowInGui(false);
        propOrder.add("Full Inventory");
        config.getCategory(CATEGORY_SPEEDRUN).setPropertyOrder(propOrder);

        propOrder = new ArrayList<>();
        config.get(CATEGORY_STATS, "Enabled", false, "Show summary of stats during play session");
        propOrder.add("Enabled");
        config.get(CATEGORY_STATS, "Games Played", 0, "The number of games played during saved play session").setShowInGui(false);
        propOrder.add("Games Played");
        config.get(CATEGORY_STATS, "Wins", 0, "The number of wins during saved play session").setShowInGui(false);
        propOrder.add("Wins");
        config.get(CATEGORY_STATS, "Points", 0, "The number of points earned during saved play session").setShowInGui(false);
        propOrder.add("Points");
        config.get(CATEGORY_STATS, "Win Round Points", 0, "The number points earned in rounds won by the player during saved play session").setShowInGui(false);
        propOrder.add("Win Round Points");
        config.get(CATEGORY_STATS, "Save Date", 0, "The date and time of the saved session stats").setShowInGui(false);
        propOrder.add("Save Date");
        config.getCategory(CATEGORY_STATS).setPropertyOrder(propOrder);

        // Deprecated settings
        config.getCategory(CATEGORY_GENERAL).remove("Hypixel API Key");
        config.getCategory(CATEGORY_GENERAL).remove("Hypixel API Key Mode");

        // Update cached usernames in case they changed
        if (config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList().length > 0) {
            config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Arrays.stream(config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList())
                    .map(entry -> {
                        JsonObject newEntry = Utils.httpGet("sessionserver.mojang.com/session/minecraft/profile",
                                new JsonParser().parse(entry).getAsJsonObject().getAsJsonPrimitive("id").getAsString());
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
            .map(uuid -> new JsonParser().parse(uuid).getAsJsonObject().getAsJsonPrimitive("name").getAsString()).toArray(String[]::new));
        config.save();
    }

    // Add usernames in the GUI config to the list of UUIDs
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_BLACKLIST).get("Blacklisted Players").hasChanged()) {
            String[] names = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList();

            // Find names not already in the list of UUIDs
            for (int i = 0; i <= names.length / 10; i++) { // Only request up to ten names at a time
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
                // Add the names to the list of UUIDs
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

                // Find UUIDs that were removed from the list of names
                boolean exists = false;
                for (String name : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList()) {
                    if (StringUtils.containsIgnoreCase(uuid, "\"" + name + "\"")) {
                        exists = true;
                        break;
                    }
                }

                // Remove the UUIDs
                if (!exists) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.remove(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList(), uuid));
                }
            }
            reloadBlacklist();
        }
        if (event.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_GENERAL).get("Tag Wins").hasChanged() || config.getCategory(CATEGORY_AUTODODGE).get("Wins Threshold").hasChanged()) {
            for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) Utils.tagWins(player);
        }
        if (event.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_GENERAL).get("Enabled").hasChanged() || config.getCategory(CATEGORY_STATS).get("Enabled").hasChanged()) {
            Utils.redrawSessionStats();
        }
        if (event.modID.equals(HighpixelSpeed.MODID) && config.getCategory(CATEGORY_SPEEDRUN).get("Speedrun.com Username").hasChanged()) {
            Speedrun.resetPersonalBests();
            Speedrun.updatePersonalBests();
        }
    }
}
