package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
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
        if(e.entity instanceof EntityPlayer && e.entity.getUniqueID()== Minecraft.getMinecraft().thePlayer.getUniqueID()){
            Utils.getServer();
            GameData.tellraw=true;
        }
        cooldowns.put(e.entity.getUniqueID(),System.currentTimeMillis());

    }
}
