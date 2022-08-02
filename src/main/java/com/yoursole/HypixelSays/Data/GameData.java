package com.yoursole.HypixelSays.Data;

import java.util.*;
import net.minecraft.client.network.NetworkPlayerInfo;

public class GameData {

    public static boolean tellraw = false;
    public static boolean inHypixelSays = false;
    public static boolean gameHasStarted = false;
    public static boolean isOnePointer = false;
    public static boolean gameEnded = false;

    public static int round = 1;
    public static int score = 0;
    public static String[] players = new String[3]; //players and
    public static int[] scores = new int[3];        //their scores on the scoreboard
    public static int upForGrabs = 3; //number of points that will be awarded to the next player to finish the task (competitive tasks)
    public static boolean secondPlaceLeft = false;
    public static Collection<NetworkPlayerInfo> tabList = new ArrayList<NetworkPlayerInfo>();

    public static void reset(){
        gameHasStarted = false;
        gameEnded = false;
        round = 1;
        players = new String[3];
        scores = new int[3];
        secondPlaceLeft = false;
        tabList.clear();
    }
}
