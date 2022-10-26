package com.yoursole.HypixelSays.Commands;

import com.yoursole.HypixelSays.Gui.ConfigHandler;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enable extends CommandBase {

    @Override
    public String getCommandName() {
        return "hs";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/<command>";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0){
            ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Enabled");
            if(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean()){
                Utils.sendChat("Hypixel says mod enabled");
                Utils.sendChat("Use /hs help for options");
            } else {
                Utils.sendChat("Hypixel says mod disabled");
            }

        } else if(args.length == 1){
            if(args[0].equalsIgnoreCase("help")){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
                Utils.sendChat("\u00A7e/hs help \u00A7bDisplay this message\n");
                Utils.sendChat(String.format("\u00A7%s/hs \u00A7bEnable the whole mod", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean())?"a":"c"));
                //Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bDodge players with a certain leaderboard rank, win count, or win-loss ratio. Use \u00A7e/hs autododge help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs empty \u00A7bRequeue if there aren't enough players to start", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs forty \u00A7bCancel requeuing if you can get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs fortyonly \u00A7bIf Forty Point Mode is true, requeue if you cannot get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs loss \u00A7bToggle requeuing if you cannot win", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean())?"a":"c"));
                Utils.sendChat("\u00A7e/hs play \u00A7bJoin Hypixel Says");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
            }

            /*else if(args[0].equalsIgnoreCase("autododge")){
                ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"Auto dodge is enabled":"Auto dodge is disabled");
            }*/ else if(args[0].equalsIgnoreCase("empty")){
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Leave Empty Queue");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean())?"Leave empty queues is enabled":"Leave empty queues is disabled");
            } else if(args[0].equalsIgnoreCase("forty")){
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Mode");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"40-point mode is enabled":"40-point mode is disabled");
            } else if(args[0].equalsIgnoreCase("fortyonly")){
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Only");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean())?"40-point only mode is enabled" + ((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"":", but Forty Point Mode is disabled"):"40-point only mode is disabled");
            } else if(args[0].equalsIgnoreCase("loss")){
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Queue On Loss");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean())?"Requeuing on loss is enabled":"Requeuing on loss is disabled");
            } else if(args[0].equalsIgnoreCase("play")){
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }

        } /*else if(args.length == 2){
            if(args[0].equalsIgnoreCase("autododge")){
                if(args[1].equalsIgnoreCase("help")){
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
                    Utils.sendChat("\u00A7e/hs autododge help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bDodge players with a certain leaderboard rank, win count, or win-loss ratio", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"a":"c"));
                    Utils.sendChat(String.format("\u00A7%s/hs autododge leaderboard \u00A7bQueue dodge people with a certain leaderboard rank", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Leaderboard Criteria").getBoolean())?"a":"c"));
                    Utils.sendChat(String.format("\u00A7%s/hs autododge wins \u00A7bQueue dodge people with a certain leaderboard rank", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Criteria").getBoolean())?"a":"c"));
                    Utils.sendChat(String.format("\u00A7%s/hs autododge WLR \u00A7bQueue dodge people with a certain leaderboard rank", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Win-Loss Ratio Criteria").getBoolean())?"a":"c"));
                    Utils.sendChat("Change the autododge criteria thresholds in the mod configuration options");
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
                }

                else if(args[1].equalsIgnoreCase("leaderboard")){
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Leaderboard Criteria");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Leaderboard Criteria").getBoolean())?"Leaderboard dodging is enabled":"Leaderboard dodging is disabled");
                } else if(args[1].equalsIgnoreCase("wins")){
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Wins Criteria");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Criteria").getBoolean())?"Wins dodging is enabled":"Wins dodging is disabled");
                } else if(args[1].equalsIgnoreCase("WLR")){
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Win-Loss Ratio Criteria");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Win-Loss Ratio Criteria").getBoolean())?"WLR dodging is enabled":"WLR dodging is disabled");
                }
            } else if(args[0].equalsIgnoreCase("blacklist")){
                if(args[1].equalsIgnoreCase("help")){
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
                    Utils.sendChat("\u00A7e/hs blacklist help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs blacklist \u00A7b  <sampletext>  ", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"a":"c"));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7b\u00A7m                                                                             "));
                }

                else if(args[1].equalsIgnoreCase("leaderboard")){
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_BLACKLIST, "Leaderboard Criteria");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Leaderboard Criteria").getBoolean())?"Leaderboard dodging is enabled":"Leaderboard dodging is disabled");
                } else if(args[1].equalsIgnoreCase("wins")){
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_BLACKLIST, "Wins Criteria");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Wins Criteria").getBoolean())?"Wins dodging is enabled":"Wins dodging is disabled");
                }
            }
        }*/

        ConfigHandler.config.save();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
       if(args.length == 1){
            return matchingArgs(args[0], new ArrayList<>(Arrays.asList("autododge", "blacklist", "empty", "forty", "fortyonly", "help", "loss", "play")));
       } /*else if(args.length == 2){
           if(args[0].equalsIgnoreCase("autododge")) {
               return matchingArgs(args[1], new ArrayList<String>(Arrays.asList("help", "leaderboard", "wins", "WLR")));
           } else if(args[0].equalsIgnoreCase("blacklist")) {
               return matchingArgs(args[1], new ArrayList<String>(Arrays.asList("help", "other options")));
           }
       }*/
        return new ArrayList<>();
    }

    public ArrayList<String> matchingArgs(String arg, ArrayList<String> options) {
        options.removeIf(i -> !i.toLowerCase().startsWith(arg.toLowerCase()));
        return options;
    }
}
