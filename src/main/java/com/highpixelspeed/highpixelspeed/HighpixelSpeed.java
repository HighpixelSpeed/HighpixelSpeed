package com.highpixelspeed.highpixelspeed;

import com.highpixelspeed.highpixelspeed.commands.HsCommand;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.utils.ChatEvent;
import com.highpixelspeed.highpixelspeed.utils.JoinWorld;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HighpixelSpeed.MODID, version = HighpixelSpeed.VERSION, guiFactory = "com.highpixelspeed.highpixelspeed.config.ModGuiFactoryHandler")
public class HighpixelSpeed {
    public static HighpixelSpeed mod;
    public static final String MODID = "highpixelspeed";
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
        ClientCommandHandler.instance.registerCommand(new HsCommand());
        mod = this;
    }
}
