package com.yoursole.HypixelSays.Commands;

import com.yoursole.HypixelSays.Data.GameData;
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



            if(GameData.isEnabled) {
                sender.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bHypixel says mode Enabled"));
                sender.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bLag mode disabled by default, do /hs lag to toggle"));
                sender.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bRequeuing if game is guaranteed loss disabled by default, do /hs loss to toggle"));
                GameData.lagMode = false;
                GameData.queueOnLoss = false;
            }
            else
                sender.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bHypixel says mode Disabled"));

            GameData.tellraw = false;
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("lag")){
                GameData.lagMode = !GameData.lagMode;
                sender.addChatMessage(new ChatComponentText((GameData.lagMode)?"\u00A7§4[AUTOREQUE]: \u00A7§bLag mode is enabled":"\u00A7§4[AUTOREQUE]: \u00A7§bLag mode is disabled"));
            }else if(args[0].equalsIgnoreCase("loss")){
                GameData.queueOnLoss = !GameData.queueOnLoss;
                sender.addChatMessage(new ChatComponentText((GameData.queueOnLoss)?"\u00A7§4[AUTOREQUE]: \u00A7§bRequeuing on loss is enabled":"\u00A7§4[AUTOREQUE]: \u00A7§bRequeuing on loss is disabled"));
            }
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<String>(){{add("lag");add("loss");}};
    }


}
