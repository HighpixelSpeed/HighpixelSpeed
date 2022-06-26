package com.yoursole.HypixelSays.Utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.HypixelSays;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import scala.collection.parallel.ParIterableLike;

import java.util.*;
import java.util.stream.Collectors;

public class ChatEvent {
    @SubscribeEvent(receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent e){
        if(!GameData.isEnabled)
            return;
        String message = StringUtils.stripControlCodes(e.message.getFormattedText());
        if(GameData.tellraw){

            e.setCanceled(true);
            JsonObject o = new JsonObject();
            try{
                o = new JsonParser().parse(message).getAsJsonObject();
            }catch (Exception ex){
                return;
            }
            String mode = "";
            try{
                mode = o.get("mode").toString();
            }catch (Exception exx){
                GameData.inHypixelSays=false;
                GameData.reset();
            }

           if(mode.equalsIgnoreCase("\"santa_says\"")||mode.equalsIgnoreCase("\"simon_says\"")){
               GameData.inHypixelSays=true;
               Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bJoined Hypixel Says"));
               GameData.reset();
           }else{
               GameData.inHypixelSays=false;
               Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bNot Hypixel Says"));
               GameData.reset();
           }

            GameData.tellraw=false;
        }else if(GameData.inHypixelSays && message.toLowerCase().startsWith("next task")){
            GameData.round++;
            ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
            for(String line : lines){
                if(line.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) &&line.split(":").length==2 ){
                    String a = StringUtils.stripControlCodes(line);
                    a = a.split(":")[1].replace(" ","");
                    a = cleanSB(a);
                    try {
                        GameData.score = Integer.parseInt(a);
                    }catch (NumberFormatException ex){
                        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("ERROR: Something went wrong"));
                    }

                }
            }

            boolean isOnePointer = Utils.isOnePointer(message);
            int possiblePoints;

            String firstLine = lines.get(8);
            String secondLine = lines.get(7);
            String a = StringUtils.stripControlCodes(firstLine);
            String b = StringUtils.stripControlCodes(secondLine);
            a = cleanSB(a.split(":")[1].replace(" ",""));
            b = cleanSB(b.split(":")[1].replace(" ",""));
            try{
                GameData.firstPlaceScore=Integer.parseInt(a);
            }catch (NumberFormatException exx){
            }
            try{
                GameData.secondPlaceScore=Integer.parseInt(b);
            }catch (NumberFormatException exx){
            }
            
            if(!isOnePointer){
                int roundsleft = 16 - GameData.round;
                possiblePoints = roundsleft*3;
            }else{
                int roundsleft = 15 - GameData.round;
                possiblePoints = (roundsleft*3)+1;
            }
            
            if(possiblePoints+GameData.secondPlaceScore<GameData.score){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bYou won and were automatically requeued"));
            }else if(possiblePoints+GameData.score<GameData.firstPlaceScore && GameData.queueOnLoss){
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7§4[AUTOREQUE]: \u00A7§bYou did not win and were automatically requeued"));
            }
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            GameData.reset();
        }
    }

    //thanks sychic for sending this from the DSM github
    public static List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        if (Minecraft.getMinecraft().theWorld == null) return lines;
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) return lines;

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return lines;

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#"))
                .collect(Collectors.toList());

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }
    public static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127) {
                cleaned.append(c);
            }
        }

        return cleaned.toString();
    }
}
