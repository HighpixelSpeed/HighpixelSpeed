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
                sender.addChatMessage(new ChatComponentText("§4[AUTOREQUE]: §bHypixel says mode Enabled"));
                sender.addChatMessage(new ChatComponentText("§4[AUTOREQUE]: §bLag mode enabled by default, do /hs lag to toggle"));
                GameData.lagMode = true;
            }
            else
                sender.addChatMessage(new ChatComponentText("§4[AUTOREQUE]: §bHypixel says mode Disabled"));

            GameData.tellraw = false;
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("lag")){
                GameData.lagMode = !GameData.lagMode;
                sender.addChatMessage(new ChatComponentText((GameData.lagMode)?"§4[AUTOREQUE]: §bLag mode is enabled":"§4[AUTOREQUE]: §bLag mode is disabled"));
            }
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<String>(){{add("enable");}};
    }


}
