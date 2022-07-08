package com.yoursole.HypixelSays;

import com.yoursole.HypixelSays.Commands.Enable;
import com.yoursole.HypixelSays.Utils.ChatEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = HypixelSays.MODID, version = HypixelSays.VERSION, guiFactory = "com.yoursole.HypixelSays.Gui.ModGuiFactoryHandler")
public class HypixelSays {
    public static HypixelSays mod;
    public static final String MODID = "hypixelsays";
    public static final String VERSION = "@VERSION@";
    public static Configuration config = null;
    
    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        config.get(Configuration.CATEGORY_CLIENT, "Enabled", false, "Toggle enabling the whole mod");
        config.get(Configuration.CATEGORY_CLIENT, "Forty Point Mode", false, "Toggle requeuing if you cannot get 40 points");
        config.get(Configuration.CATEGORY_CLIENT, "Lag Mode", false, "Toggle lag mode");
        config.get(Configuration.CATEGORY_CLIENT, "Queue On Loss", false, "Toggle requeuing if you cannot win");
        config.save();

    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
        //CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        //ch.registerCommand(new Enable());
        MinecraftForge.EVENT_BUS.register(new JoinWorld());
        MinecraftForge.EVENT_BUS.register(new ChatEvent());
        ClientCommandHandler.instance.registerCommand(new Enable());
        mod = this;
    }

    public static boolean get(String property, String comment) {
        return config.get(Configuration.CATEGORY_CLIENT, property, false, comment).getBoolean();
    }
    
    public static void toggle(String property, String comment) {
        config.get(Configuration.CATEGORY_CLIENT, property, false, comment).set(!config.get(Configuration.CATEGORY_CLIENT, property, false, comment).getBoolean());
    }
}
