package com.yoursole.HypixelSays;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.Gui.ConfigHandler;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.*;


public class JoinWorld {

    static String ip = "";
    static boolean worldJustLoaded = false;
    static boolean doServerCheck; //check if joined server is Hypixel Says
    static long timeAtLastCheck;
    static long enteredQueueTime;
    static Queue<UUID> newPlayerUUIDs;
    static String scaryPlayerName;
    static int scaryPlayerWins;
    static double joinYLevel; //Santa Says and Hypixel Says are at different Y levels for some reason

    @SubscribeEvent
    public void onWorldLoad(Load event) {
        GameData.inHypixelSays = false;
        worldJustLoaded = true;
        doServerCheck = true;
        timeAtLastCheck = System.currentTimeMillis();
        enteredQueueTime = System.currentTimeMillis();
        newPlayerUUIDs = new LinkedList<>();
        scaryPlayerName = "";
        scaryPlayerWins = 0;
        try {
            ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        } catch (Exception ignored){}
    }

    @SubscribeEvent
    public void onPlayerLogin(EntityJoinWorldEvent event) {
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean() && event.entity instanceof EntityPlayer) {
            try {
                if (doServerCheck && StringUtils.stripControlCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjective("PreScoreboard").getDisplayName()).endsWith(" SAYS")) { //Minigame is HYPIXEL SAYS or SANTA SAYS
                    doServerCheck = false;
                    GameData.inHypixelSays = true;
                    GameData.gameHasStarted = false;
                    joinYLevel = Minecraft.getMinecraft().thePlayer.posY;
                    Utils.sendChat("Joined Hypixel Says");

                }
            } catch (NullPointerException ignored) {}

            if (GameData.inHypixelSays && !event.entity.getName().equals(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {
                newPlayerUUIDs.add(event.entity.getUniqueID());
            }
        }
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if(worldJustLoaded && ip.equals("hypixel.net")) {
            worldJustLoaded = false;
            if (GameData.score >= 40) {
                Minecraft.getMinecraft().ingameGUI.displayTitle(String.format("\u00A7aYou won with \u00A76%s \u00A7apoints!", GameData.score), "", 10, 200, 20);
            }
            GameData.score = 0;
            if (GameData.apiKey == null) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/api new");
            }
        }

        //Run requeue methods
        if (GameData.inHypixelSays && !GameData.gameHasStarted && System.currentTimeMillis() - timeAtLastCheck >= 5000L) { //only check every five seconds (stupid /play cooldown)
            timeAtLastCheck = System.currentTimeMillis();

            //Leave empty queue
            if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean()) {

                try {
                    int playersInQueue = Integer.parseInt(StringUtils.stripControlCodes(ChatEvent.getSidebarLines().get(5).split("/")[0].split(" ")[1]));
                    if ((System.currentTimeMillis() - enteredQueueTime) / 5000 >= playersInQueue) { //the number of seconds in the queue is five times the number of players in the queue
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
                        return;
                    }
                } catch (Exception ignored) {}
            }

            //Blacklist
            if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean()) {
                Queue<UUID> newPlayerUUIDsCopy = new LinkedList<>(newPlayerUUIDs); //Poll removes UUIDs from the queue, and autodoge needs newPlayerUUIDs, so a copy is needed
                while (newPlayerUUIDsCopy.size() > 0) {
                    String newPlayerUUID = newPlayerUUIDsCopy.poll().toString().replace("-","");
                    for (String uuid : ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList()) {
                        if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(uuid, newPlayerUUID)) {
                            Utils.sendChat(String.format("\u00A7bYou were requeued to avoid %s", new JsonParser().parse(uuid).getAsJsonObject().getAsJsonPrimitive("name").toString().replace("\"","")));
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
                            return;
                        }
                    }
                }
            }

            //Autododge
            if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) {
                while (newPlayerUUIDs.size() > 0) {
                    try {
                        JsonObject playerData = Utils.httpGet("api.hypixel.net", GameData.apiKey, "player", "uuid", newPlayerUUIDs.poll().toString()).getAsJsonObject().getAsJsonObject("player");
                        String name = playerData.get("displayname").toString().replaceAll("^\"|\"$", "");
                        int wins = playerData.getAsJsonObject("stats").getAsJsonObject("Arcade").get("wins_simon_says").getAsInt();
                        List<String> playerNames = new ArrayList<String>() {{ Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().iterator().forEachRemaining(playerInfo -> add(playerInfo.getGameProfile().getName())); }};
                        if (wins >= ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt() && playerNames.contains(name)){
                            Utils.sendChat(String.format("\u00A7bYou were scared of %s's %s wins", name, wins));
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
                            return;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        //When the game starts, the player is teleported above the island
        if (Minecraft.getMinecraft().thePlayer.posY > joinYLevel + 3) {
            GameData.gameHasStarted = true;
        }
    }
}
