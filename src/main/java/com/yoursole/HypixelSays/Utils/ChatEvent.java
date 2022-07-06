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
        if(!HypixelSays.get("Enabled", "Toggle enabling the whole mod"))
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
               Utils.sendChat("\u00A7§bJoined Hypixel Says");
               GameData.reset();
           }else{
               GameData.inHypixelSays=false;
               Utils.sendChat("\u00A7§bNot Hypixel Says");
               GameData.reset();
           }
            GameData.tellraw=false;
            
        }else if (GameData.inHypixelSays && message.startsWith("NEXT TASK")) {
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

            String a = sbScore(8);
            String b = sbScore(7);
            String c = sbScore(6);
            GameData.players[0] = sbPlayer(8);
            GameData.players[1] = sbPlayer(7);
            GameData.players[2] = sbPlayer(6);
            try{
                GameData.scores[0]=Integer.parseInt(a);
            }catch (NumberFormatException exx){
            }
            try{
                GameData.scores[1]=Integer.parseInt(b);
            }catch (NumberFormatException exx){
            }
            try{
                GameData.scores[2]=Integer.parseInt(c);
            }catch (NumberFormatException exx){
            }
            
            GameData.isOnePointer = Utils.isOnePointer(message);
            

            if (!GameData.isOnePointer){
                GameData.upForGrabs = 3;
            }else{
                GameData.upForGrabs = 1;
            }
            requeue(true);
            
        }else if (GameData.inHypixelSays && message.contains("finished") && !message.contains(":")){ //all player chats have ":" in them. filtering
            String finishedPlayer = message.split(" finished")[0];                                   //out colons guarantees none will be processed
            if (finishedPlayer.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString())){
                GameData.score = GameData.score + GameData.upForGrabs;
            }
            for (int i = 0; i < 3; i++){
                if (finishedPlayer.equals(GameData.players[i])){
                    GameData.scores[i] = GameData.scores[i] + GameData.upForGrabs;
                }
            }
            if (!GameData.isOnePointer && GameData.upForGrabs > 0 ){
                GameData.upForGrabs--;
            }
            
        }else if (GameData.inHypixelSays && message.contains("Game ended") && !message.contains(":")){
            GameData.round++;
            requeue(false);
            
        }else if (GameData.inHypixelSays && message.contains("disconnected") && !message.contains(":")){
            GameData.disconnectedPlayer = message.split(" disconnected")[0];
            if (GameData.disconnectedPlayer.equals(GameData.players[1])){
                GameData.secondPlaceLeft = true;
            }
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

        if (list.size() > 15){
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        }else{
            scores = list;
        }

        for (Score score : scores){
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

    static String sbScore(int i) {
        ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
        String line = lines.get(i);
        String a = StringUtils.stripControlCodes(line);
        a = a.split(":")[1].replace(" ","");
        a = cleanSB(a);
        return a;
    }
    
    static String sbPlayer(int i) {
        ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
        String line = lines.get(i);
        String a = StringUtils.stripControlCodes(line);
        a = a.split(":")[0].replace(" ","");
        a = cleanSB(a);
        return a;
    }
    
    static void requeue(boolean isNewTask) {
        int possiblePoints;
        if (GameData.isOnePointer && isNewTask){  //if this is executed on "Game ended," it needs to be counted as 3-pointer,
            int roundsleft = 16 - GameData.round; //in case the current round was a 1-pointer
            possiblePoints = (roundsleft*3)-2;
        }else{
            int roundsleft = 16 - GameData.round;
            possiblePoints = roundsleft*3;
        }
        if (GameData.secondPlaceLeft && !GameData.disconnectedPlayer.equals(GameData.players[1])){
            GameData.secondPlaceLeft = false;
        }
        if (possiblePoints+GameData.score<40 && HypixelSays.get("fortyPointGame", "Toggle requeuing if you cannot get 40 points")){
            Utils.sendChat("\u00A7§bYou did not get at least 40 points and were automatically requeued");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            GameData.reset();
        }else if (possiblePoints+GameData.scores[1]<GameData.score){
            Utils.sendChat("\u00A7§bYou won and were automatically requeued");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            GameData.reset();
        }else if (possiblePoints+GameData.score<GameData.scores[0] && HypixelSays.get("queueOnLoss", "Toggle requeuing if you cannot win")){
            Utils.sendChat("\u00A7§bYou did not win and were automatically requeued");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            GameData.reset();
        }else if (possiblePoints+GameData.scores[2]<GameData.score && GameData.secondPlaceLeft){
            Utils.sendChat("\u00A7§bYou won and were automatically requeued");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            GameData.reset();
        }
    }
}