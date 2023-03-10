package com.highpixelspeed.highpixelspeed.data;

import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;

public class GameData {

    public static boolean inHypixelSays = false;
    public static boolean isInParty = false;
    public static int doPartyCheck = 0;
    public static boolean gameHasStarted = false;
    public static boolean isOnePointer = false;
    public static String apiKey;
    public static int round = 1;
    public static int score = 0;
    public static String[] players = new String[3]; //players and
    public static int[] scores = new int[3];        //their scores on the scoreboard
    public static int upForGrabs = 3; //number of points that will be awarded to the next player to finish the task (competitive tasks)
    public static boolean secondPlaceLeft = false;
    public static Collection<NetworkPlayerInfo> tabList = new ArrayList<>();
    public static int chatsRemaining = 11; //Until sending the leaderboard in chat after game end
    public static boolean sendLeaderboardChat = false;

    public static void reset(){
        inHypixelSays = true;
        isInParty = false;
        gameHasStarted = false;
        round = 1;
        score = 0;
        players = new String[3];
        scores = new int[3];
        secondPlaceLeft = false;
        tabList.clear();
        chatsRemaining = 11;
        sendLeaderboardChat = false;
    }
}
