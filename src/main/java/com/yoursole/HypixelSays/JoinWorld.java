package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.StringUtils;
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
        GameData.enteredQueue = System.currentTimeMillis();
        try{
            ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        }catch (Exception ex){}
    }
    
    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
    if(worldJustLoaded && ip == "hypixel.net") {
            worldJustLoaded = false;
            if (GameData.score >= 40){
                Minecraft.getMinecraft().ingameGUI.displayTitle(String.format("\u00A7aYou won with \u00A76%s \u00A7apoints!", GameData.score), "", 10, 100, 20);
            }
            GameData.score = 0;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
            GameData.tellraw=true;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/api new");
        }
        if (GameData.inHypixelSays && !GameData.gameHasStarted && System.currentTimeMillis() - 1000L >= timeSinceLastCheck){ //only check every second
            timeSinceLastCheck = System.currentTimeMillis();
            int playersInQueue = 1000;
            try{
                playersInQueue = Integer.valueOf(StringUtils.stripControlCodes(ChatEvent.getSidebarLines().get(5).split("/")[0].split(" ")[1]));
            }catch (Exception ex){}
            if ((System.currentTimeMillis() - GameData.enteredQueue) / 4000 >= playersInQueue){ //the number of seconds in the queue is four times the number of players in the queue
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }
        }
        if (GameData.gameHasStarted){
            BossStatus.setBossStatus(new EntityWither(Minecraft.getMinecraft().theWorld), false);
            BossStatus.bossName = String.valueOf(GameData.score);
            BossStatus.healthScale = 0f;
        }
    }
}
