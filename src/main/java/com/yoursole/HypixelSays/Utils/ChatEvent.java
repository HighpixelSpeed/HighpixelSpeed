package com.yoursole.HypixelSays.Utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yoursole.HypixelSays.Data.GameData;
import com.yoursole.HypixelSays.HypixelSays;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
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
         
public class ChatEvent {
    @SubscribeEvent(receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent e){
        if(!HypixelSays.get("Enabled"))
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
                GameData.gameHasStarted = false;
                GameData.reset();
            }

           if(mode.equalsIgnoreCase("\"santa_says\"")||mode.equalsIgnoreCase("\"simon_says\"")){
               GameData.inHypixelSays=true;
               Utils.sendChat("\u00A7bJoined Hypixel Says");
               GameData.reset();
           }else{
               GameData.inHypixelSays=false;
               GameData.gameHasStarted = false;
               GameData.reset();
           }
            GameData.tellraw=false;
            
        }else if (GameData.inHypixelSays && message.startsWith("NEXT TASK")) {
            ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
            for(String line : lines){
                if(line.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) &&line.split(":").length==2 ){
                    String a = StringUtils.stripControlCodes(line);
                    a = a.split(":")[1].replace(" ","");
                    a = clean(a);
                    try {
                        GameData.score = Integer.parseInt(a);
                    }catch (NumberFormatException ex){
                    }
                }
            }

            for (int i = 0; i < 3; i++){
                String score = sbScore(8 - i);
                GameData.players[i] = sbPlayer(8 - i);
                try{
                    GameData.scores[i]=Integer.parseInt(score);
                }catch (NumberFormatException exx){
                }
            }
            
            GameData.isOnePointer = Utils.isOnePointer(message);
            

            if (!GameData.isOnePointer){
                GameData.upForGrabs = 3;
            }else{
                GameData.upForGrabs = 1;
            }
            checkRequeue(true);
            
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
            checkRequeue(false);
        }
        
        if (GameData.inHypixelSays && message.startsWith("The game starts in 1 ")) { //avoid checking tab in queue
            GameData.tabList = new ArrayList<>(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap());
            GameData.gameHasStarted = true;
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
    
    public static String clean(String scoreboard) {
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
        a = clean(a);
        return a;
    }
    
    static String sbPlayer(int i) {
        ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
        String line = lines.get(i);
        String a = StringUtils.stripControlCodes(line);
        a = a.split(":")[0].replace(" ","");
        a = clean(a);
        return a;
    }
    
    static void checkRequeue(boolean isNewTask) {
        Collection<NetworkPlayerInfo> tabList = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
        List<String> playerNames = new ArrayList<String>() {{ tabList.iterator().forEachRemaining(playerInfo -> add(playerInfo.getGameProfile().getName())); }};
        List<String> disconnectedPlayers = new ArrayList<String>();
        if (!tabList.containsAll(GameData.tabList)){
            Iterator<NetworkPlayerInfo> oldList = GameData.tabList.iterator();
            oldList.forEachRemaining(playerInfo -> {
                String playerName = playerInfo.getGameProfile().getName();
                if (!playerNames.contains(playerName)){
                    disconnectedPlayers.add(playerName);
                }
            });
        }
        GameData.secondPlaceLeft = false;
        if (disconnectedPlayers.contains(GameData.players[1]) && tabList.size() != 1){
            GameData.secondPlaceLeft = true;
        }
        
        int possiblePoints;
        if (GameData.isOnePointer && isNewTask){  //if this is executed on "Game ended," it needs to be counted as 3-pointer,
            int roundsleft = 16 - GameData.round; //in case the current round was a 1-pointer
            possiblePoints = (roundsleft*3)-2;
        }else{
            int roundsleft = 16 - GameData.round;
            possiblePoints = roundsleft*3;
        }
        
        if (possiblePoints+GameData.score<40 && HypixelSays.get("Forty Point Mode") && HypixelSays.get("Forty Point Only")){
            Utils.sendChat("\u00A7bYou could not get at least 40 points and were automatically requeued");
            requeue();
        }else if (possiblePoints+GameData.score>=40 && HypixelSays.get("Forty Point Mode")){
            return;
        }else if (possiblePoints+GameData.scores[1]<GameData.score){
            Utils.sendChat("\u00A7bYou won and were automatically requeued");
            requeue();
        }else if (possiblePoints+GameData.score<GameData.scores[0] && HypixelSays.get("Queue On Loss")){
            Utils.sendChat("\u00A7bYou could not win and were automatically requeued");
            requeue();
        }else if (possiblePoints+GameData.scores[2]<GameData.score && GameData.secondPlaceLeft){
            Utils.sendChat("\u00A7bThe player in 2nd place left, so you won and were automatically requeued");
            requeue();
        }
    }
    
    static void requeue() {
        GameData.gameEnded = true;
        Utils.sendChat(String.format("After " + (GameData.round - 1) + " rounds:"));
        Utils.sendChat("\u00A7m                         ");
        for (int line = 8; line >= 6; line--){
            Utils.sendChat(getSidebarLines().get(line));
        }
        Utils.sendChat("\u00A7m                         ");
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
        GameData.reset();
    }
}