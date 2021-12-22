package com.yoursole.HypixelSays.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.Sys;

public class Utils {
    public static void getServer(){
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
                } catch (InterruptedException exception) {

                }
            }
        });

    }

}
