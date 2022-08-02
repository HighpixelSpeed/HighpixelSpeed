package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.HypixelSays;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.WorldEvent.Load;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class JoinWorld {
    
    private boolean worldJustLoaded = false;
    @SubscribeEvent
    public void onWorldLoad(Load event) {
        this.worldJustLoaded = true;
    }
    
    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if(this.worldJustLoaded) {
            this.worldJustLoaded = false;
            if (GameData.score >= 40){
            Minecraft.getMinecraft().ingameGUI.displayTitle(String.format("\u00A7aYou won with \u00A76%s \u00A7apoints!", GameData.score), "", 10, 100, 20);
            }
            GameData.score = 0;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
            GameData.tellraw=true;
        }
    }
}
