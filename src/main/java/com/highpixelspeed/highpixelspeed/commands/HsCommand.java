package com.highpixelspeed.highpixelspeed.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.data.GameData;
import com.highpixelspeed.highpixelspeed.feature.Speedrun;
import com.highpixelspeed.highpixelspeed.utils.JoinWorld;
import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
// import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "hs";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/<command>";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!JoinWorld.ip.equals("hypixel.net")) return;

        if(args.length == 0) { //hs
            ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Enabled");
            if(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean()) {
                Utils.sendChat("Highpixel Speed mod enabled");
                Utils.sendChat("Use /hs help for options");
            } else {
                Utils.sendChat("Highpixel Speed mod disabled");
            }
            Utils.redrawSessionStats();

        } else if(args.length == 1){
            if(args[0].equalsIgnoreCase("help")) { //hs help
                Utils.sendChat("\u00A7m                                                                             ");
                Utils.sendChat("\u00A7e/hs help \u00A7bDisplay this message\n");
                Utils.sendChat(String.format("\u00A7%s/hs \u00A7bEnable the whole mod", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean()) ? "a" : "c"));
                // Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bQueue dodge players with a certain number of wins. Use \u00A7e/hs autododge help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs blacklist \u00A7bQueue dodge players whom you have blacklisted. Use \u00A7e/hs blacklist help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs empty \u00A7bRequeue if there aren't enough players to start", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs forty \u00A7bCancel requeuing if you can get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs fortyonly \u00A7bIf Forty Point Mode is true, requeue if you cannot get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs loss \u00A7bRequeue if you cannot win", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs party \u00A7bCancel requeuing if you are in a party", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs speedrun \u00A7bTime your games for speedrunning. Use \u00A7e/hs speedrun help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean()) ? "a" : "c"));
                Utils.sendChat(String.format("\u00A7%s/hs stats \u00A7bShow summary of stats during current play session. Use \u00A7e/hs stats help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Enabled").getBoolean()) ? "a" : "c"));
                // Utils.sendChat(String.format("\u00A7%s/hs tagwins \u00A7bShow Hypixel Says win count above players' heads", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Tag Wins").getBoolean()) ? "a" : "c"));
                Utils.sendChat("\u00A7e/hs play \u00A7bJoin Hypixel Says");
                Utils.sendChat("\u00A7m                                                                             ");
            }

            /*else if(args[0].equalsIgnoreCase("autododge")) { //hs autododge
                ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) ? "Auto dodge is enabled" : "Auto dodge is disabled");
                if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) {
                    Utils.sendChat("Auto dodge wins threshold: \u00A7e" + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt());
                }
            }*/else if(args[0].equalsIgnoreCase("blacklist")) { //hs blacklist
                ConfigHandler.toggle(ConfigHandler.CATEGORY_BLACKLIST, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean()) ? "Blacklist is enabled" : "Blacklist is disabled");
            } else if(args[0].equalsIgnoreCase("empty")) { //hs empty
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Leave Empty Queue");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean()) ? "Leave empty queues is enabled" : "Leave empty queues is disabled");
            } else if(args[0].equalsIgnoreCase("forty")) { //hs forty
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Mode");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean()) ? "40-point mode is enabled" : "40-point mode is disabled");
            } else if(args[0].equalsIgnoreCase("fortyonly")) { //hs fortyonly
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Only");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean()) ? "40-point only mode is enabled" +
                        ((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean()) ? "" : ", but Forty Point Mode is disabled") : "40-point only mode is disabled");
            } else if(args[0].equalsIgnoreCase("loss")) { //hs loss
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Queue On Loss");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean()) ? "Requeuing on loss is enabled" : "Requeuing on loss is disabled");
            } else if(args[0].equalsIgnoreCase("party")) { //hs party
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Queue With Party");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean()) ? "Requeuing with a party is enabled" : "Requeuing with a party is disabled");
            } else if(args[0].equalsIgnoreCase("play")) { //hs play
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            } else if(args[0].equalsIgnoreCase("speedrun")) { //hs speedrun
                ConfigHandler.toggle(ConfigHandler.CATEGORY_SPEEDRUN, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean()) ? "Speedrun mode is enabled" : "Speedrun mode is disabled");
            } else if(args[0].equalsIgnoreCase("stats")) { //hs stats
                ConfigHandler.toggle(ConfigHandler.CATEGORY_STATS, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Enabled").getBoolean()) ? "Session Stats are enabled" : "Session Stats are disabled");
                Utils.redrawSessionStats();
            } /*else if(args[0].equalsIgnoreCase("tagwins")) { //hs tagwins
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Tag Wins");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Tag Wins").getBoolean()) ? "Tagging Wins is enabled" : "Tagging Wins is disabled");
                for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) Utils.tagWins(player);
            }*/

        } else if(args.length == 2){
            /*if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_AUTODODGE)) {
                if(args[1].equalsIgnoreCase("help")) { //hs autododge help
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs autododge help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bEnable Auto Dodge", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) ? "a" : "c"));
                    Utils.sendChat("\u00A7e/hs autododge <wins> \u00A7bSet the number of wins the player must have");
                    Utils.sendChat(String.format("Threshold is currently set to \u00A7e%s", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt()));
                    Utils.sendChat("\u00A7m                                                                             ");
                } else { //hs autododge <wins>
                    try {
                        ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").set(Integer.parseInt(args[1]));
                        Utils.sendChat("Auto dodge wins threshold set to \u00A7e" + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt());
                        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) Utils.tagWins(player);
                    } catch (NumberFormatException e) {
                        Utils.sendChat("Please enter a valid integer");
                    }
                }
            } else */if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_BLACKLIST)) {
                if(args[1].equalsIgnoreCase("help")) { //hs blacklist help
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs blacklist help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs blacklist \u00A7bEnable Blacklist", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean()) ? "a" : "c"));
                    Utils.sendChat("\u00A7e/hs blacklist add <username> [username] ... \u00A7bAdd player(s) to your blacklist (limit 10)");
                    Utils.sendChat("\u00A7e/hs blacklist list \u00A7bDisplay all blacklisted players");
                    Utils.sendChat("\u00A7e/hs blacklist remove <username> [username] ... \u00A7bRemove player(s) from your blacklist");
                    Utils.sendChat("\u00A7m                                                                             ");
                } else if(args[1].equalsIgnoreCase("list")) { //hs blacklist list
                    Utils.sendChat("Blacklisted players:\n\u00A7b" + String.join(", ", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList()));
                }
            } else if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_SPEEDRUN)) {
                if (args[1].equalsIgnoreCase("help")) { //hs speedrun help
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs speedrun help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs speedrun \u00A7bEnable Speedrun mode", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean()) ? "a" : "c"));
                    Utils.sendChat(String.format("\u00A7%s/hs speedrun game \u00A7bSet level mode to time the entire game", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Full Game") ? "a" : "c")));
                    Utils.sendChat(String.format("\u00A7%s/hs speedrun round \u00A7bSet level mode to time each individual round", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds")) ? "a" : "c"));
                    Utils.sendChat(String.format("\u00A7%s/hs speedrun all \u00A7bIf level mode is set to Individual Rounds, time every round, not only the speedrun category rounds", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Time All Rounds").getBoolean()) ? "a" : "c"));
                    Utils.sendChat("\u00A7e/hs speedrun name \u00A7bDisplay your username set for speedrun.com");
                    Utils.sendChat("\u00A7e/hs speedrun name <username> \u00A7bEnter your speedrun.com username if you have any posted Hypixel Says runs");
                    Utils.sendChat(String.format("\u00A7%s/hs speedrun requeue \u00A7bRequeue when your run time has surpassed your personal best", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Requeue Slow Run").getBoolean()) ? "a" : "c"));
                    Utils.sendChat("\u00A7e/hs speedrun stats \u00A7bDisplay your personal best for each speedrun category");
                    Utils.sendChat("\u00A7e/hs speedrun reset \u00A7bPermanently erase your speedrun personal best stats");
                    Utils.sendChat("\u00A7m                                                                             ");
                } else if (args[1].equalsIgnoreCase("game")) { //hs speedrun game
                    Utils.sendChat(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Full Game") ? "Level mode is already set to Full Game" : "Level mode set to Full Game");
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").set("Full Game");
                } else if (args[1].equalsIgnoreCase("round")) { //hs speedrun round
                    Utils.sendChat(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds") ? "Level mode is already set to Individual Rounds" : "Level mode set to Individual Rounds");
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").set("Individual Rounds");
                    if (GameData.inHypixelSays) Speedrun.color = null;
                } else if (args[1].equalsIgnoreCase("all")) { //hs speedrun all
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_SPEEDRUN, "Time All Rounds");
                    Utils.sendChat(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Time All Rounds").getBoolean() ? "Timing all rounds is enabled" : "Only speedrun category rounds will be timed");
                } else if (args[1].equalsIgnoreCase("name")) { //hs speedrun name
                    Utils.sendChat("Your speedrun.com username is " + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").getString());
                } else if (args[1].equalsIgnoreCase("stats")) { //hs speedrun stats
                    boolean sentMessage = false;
                    Utils.sendChat("Your speedrun personal bests:");
                    for (String category : Arrays.asList("Win", "Complete%", "Anvil", "Iron Golem", "Parkour", "Jump off Platform", "Pig off Platform", "Full Inventory", "Get to Full Hunger", "Shear the Sheep", "Brew a Potion", "Repair an Item", "Enchant an Item", "Put a Music Disc in the Jukebox", "Feed the Sheep", "Jump onto the Platform")) {
                        try {
                            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("\u00A7%s%s \u00A7b%s",
                                    Speedrun.publicPersonalBests.get(category) == 0 ? "e" : "a",
                                    Utils.formatTime(Speedrun.getPersonalBest(category)),
                                    category))
                                    .setChatStyle(new ChatStyle()
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("\u00A76" + Utils.formatTime(Speedrun.worldRecords.get(category)))))));
                            sentMessage = true;
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                    if (!sentMessage) Utils.sendChat("don't exist :(");
                } else if (args[1].equalsIgnoreCase("requeue")) { //hs speedrun requeue
                    ConfigHandler.toggle(ConfigHandler.CATEGORY_SPEEDRUN, "Requeue Slow Run");
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Requeue Slow Run").getBoolean()) ? "Requeueing slow runs is enabled" : "Requeueing slow runs is disabled");
                } else if (args[1].equalsIgnoreCase("reset")) { //hs speedrun reset
                    Utils.sendChat("Are you sure you want to permanently erase your speedrun personal best stats?");
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7a\u00A7l[YES]")
                            .setChatStyle(new ChatStyle()
                                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Reset personal best stats")))
                                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hs speedrun resetconfirm"))));
                } else if (args[1].equalsIgnoreCase("resetconfirm")) {
                    for (String category : Speedrun.publicPersonalBests.keySet()) {
                        ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(category).set(0);
                    }
                    Utils.sendChat("All personal best stats erased");
                }
            } else if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_STATS)) {
                if(args[1].equalsIgnoreCase("help")) { //hs stats help
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs stats help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs stats \u00A7bEnable session stats display", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Enabled").getBoolean()) ? "a" : "c"));
                    Utils.sendChat("\u00A7e/hs stats reset \u00A7bReset your session stats");
                    Utils.sendChat("\u00A7e/hs stats save \u00A7bSave your current session stats for later");
                    Utils.sendChat("\u00A7e/hs stats load \u00A7bLoad your saved session stats");
                    Utils.sendChat("\u00A7m                                                                             ");
                } else if(args[1].equalsIgnoreCase("reset")) { //hs stats reset
                    Utils.sendChat("Are you sure you want to erase your current session stats?");

                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7a\u00A7l[YES]")
                            .setChatStyle(new ChatStyle()
                                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Reset session stats")))
                                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hs stats resetconfirm"))));
                } else if(args[1].equalsIgnoreCase("save")) { //hs stats save
                    Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").getInt() == 0
                            ? "" : String.format("You already have a session saved from %s. Those stats will be overridden. ",
                            Utils.formatDateTime(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").getInt()))) +
                            "Are you sure you want to save your current session stats?");

                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7a\u00A7l[YES]")
                            .setChatStyle(new ChatStyle()
                                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Save session stats")))
                                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hs stats saveconfirm"))));
                } else if(args[1].equalsIgnoreCase("load")) { //hs stats load
                    if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").getInt() == 0) {
                        Utils.sendChat("You do not have a session saved");
                    } else {
                        Utils.sendChat((GameData.sessionGamesPlayed == 0 ? "" : "You already have stats from your current session. Those stats will be overridden. ") +
                                String.format("Are you sure you want to load your saved session stats from %s?",
                                        Utils.formatDateTime(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").getInt())));

                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00A7a\u00A7l[YES]")
                                .setChatStyle(new ChatStyle()
                                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Load session stats")))
                                        .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hs stats loadconfirm")))
                                .appendSibling(new ChatComponentText(" "))
                                .appendSibling(new ChatComponentText("\u00A72\u00A7l[COMBINE]")
                                        .setChatStyle(new ChatStyle()
                                                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Load session stats and combine with current session stats")))
                                                .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hs stats combineconfirm")))));
                    }
                } else if (args[1].equalsIgnoreCase("resetconfirm")) {
                    GameData.sessionGamesPlayed = 0;
                    GameData.sessionWins = 0;
                    GameData.sessionPoints = 0;
                    GameData.sessionWinRoundPoints = 0;

                    Utils.sendChat("All session stats reset to \u00A7e0");
                    Utils.redrawSessionStats();
                } else if (args[1].equalsIgnoreCase("saveconfirm")) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Games Played").set(GameData.sessionGamesPlayed);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Wins").set(GameData.sessionWins);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Points").set(GameData.sessionPoints);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Win Round Points").set(GameData.sessionWinRoundPoints);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").set((int) Instant.now().getEpochSecond());

                    GameData.sessionGamesPlayed = 0;
                    GameData.sessionWins = 0;
                    GameData.sessionPoints = 0;
                    GameData.sessionWinRoundPoints = 0;

                    Utils.sendChat("Session stats saved");
                    Utils.redrawSessionStats();
                } else if (args[1].equalsIgnoreCase("loadconfirm")) {
                    GameData.sessionGamesPlayed = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Games Played").getInt();
                    GameData.sessionWins = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Wins").getInt();
                    GameData.sessionPoints = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Points").getInt();
                    GameData.sessionWinRoundPoints = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Win Round Points").getInt();

                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Games Played").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Wins").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Points").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Win Round Points").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").set(0);

                    Utils.sendChat("Session stats loaded");
                    Utils.redrawSessionStats();
                } else if (args[1].equalsIgnoreCase("combineconfirm")) {
                    GameData.sessionGamesPlayed += ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Games Played").getInt();
                    GameData.sessionWins += ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Wins").getInt();
                    GameData.sessionPoints += ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Points").getInt();
                    GameData.sessionWinRoundPoints += ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Win Round Points").getInt();

                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Games Played").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Wins").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Points").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Win Round Points").set(0);
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_STATS).get("Save Date").set(0);

                    Utils.sendChat("Session stats combined with saved session stats");
                    Utils.redrawSessionStats();
                }
            }
        } else {
            if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_BLACKLIST)) {
                if(args[1].equalsIgnoreCase("add")){ //hs blacklist add <username> [username]

                    JsonArray payload = new JsonArray();
                    for (int i = 2; i < args.length; i++) {
                        payload.add( new JsonPrimitive(args[i]));
                    }
                    List<String> addList = new ArrayList<>();
                    for (JsonElement jsonElement : (JsonArray) Utils.httpPOST("https://api.mojang.com/profiles/minecraft", payload)) {
                        if (Arrays.stream(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList()).noneMatch(jsonElement.toString()::equalsIgnoreCase)) {
                            ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.append(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList(), jsonElement.toString()));
                            addList.add(jsonElement.getAsJsonObject().getAsJsonPrimitive("name").toString());
                        }
                    }
                    ConfigHandler.reloadBlacklist();
                    if (!addList.isEmpty()) {
                        Utils.sendChat(String.format("%s%s%s added to your blacklist", (addList.size() > 1) ? "Players " : "", String.join(", ", addList).replace("\"", ""), (addList.size() > 1) ? " were" : " was"));
                    }

                } else if(args[1].equalsIgnoreCase("remove")) { //hs blacklist remove <username> [username]
                    List<String> removeList = new ArrayList<>();
                    for (int i = 2; i < args.length; i++) {
                        String[] uuids = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList();
                        for (String uuid : uuids) {
                            if (StringUtils.containsIgnoreCase(uuid,"\"" + args[i] + "\"")) {
                                removeList.add(new JsonParser().parse(uuid).getAsJsonObject().getAsJsonPrimitive("name").getAsString());
                                ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.remove(uuids, uuid));
                            }
                        }
                    }
                    ConfigHandler.reloadBlacklist();
                    if (!removeList.isEmpty()) {
                        Utils.sendChat(String.format("%s%s%s removed from your blacklist", (removeList.size() > 1) ? "Players " : "", String.join(", ", removeList).replace("\"", ""), (removeList.size() > 1) ? " were" : " was"));
                    }
                }
            } else if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_SPEEDRUN)) {
                if(args[1].equalsIgnoreCase("name")) { //hs speedrun name <username>
                    Utils.asyncHttpGet("www.speedrun.com", "api/v1/users/" + args[2], response -> {
                        if (response.has("data")) {
                            ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").set(response
                                    .getAsJsonObject("data")
                                    .getAsJsonObject("names")
                                    .getAsJsonPrimitive("international")
                                    .getAsString());
                            Utils.sendChat("Your speedrun.com username has been set to " + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").getString());
                            ConfigHandler.config.save();
                            Speedrun.resetPersonalBests();
                            Speedrun.updatePersonalBests();
                        } else Utils.sendChat(String.format("The username %s does not exist on speedrun.com", args[2]));
                    });
                }
            }
        }

        ConfigHandler.config.save();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        Collection<NetworkPlayerInfo> tabList = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
        ArrayList<String> playerNames = new ArrayList<String>() {{ tabList.iterator().forEachRemaining(playerInfo -> add(playerInfo.getGameProfile().getName())); }};

        if(args.length == 1){
             return matchingArgs(args, new ArrayList<>(Arrays.asList("help", /*"autododge",*/ "blacklist", "empty", "forty", "fortyonly", "loss", "play", "party", "speedrun", "stats"/*, "tagwins"*/)));
        } else if(args.length == 2) {
            /*if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_AUTODODGE)) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "2500", "10000")));
            } else*/ if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_BLACKLIST)) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "add", "list", "remove")));
            } else if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_SPEEDRUN)) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "game", "round", "all", "name", "requeue", "stats", "reset")));
            } else if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_STATS)) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "reset", "save", "load")));
            }
        } else {
            if(args[0].equalsIgnoreCase(ConfigHandler.CATEGORY_BLACKLIST)) {
                if (args[1].equalsIgnoreCase("add")) {
                    return matchingArgs(args, playerNames);
                } else if(args[1].equalsIgnoreCase("remove")) {
                    return matchingArgs(args, new ArrayList<>(Arrays.asList(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList())));
                }
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<String> matchingArgs(String[] args, ArrayList<String> options) {
        options.removeIf(i -> !i.toLowerCase().startsWith(args[args.length-1].toLowerCase()));
        return options;
    }
}
