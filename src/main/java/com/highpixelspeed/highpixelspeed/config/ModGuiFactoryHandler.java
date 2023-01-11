package com.highpixelspeed.highpixelspeed.config;

import com.highpixelspeed.highpixelspeed.HighpixelSpeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//Inspired by NovaViper/ZeroQuest config layout
public class ModGuiFactoryHandler implements IModGuiFactory {
    
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }
     
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ModGuiConfig.class;
    }
 
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
    {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }

    public static class ModGuiConfig extends GuiConfig {

        public ModGuiConfig(GuiScreen guiScreen) {
            super(guiScreen, getConfigElements(), HighpixelSpeed.MODID, false, false, I18n.format("gui.config.general"));
        }

        //Main config screen elements
        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>(new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL)).getChildElements());
            list.add(new DummyCategoryElement(ConfigHandler.CATEGORY_AUTODODGE, "gui.config.autododge", AutoDodgeEntry.class));
            list.add(new DummyCategoryElement(ConfigHandler.CATEGORY_BLACKLIST, "gui.config.blacklist", BlacklistEntry.class));
            return list;
        }

        //Auto dodge config screen
        public static class AutoDodgeEntry extends GuiConfigEntries.CategoryEntry {

            public AutoDodgeEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement element) {
                super(owningScreen, owningEntryList, element);
            }

            @Override
            protected GuiScreen buildChildScreen() {

                return new GuiConfig(
                    this.owningScreen,
                    new ArrayList<>((new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE))).getChildElements()),
                    this.owningScreen.modID,
                    ConfigHandler.CATEGORY_AUTODODGE,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    I18n.format("gui.config.general"),
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).getComment()
                );
            }
        }

        //Blacklist config screen
        public static class BlacklistEntry extends GuiConfigEntries.CategoryEntry {

            public BlacklistEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement element) {
                super(owningScreen, owningEntryList, element);
            }

            @Override
            protected GuiScreen buildChildScreen() {

                return new GuiConfig(
                        this.owningScreen,
                        new ArrayList<>((new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST))).getChildElements()),
                        this.owningScreen.modID,
                        ConfigHandler.CATEGORY_BLACKLIST,
                        this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        I18n.format("gui.config.general"),
                        ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).getComment()
                );
            }
        }
    }
}
