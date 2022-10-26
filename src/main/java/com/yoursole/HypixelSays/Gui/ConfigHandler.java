package com.yoursole.HypixelSays.Gui;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

//Inspired by NovaViper/ZeroQuest config layout
public class ConfigHandler {

    public static Configuration config;
    public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
    //public static final String CATEGORY_AUTODODGE = "autododge";
    //public static final String CATEGORY_BLACKLIST = "blacklist";

    public static void init(File file) {
        config = new Configuration(file);
        config.addCustomCategoryComment(CATEGORY_GENERAL, I18n.format("gui.config.general.tooltip"));
        //config.setCategoryPropertyOrder(CATEGORY_GENERAL, order)
        //config.addCustomCategoryComment(CATEGORY_AUTODODGE, I18n.format("gui.config.autododge.tooltip"));
        //config.addCustomCategoryComment(CATEGORY_BLACKLIST, I18n.format("gui.config.blacklist.tooltip"));
        config.load();

        config.get(CATEGORY_GENERAL, "Enabled", false, "Enable the whole mod");
        config.get(CATEGORY_GENERAL, "Forty Point Mode", false, "Cancel requeuing if you can get 40 points");
        config.get(CATEGORY_GENERAL, "Forty Point Only", false, "If Forty Point Mode is true, requeue if you cannot get 40 points");
        config.get(CATEGORY_GENERAL, "Leave Empty Queue", false, "Requeue if there aren't enough players to start");
        config.get(CATEGORY_GENERAL, "Queue On Loss", false, "Requeue if you cannot win");

        /*config.get(CATEGORY_AUTODODGE, "Enabled", false, "Enable auto dodge");
        config.get(CATEGORY_AUTODODGE, "Leaderboard Criteria", false, "Queue dodge people with a certain leaderboard rank");
        config.get(CATEGORY_AUTODODGE, "Leaderboard Threshold", 1000, "The rank on the leaderboard the player must have");
        config.get(CATEGORY_AUTODODGE, "Wins Criteria", false, "Queue dodge people with a certain number of wins");
        config.get(CATEGORY_AUTODODGE, "Wins Threshold", 100, "The number of wins the player must have");
        config.get(CATEGORY_AUTODODGE, "Win-Loss Ratio Criteria", false, "Queue dodge people with a certain win-loss ratio");
        config.get(CATEGORY_AUTODODGE, "Win-Loss Ratio Threshold", 0.0 , "The percentage of rounds the player must have won (~67% of rounds are competitive, so practical maximum)", 0.0, 66.7);

        config.get(CATEGORY_BLACKLIST, "Enabled", false, "Enable queue dodging blacklisted players");
        config.get(CATEGORY_BLACKLIST, "Blacklisted Players", new String[] {"Sample_player"}, "Queue dodge these players");*/

        config.save();
    }

    public static void toggle(String category, String property) {
        config.getCategory(category).get(property)
            .set(!config.getCategory(category).get(property).getBoolean());
    }
}
