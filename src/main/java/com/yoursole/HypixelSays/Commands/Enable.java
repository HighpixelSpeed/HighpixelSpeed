package com.yoursole.HypixelSays.Commands;

import com.yoursole.HypixelSays.Data.GameData;
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
        if(args.length ==0){
            GameData.isEnabled=!GameData.isEnabled;

            if(GameData.isEnabled){
                Utils.sendChat("\u00A7§bHypixel says mode enabled");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§bUse /hs help for more options");
            }else{
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§bHypixel says mode Disabled");
            }

            GameData.tellraw = false;
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("forty")){
                GameData.fortyPointGame = !GameData.fortyPointGame;
                Utils.sendChat((GameData.lagMode)?"\u00A7§4[AUTOREQUE]: \u00A7§b40-point mode is enabled":"\u00A7§4[AUTOREQUE]: \u00A7§b40-point mode is disabled");
            }else if(args[0].equalsIgnoreCase("help")){
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs \u00A7§bToggle enabling the whole mod");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs forty \u00A7§bToggle requeuing if you cannot get 40 points");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs help \u00A7§bDisplay this message");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs lag \u00A7§bToggle lag mode");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs loss \u00A7§bToggle requeuing if you cannot win");
                Utils.sendChat("\u00A7§4[AUTOREQUE]: \u00A7§e/hs play \u00A7§bJoin Hypixel Says");
            }else if(args[0].equalsIgnoreCase("lag")){
                GameData.lagMode = !GameData.lagMode;
                Utils.sendChat((GameData.lagMode)?"\u00A7§4[AUTOREQUE]: \u00A7§bLag mode is enabled":"\u00A7§4[AUTOREQUE]: \u00A7§bLag mode is disabled");
            }else if(args[0].equalsIgnoreCase("loss")){
                GameData.queueOnLoss = !GameData.queueOnLoss;
                Utils.sendChat((GameData.queueOnLoss)?"\u00A7§4[AUTOREQUE]: \u00A7§bRequeuing on loss is enabled":"\u00A7§4[AUTOREQUE]: \u00A7§bRequeuing on loss is disabled");
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
        return new ArrayList<String>(){{add("help");add("lag");add("loss");add("play");}};
    }


}
