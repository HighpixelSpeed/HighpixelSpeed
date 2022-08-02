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
            HypixelSays.toggle("Enabled");

            if(HypixelSays.get("Enabled")){
                Utils.sendChat("Hypixel says mode enabled");
                Utils.sendChat("Use /hs help for options");
            }else{
                Utils.sendChat("Hypixel says mode Disabled");
            }
            GameData.tellraw = false;
            
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("forty")){
                HypixelSays.toggle("Forty Point Mode");
                Utils.sendChat((HypixelSays.get("Forty Point Mode"))?"40-point mode is enabled":"40-point mode is disabled");
            }else if(args[0].equalsIgnoreCase("fortyonly")){
                HypixelSays.toggle("Forty Point Only");
                Utils.sendChat((HypixelSays.get("Forty Point Only"))?"40-point only mode is enabled" + ((HypixelSays.get("Forty Point Mode"))?"":", but Forty Point Mode is disabled"):"40-point only mode is disabled");
            }else if(args[0].equalsIgnoreCase("help")){
                Utils.sendChat(String.format("\u00A7%s/hs \u00A7bEnable the whole mod", (HypixelSays.get("Enabled"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs forty \u00A7bCancel requeuing if you can get 40 points", (HypixelSays.get("Forty Point Mode"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs fortyonly \u00A7bIf Forty Point Mode is true, requeue if you cannot get 40 points", (HypixelSays.get("Forty Point Only"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7e/hs help \u00A7bDisplay this message"));
                Utils.sendChat(String.format("\u00A7%s/hs loss \u00A7bToggle requeuing if you cannot win", (HypixelSays.get("Queue On Loss"))?"a":"c"));
                Utils.sendChat(String.format("\u00A7e/hs play \u00A7bJoin Hypixel Says"));
            }else if(args[0].equalsIgnoreCase("loss")){
                HypixelSays.toggle("Queue On Loss");
                Utils.sendChat((HypixelSays.get("Queue On Loss"))?"Requeuing on loss is enabled":"Requeuing on loss is disabled");
            }else if(args[0].equalsIgnoreCase("play")){
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }
        HypixelSays.config.save();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 0){
            return new ArrayList<String>(Arrays.asList("forty", "fortyonly", "help", "loss", "play"));
        }
        if(args.length == 1){
            ArrayList<String> options = new ArrayList<String>();
            for (String i : new ArrayList<String>(Arrays.asList("forty", "fortyonly", "help", "loss", "play"))){
                if (i.startsWith(args[0])){
                    options.add(i);
                }
            }
            return options;
        }
        return new ArrayList<String>();
    }
}
