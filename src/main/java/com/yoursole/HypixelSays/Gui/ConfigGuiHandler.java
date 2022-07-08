package com.yoursole.HypixelSays.Gui;

import com.yoursole.HypixelSays.HypixelSays;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGuiHandler extends GuiConfig {

    public ConfigGuiHandler(GuiScreen parent) {
        super(parent, new ConfigElement(HypixelSays.config.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
            HypixelSays.MODID, false,  false, "Hypixel Says Configuration Options");
    }
}
