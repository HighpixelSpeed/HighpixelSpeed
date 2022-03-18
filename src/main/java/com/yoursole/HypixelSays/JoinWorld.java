package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class JoinWorld {
    private HashMap<UUID, Long> cooldowns = new HashMap();
    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e){
        if(!GameData.isEnabled)
            return;
        int cooldownTime = 2;
        if (cooldowns.containsKey(e.entity.getUniqueID())) {
            long secondsLeft = cooldowns.get(e.entity.getUniqueID()) / 1000 + cooldownTime - System.currentTimeMillis() / 1000;
            if (secondsLeft > 0) {
                return;
            }
        }
        if(e.entity instanceof EntityPlayer && e.entity.getUniqueID() == Minecraft.getMinecraft().thePlayer.getUniqueID()){
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
                    GameData.tellraw=true;
                    t.cancel();
                }
            }, GameData.lagMode?1000:200);
        }
        cooldowns.put(e.entity.getUniqueID(),System.currentTimeMillis());

    }

    @SubscribeEvent
    public void onLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
        GameData.isEnabled = false;
        GameData.reset();
    }
}
