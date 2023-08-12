package com.highpixelspeed.highpixelspeed.data;

import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameData {

    public static boolean inHypixelSays = false;
    public static boolean isInParty = false;
    public static int doPartyCheck = 0;
    public static boolean gameHasStarted = false;
    public static boolean isOnePointer = false;
    public static int round = 1;
    public static int score = 0;
    public static String[] players = new String[3]; //players and
    public static int[] scores = new int[3];        //their scores on the scoreboard
    public static int upForGrabs = 3; //number of points that will be awarded to the next player to finish the task (competitive tasks)
    public static boolean secondPlaceLeft = false;
    public static boolean doRoundCheck = false;
    public static Collection<NetworkPlayerInfo> tabList = new ArrayList<>();
    public static int chatsRemaining = 12; //Until sending the leaderboard in chat after game end
    public static boolean sendLeaderboardChat = false;
    public static HashMap<String, Integer> hsWins = new HashMap<>(); //Cache player win counts

    public static int sessionGamesPlayed = 0;
    public static int sessionWins = 0;
    public static int sessionPoints = 0;
    public static int sessionWinRoundPoints = 0;
    public static boolean addSessionGame = false;
    public static double joinYLevel;

    public static void addSessionWin() {
        sessionWins++;
        sessionWinRoundPoints += score;
    }

    //Reset scores
    public static void initializeGame(){
        sessionPoints += score;
        inHypixelSays = true;
        gameHasStarted = false;
        isInParty = false;
        joinYLevel = Minecraft.getMinecraft().thePlayer.posY; //Santa Says and Hypixel Says are at different Y levels for some reason
        round = 1;
        score = 0;
        players = new String[3];
        scores = new int[3];
        secondPlaceLeft = false;
        hsWins.clear();
        tabList.clear();
        chatsRemaining = 5;
        sendLeaderboardChat = false;
        Utils.sendChat("Joined Hypixel Says");
    }
}
