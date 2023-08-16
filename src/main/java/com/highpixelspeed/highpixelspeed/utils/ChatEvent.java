package com.highpixelspeed.highpixelspeed.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.data.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatEvent {

    @SubscribeEvent(receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent event){

        String message = StringUtils.stripControlCodes(event.message.getFormattedText());

        if (GameData.doPartyCheck > 0) { //check for beginning and ending borders of party info
            if (GameData.doPartyCheck == 1) {
                event.setCanceled(true); //cancel sending party info after the first border
            }
            if (message.contains("-----------------")) {
                event.setCanceled(true); //cancel sending the first border
                GameData.doPartyCheck--;
            } else if (message.contains("Party Members (")) {
                GameData.isInParty = true;
            }
        } else if (GameData.sendLeaderboardChat) {
            GameData.chatsRemaining--;
            if (GameData.chatsRemaining == 0) {
                requeue(false);
            }
        }

        if(!ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean()) return;
        if(!GameData.inHypixelSays) {
            if (message.contains("Welcome back to Hypixel Says") && !message.contains(":")) {
                GameData.inHypixelSays = true;
                GameData.addSessionGame = true;
                GameData.sessionGamesPlayed--;
            } else return;
        }

        if (message.startsWith("NEXT TASK")){
            ArrayList<String> lines = (ArrayList<String>) getSidebarLines();
            for(String line : lines){
                if(line.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) && line.split(":").length==2 ){
                    String a = StringUtils.stripControlCodes(line);
                    a = a.split(":")[1].replace(" ","");
                    a = clean(a);
                    try {
                        GameData.score = Integer.parseInt(a);
                    } catch (NumberFormatException ignored){}
                }
            }

            for (int i = 0; i < 3; i++){
                String score = sbScore(8 - i);
                GameData.players[i] = sbPlayer(8 - i);
                try{
                    GameData.scores[i]=Integer.parseInt(score);
                } catch (NumberFormatException ignored){}
            }

            GameData.isOnePointer = Utils.isOnePointer(message);
            if (!GameData.isOnePointer){
                GameData.upForGrabs = 3;
            } else{
                GameData.upForGrabs = 1;
            }
            checkRequeue(true);

        } else if (message.contains("finished") && !message.contains(":")){ //all player chats have ":" in them. filtering
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

        } else if (message.contains("Game ended") && !message.contains(":")){
            GameData.doRoundCheck = true; //Unnecessary except after rejoin...
            GameData.round++; //Because this is here because it's necessary for...
            checkRequeue(false); //This

        } else if (message.contains(" disconnected ") && !message.contains(":")){
            String disconnectedPlayer = message.split(" disconnected ")[0];
            if (disconnectedPlayer.equals(GameData.players[1])){
                checkRequeue(false);
            }

        } else if (message.contains("1st Place - ") && !message.contains(":")){
            GameData.sendLeaderboardChat = true;
            if (message.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {
                GameData.addSessionWin();
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
        List<Score> list = scores.stream().filter(input ->
            input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (list.size() > 15){
             scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else{
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
        List<String> disconnectedPlayers = new ArrayList<>();
        if (!tabList.containsAll(GameData.tabList)){
            Iterator<NetworkPlayerInfo> oldList = GameData.tabList.iterator();
            oldList.forEachRemaining(playerInfo -> {
                String playerName = playerInfo.getGameProfile().getName();
                if (!playerNames.contains(playerName)){
                    disconnectedPlayers.add(playerName);
                }
            });
        }
        GameData.secondPlaceLeft = disconnectedPlayers.contains(GameData.players[1]) && tabList.size() != 1;
        
        int possiblePoints;
        int roundsLeft = 16 - GameData.round; 
        if (GameData.isOnePointer && isNewTask){  //if this is executed on "Game ended," it needs to be counted as 3-pointer,
            possiblePoints = (roundsLeft*3)-2;    //in case the current round was a 1-pointer
        } else{
            possiblePoints = roundsLeft*3;
        }
        
        if (!GameData.isInParty || ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean()) {
            if (possiblePoints+GameData.score<40 && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean()
                    && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean()){
                Utils.sendChat("You could not get at least 40 points and were automatically requeued");
                requeue(true);
            } else if (possiblePoints+GameData.score>=40 && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean()){
                return; //Cancel subsequent requeue attempts
            } else if (possiblePoints+GameData.scores[1]<GameData.score){
                Utils.sendChat("You won and were automatically requeued");
                GameData.addSessionWin();
                requeue(true);
            } else if (possiblePoints+GameData.score<GameData.scores[0] && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean()){
                Utils.sendChat("You could not win and were automatically requeued");
                requeue(true);
            } else if (possiblePoints+GameData.scores[2]<GameData.score && GameData.secondPlaceLeft){
                Utils.sendChat("The player in 2nd place left, so you won and were automatically requeued");
                GameData.addSessionWin();
                requeue(true);
            }
        }
    }
    
    static void requeue(boolean subtractRound) {
        Utils.sendChat(String.format("After %s rounds:", GameData.round - (subtractRound? 1 : 0)));
        Utils.sendChat("\u00A7m                         ");
        for (int line = 8; line >= 6; line--){
            Utils.sendChat(getSidebarLines().get(line));
        }
        Utils.sendChat("\u00A7m                         ");
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
    }
}