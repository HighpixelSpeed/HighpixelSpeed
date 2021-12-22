package com.yoursole.HypixelSays.Commands;

import com.yoursole.HypixelSays.Data.GameData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class Enable extends CommandBase {

    @Override
    public String getCommandName() {
        return "enable";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/<command>";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameData.isEnabled=!GameData.isEnabled;



        if(GameData.isEnabled)
            sender.addChatMessage(new ChatComponentText("Hypixel says mode Enabled"));
        else
            sender.addChatMessage(new ChatComponentText("Hypixel says mode Disabled"));

        GameData.tellraw = false;
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
