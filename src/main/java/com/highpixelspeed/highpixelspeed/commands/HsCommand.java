package com.highpixelspeed.highpixelspeed.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.data.GameData;
import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;

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

        if(args.length == 0) {
            ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Enabled");
            if(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean()) {
                Utils.sendChat("Highpixel Speed mod enabled");
                Utils.sendChat("Use /hs help for options");
            } else {
                Utils.sendChat("Highpixel Speed mod disabled");
            }

        } else if(args.length == 1){
            if(args[0].equalsIgnoreCase("help")) {
                Utils.sendChat("\u00A7m                                                                             ");
                Utils.sendChat("\u00A7e/hs help \u00A7bDisplay this message\n");
                Utils.sendChat(String.format("\u00A7%s/hs \u00A7bEnable the whole mod", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bQueue dodge players with a certain number of wins. Use \u00A7e/hs autododge help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs blacklist \u00A7bQueue dodge players whom you have blacklisted. Use \u00A7e/hs blacklist help\u00A7b for options", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs empty \u00A7bRequeue if there aren't enough players to start", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs forty \u00A7bCancel requeuing if you can get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs fortyonly \u00A7bIf Forty Point Mode is true, requeue if you cannot get 40 points", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs key \u00A7bDisplay Hypixel API key mode. Use \u00A7e/hs key help\u00A7b for options", (Utils.isValidHypixelAPIKey(GameData.apiKey))?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs loss \u00A7bToggle requeuing if you cannot win", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean())?"a":"c"));
                Utils.sendChat(String.format("\u00A7%s/hs party \u00A7bToggle requeuing if you are in a party", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean())?"a":"c"));
                Utils.sendChat("\u00A7e/hs play \u00A7bJoin Hypixel Says");
                Utils.sendChat("\u00A7m                                                                             ");
            }

            else if(args[0].equalsIgnoreCase("autododge")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_AUTODODGE, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"Auto dodge is enabled":"Auto dodge is disabled");
                if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean()) {
                    Utils.sendChat("Auto dodge wins threshold: \u00A7e" + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt());
                    if (GameData.apiKey == null) {
                        Utils.sendChat(String.format("\n\u00A7bHypixel API Key Mode is set to Manual, but %s",
                                (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Hypixel API Key").getString().equals(""))? "there is no API key set. " : "the key is invalid. ") +
                                "Please set to automatic (be careful), enter your key in the config or with \u00A7e/hs key <key>\u00A7b, or disable Autododge");
                    }
                }
            }else if(args[0].equalsIgnoreCase("blacklist")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_BLACKLIST, "Enabled");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean())?"Blacklist is enabled":"Blacklist is disabled");
            } else if(args[0].equalsIgnoreCase("empty")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Leave Empty Queue");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Leave Empty Queue").getBoolean())?"Leave empty queues is enabled":"Leave empty queues is disabled");
            } else if(args[0].equalsIgnoreCase("forty")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Mode");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"40-point mode is enabled":"40-point mode is disabled");
            } else if(args[0].equalsIgnoreCase("fortyonly")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Forty Point Only");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Only").getBoolean())?"40-point only mode is enabled" +
                        ((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Forty Point Mode").getBoolean())?"":", but Forty Point Mode is disabled"):"40-point only mode is disabled");
            } else if(args[0].equalsIgnoreCase("key")) {
                Utils.sendChat("Hypixel API Key Mode is set to " + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Hypixel API Key Mode").getString());
                if (!Utils.isValidHypixelAPIKey(GameData.apiKey)) {
                    Utils.sendChat("Use \u00A7e/hs key help \u00A7bfor options");
                }
            } else if(args[0].equalsIgnoreCase("loss")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Queue On Loss");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue On Loss").getBoolean())?"Requeuing on loss is enabled":"Requeuing on loss is disabled");
            } else if(args[0].equalsIgnoreCase("party")) {
                ConfigHandler.toggle(ConfigHandler.CATEGORY_GENERAL, "Queue With Party");
                Utils.sendChat((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean())?"Requeuing with a party is enabled":"Requeuing with a party is disabled");
            } else if(args[0].equalsIgnoreCase("play")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }

        } else if(args.length == 2){
            if(args[0].equalsIgnoreCase("autododge")) {
                if(args[1].equalsIgnoreCase("help")) {
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs autododge help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs autododge \u00A7bEnable auto dodge", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Enabled").getBoolean())?"a":"c"));
                    Utils.sendChat("\u00A7e/hs autododge <wins> \u00A7bSet the number of wins the player must have");
                    Utils.sendChat(String.format("Threshold is currently set to \u00A7e%s", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt()));
                    Utils.sendChat("\u00A7m                                                                             ");
                } else {
                    try {
                        ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").set(Integer.parseInt(args[1]));
                        Utils.sendChat("Auto dodge wins threshold set to \u00A7e" + ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_AUTODODGE).get("Wins Threshold").getInt());
                    } catch (NumberFormatException e) {
                        Utils.sendChat("Please enter a valid integer");
                    }
                }
            } else if(args[0].equalsIgnoreCase("blacklist")) {
                if(args[1].equalsIgnoreCase("help")) {
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs blacklist help \u00A7bDisplay this message\n");
                    Utils.sendChat(String.format("\u00A7%s/hs blacklist \u00A7bEnable blacklist", (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Enabled").getBoolean())?"a":"c"));
                    Utils.sendChat("\u00A7e/hs blacklist add <username> [username] ... \u00A7bAdd player(s) to your blacklist (limit 10)");
                    Utils.sendChat("\u00A7e/hs blacklist list \u00A7bDisplay all blacklisted players");
                    Utils.sendChat("\u00A7e/hs blacklist remove <username> [username] ... \u00A7bRemove player(s) from your blacklist");
                    Utils.sendChat("\u00A7m                                                                             ");
                } else if(args[1].equalsIgnoreCase("list")) {
                    Utils.sendChat("Blacklisted players:\n\u00A7b" + String.join(", ", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted Players").getStringList()));
                }
            } else if(args[0].equalsIgnoreCase("key")) {
                if (args[1].equalsIgnoreCase("help")) {
                    Utils.sendChat("\u00A7m                                                                             ");
                    Utils.sendChat("\u00A7e/hs key help \u00A7bDisplay this message\n");
                    Utils.sendChat("\u00A7e/hs key automatic \u00A7bAutomatically handle your API key. This will reset your key every time you restart the game. If you use your API key for something else, use manual mode");
                    Utils.sendChat("\u00A7e/hs key manual \u00A7bUse your saved API key. Use \u00A7e/hs key <key>");
                    Utils.sendChat("\u00A7e/hs key <key> \u00A7bSet your Hypixel API key. Be aware that it will be saved on your computer");
                    Utils.sendChat("\u00A7m                                                                             ");
                } else if (args[1].equalsIgnoreCase("manual")) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Hypixel API Key Mode").set("Manual");
                    Utils.sendChat("Hypixel API key mode set to Manual");
                } else if (args[1].equalsIgnoreCase("automatic")) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Hypixel API Key Mode").set("Automatic");
                    Utils.sendChat("Hypixel API key mode set to Automatic");
                } else if (Utils.isValidHypixelAPIKey(args[1])) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Hypixel API Key").set(args[1]);
                    Utils.sendChat("Hypixel API key successfully changed");
                }
            }
        } else {
            if(args[0].equalsIgnoreCase("blacklist")) {
                if(args[1].equalsIgnoreCase("add")){

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
                    if (addList.size() > 0) {
                        Utils.sendChat(String.format("%s%s%s added to your blacklist", (addList.size() > 1)?"Players ":"", String.join(", ", addList).replace("\"", ""), (addList.size() > 1)?" were":" was"));
                    }

                } else if(args[1].equalsIgnoreCase("remove")) {
                    List<String> removeList = new ArrayList<>();
                    for (int i = 2; i < args.length; i++) {
                        String[] uuids = ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").getStringList();
                        for (String uuid : uuids) {
                            if (StringUtils.containsIgnoreCase(uuid,"\"" + args[i] + "\"")) {
                                removeList.add(new JsonParser().parse(uuid).getAsJsonObject().getAsJsonPrimitive("name").toString().replace("\"", ""));
                                ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_BLACKLIST).get("Blacklisted UUIDs").set(Utils.remove(uuids, uuid));
                            }
                        }
                    }
                    ConfigHandler.reloadBlacklist();
                    if (removeList.size() > 0) {
                        Utils.sendChat(String.format("%s%s%s removed from your blacklist", (removeList.size() > 1)?"Players ":"", String.join(", ", removeList).replace("\"", ""), (removeList.size() > 1)?" were":" was"));
                    }
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
             return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "autododge", "blacklist", "empty", "forty", "fortyonly", "key", "loss", "play", "party")));
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("autododge")) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "2500", "10000")));
            } else if(args[0].equalsIgnoreCase("blacklist")) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "add", "list", "remove")));
            } else if(args[0].equalsIgnoreCase("key")) {
                return matchingArgs(args, new ArrayList<>(Arrays.asList("help", "automatic", "manual")));
            }
        } else {
            if(args[0].equalsIgnoreCase("blacklist")) {
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
