package com.yoursole.HypixelSays.Commands;

import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.HypixelSays;
import com.yoursole.HypixelSays.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import net.minecraftforge.common.config.Configuration;////

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
            HypixelSays.toggle("Enabled", "Toggle enabling the whole mod");

            if(HypixelSays.get("Enabled", "Toggle enabling the whole mod")){
                Utils.sendChat("\u00A7§bHypixel says mode enabled");
                Utils.sendChat("\u00A7§bUse /hs help for more options");
            }else{
                Utils.sendChat("\u00A7§bHypixel says mode Disabled");
            }
            GameData.tellraw = false;
            
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("forty")){
                HypixelSays.toggle("Forty Point Mode", "Toggle requeuing if you cannot get 40 points");
                Utils.sendChat((HypixelSays.get("Forty Point Mode", "Toggle requeuing if you cannot get 40 points"))?"\u00A7§b40-point mode is enabled":"\u00A7§b40-point mode is disabled");
            }else if(args[0].equalsIgnoreCase("help")){
                Utils.sendChat(String.format("\u00A7§%s/hs \u00A7§bToggle enabling the whole mod", (HypixelSays.get("Enabled", "Toggle enabling the whole mod"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7§%s/hs forty \u00A7§bToggle requeuing if you cannot get 40 points", (HypixelSays.get("Forty Point Mode", "Toggle requeuing if you cannot get 40 points"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7§e/hs help \u00A7§bDisplay this message"));
                Utils.sendChat(String.format("\u00A7§%s/hs lag \u00A7§bToggle lag mode", (HypixelSays.get("Lag Mode", "Toggle lag mode"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7§%s/hs loss \u00A7§bToggle requeuing if you cannot win", (HypixelSays.get("Queue On Loss", "Toggle requeuing if you cannot win"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7§e/hs play \u00A7§bJoin Hypixel Says"));
            }else if(args[0].equalsIgnoreCase("lag")){
                HypixelSays.toggle("Lag Mode", "Toggle lag mode");
                Utils.sendChat((HypixelSays.get("Lag Mode", "Toggle lag mode"))?"\u00A7§bLag mode is enabled":"\u00A7§bLag mode is disabled");
            }else if(args[0].equalsIgnoreCase("loss")){
                HypixelSays.toggle("Queue On Loss", "Toggle requeuing if you cannot win");
                Utils.sendChat((HypixelSays.get("Queue On Loss", "Toggle requeuing if you cannot win"))?"\u00A7§bRequeuing on loss is enabled":"\u00A7§bRequeuing on loss is disabled");
            }else if(args[0].equalsIgnoreCase("play")){
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 0){
            return new ArrayList<String>(Arrays.asList("forty", "help", "lag", "loss", "play"));
        }
        if(args.length == 1){
            ArrayList<String> options = new ArrayList<String>();
            for (String i : new ArrayList<String>(Arrays.asList("forty", "help", "lag", "loss", "play"))){
                if (i.startsWith(args[0])){
                    options.add(i);
                }
            }
            return options;
        }
        return new ArrayList<String>();
    }
}
