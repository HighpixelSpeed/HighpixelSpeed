package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.Gui.ConfigHandler;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.WorldEvent.Load;

public class JoinWorld {
    
    private boolean worldJustLoaded = false;
    long timeSinceLastCheck = System.currentTimeMillis();
    String ip = "";
    
    @SubscribeEvent
    public void onWorldLoad(Load event) {
        worldJustLoaded = true;
        GameData.doServerCheck = true;
        GameData.enteredQueueTime = System.currentTimeMillis();
        try{
            ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        }catch (Exception ignored){}
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if(worldJustLoaded && ip.equals("hypixel.net")) {
            worldJustLoaded = false;
            if (GameData.score >= 40){
                Minecraft.getMinecraft().ingameGUI.displayTitle(String.format("\u00A7aYou won with \u00A76%s \u00A7apoints!", GameData.score), "", 10, 200, 20);
            }
            GameData.score = 0;
            if (GameData.apiKey == null) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/api new");
            }
        }

        //Leave empty queue
        if (GameData.inHypixelSays && !GameData.gameHasStarted && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean() && System.currentTimeMillis() - 1000L >= timeSinceLastCheck){ //only check every second

            timeSinceLastCheck = System.currentTimeMillis();
            int playersInQueue = 1000;
            try{
                playersInQueue = Integer.parseInt(StringUtils.stripControlCodes(ChatEvent.getSidebarLines().get(5).split("/")[0].split(" ")[1]));
            }catch (Exception ignored){}
            if ((System.currentTimeMillis() - GameData.enteredQueueTime) / 4000 >= playersInQueue){ //the number of seconds in the queue is four times the number of players in the queue
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }
        }
    }


    @SubscribeEvent
    public void onPlayerLogin(EntityJoinWorldEvent event) {
        try {
            if (GameData.doServerCheck && event.entity instanceof EntityPlayer && StringUtils.stripControlCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjective("PreScoreboard").getDisplayName()).endsWith(" SAYS")) { //Minigame is HYPIXEL SAYS or SANTA SAYS
                GameData.doServerCheck = false;
                GameData.inHypixelSays = true;
                GameData.gameHasStarted = false;
                Utils.sendChat("Joined Hypixel Says");
            }
        } catch (Exception ignored) {}

        if (GameData.inHypixelSays && event.entity instanceof EntityPlayer) {

            // Autododge


            //Blacklist

        }
    }
}
