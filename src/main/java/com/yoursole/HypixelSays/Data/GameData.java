package com.yoursole.HypixelSays.Data;

public class GameData {
    public static boolean isEnabled = false;
    public static boolean tellraw = false;
    public static boolean lagMode = false;
    public static boolean queueOnLoss = true;
    public static boolean inHypixelSays = false;
    public static boolean isOnePointer = false;
    public static boolean fortyPointGame = false;

    public static int round = 1;
    public static int score = 0;
    public static String[] players = new String[3]; //players and
    public static int[] scores = new int[3];      //their scores on the scoreboard
    public static int upForGrabs = 3; //number of points that will be awarded to the next player to finish the task (competitive tasks)
    public static boolean secondPlaceLeft = false;
    public static String disconnectedPlayer = "";

    public static void reset(){
        round = 1;
        score = 0;
        players = new String[3];
        scores = new int[3];
        secondPlaceLeft = false;
        disconnectedPlayer = "";
    }
}
