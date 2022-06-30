package com.yoursole.HypixelSays.Utils;

import com.yoursole.HypixelSays.Data.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import org.lwjgl.Sys;

import java.util.Locale;

public class Utils {

/*
 if(!GameData.lagMode){
                Thread.sleep(200);
            }else{
                Thread.sleep(1000);
            }
 */    
    public static void sendChat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: " + message));
    }
    
    public static boolean isOnePointer(String message){
        message = StringUtils.stripControlCodes(message);
        if(!(message.startsWith("NEXT TASK")))
            return GameData.isOnePointer;
        message = message.toLowerCase();
        if(message.contains("press")){//press the button
            return true;
        }
        if(message.contains("throw eggs")){//throw eggs at other players
            return true;
        }
        if(message.contains("throw snowball")){//throw snowball at other players
            return true;
        }
        if(message.contains("give rud")){//give rudolph his shiny red nose
            return true;
        }
        if(message.contains("get into a minecart")){//get into a minecart
            return true;
        }
        if(message.contains("nether portal")){//enter the nether portal
            return true;
        }
        if(message.contains("end portal")){//enter the end portal
            return true;
        }
        if(message.contains("water safely without")){//jump into the water safely without hitting solid blocks
            return true;
        }
        if(message.contains("play a song")){//play a song with the noteblocks
            return true;
        }
        if(message.contains("grandma")){//give grandma a flower
            return true;
        }
        if(message.contains("on the spot")){//jump into the air on the spot
            return true;
        }
        if(message.contains("look at a") && message.contains("block")){//look at a block
            return true;
        }
        if(message.contains("extinguish yourself")){//extinguish yourself
            return true;
        }
        if(message.contains("bed")){//get in a bed
            return true;
        }
        if(message.contains("remove the poison")){//remove the poison effect
            return true;
        }
        if(message.contains("eggnog")){//drink the eggnog
            return true;
        }
        if(message.contains("completely still")){//stand completely still
            return true;
        }
        if(message.contains("santa's hat")){//wear santa's hat
            return true;
        }
        if(message.contains("look at the sky")){//look at the sky
            return true;
        }
        if(message.contains("throw a diamond")){//throw a diamond at a player
            return true;
        }
        if(message.contains("nod by")){//nod by looking up and down
            return true;
        }
        if(message.contains("look at a player")){//look at a player's head
            return true;
        }
        if(message.contains("wrapped present")){//give a player a wrapped present
            return true;
        }
        if(message.contains("on the cobblestone")){//stand on the cobblestone
            return true;
        }
        if(message.contains("spin around")){//spin around in a full circle
            return true;
        }
        if(message.contains("the horse")){//tame the horse by feeding it
            return true;
        }
        if(message.contains("open a present")){//open a present under the tree
            return true;
        }
        if(message.contains("give santa")){//give santa milk and cookies
            return true;
        }
        if(message.contains("on the ice")){//don't stand on the ice
            return true;
        }
        if(message.contains("remove the coal")){//remove the coal from your inventory
            return true;
        }
        return false;


    }

}
