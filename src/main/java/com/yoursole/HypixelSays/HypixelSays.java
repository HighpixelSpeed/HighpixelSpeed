package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Commands.Enable;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = HypixelSays.MODID, version = HypixelSays.VERSION)
public class HypixelSays
{
    public static HypixelSays mod;
    public static final String MODID = "hypixelsays";
    public static final String VERSION = "1.0";
    @EventHandler
    public void init(FMLInitializationEvent e) {
        //CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        //ch.registerCommand(new Enable());
        MinecraftForge.EVENT_BUS.register(new JoinWorld());
        MinecraftForge.EVENT_BUS.register(new ChatEvent());
        ClientCommandHandler.instance.registerCommand(new Enable());
        mod = this;
    }

//    @EventHandler
//    public void start(FMLServerStartingEvent e){
//        if(e.getServer().isSinglePlayer())
//            return;
//
//       // e.registerServerCommand(new Enable());
//    }


}

