package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Commands.Enable;
import com.yoursole.HypixelSays.Gui.ConfigHandler;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HypixelSays.MODID, version = HypixelSays.VERSION, guiFactory = "com.yoursole.HypixelSays.Gui.ModGuiFactoryHandler")
public class HypixelSays {
    public static HypixelSays mod;
    public static final String MODID = "hypixelsays";
    public static final String VERSION = "@VERSION@";

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new JoinWorld());
        MinecraftForge.EVENT_BUS.register(new ChatEvent());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        ClientCommandHandler.instance.registerCommand(new Enable());
        mod = this;
    }
}
