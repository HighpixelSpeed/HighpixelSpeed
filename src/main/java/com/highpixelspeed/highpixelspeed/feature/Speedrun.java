package com.highpixelspeed.highpixelspeed.feature;

import com.google.gson.JsonElement;
import com.highpixelspeed.highpixelspeed.config.ConfigHandler;
import com.highpixelspeed.highpixelspeed.data.GameData;
import com.highpixelspeed.highpixelspeed.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Speedrun {

    public static long gameStartedTime = 0L;
    public static boolean gameInProgress = false;
    public static long inGameDuration = 0L;
    public static boolean isWinEligible; // Player is not in party and the game has always had at least seven players
    public static boolean isFirstPlaceFinish = false;
    public static ArrayList<String> partyMembers = new ArrayList<>();

    public enum Round {
        ANVIL("Anvil"),
        IRON_GOLEM("Iron Golem"),
        PARKOUR("Parkour"),
        JUMP("Jump off Platform"),
        PIG("Pig off Platform"),
        INVENTORY("Full Inventory"),
        OTHER(null);


        private final String readableName;
        Round(String readableName) {
            this.readableName = readableName;
        }

        public String getReadableName() {
            return readableName;
        }


    }
    static long roundStartedTime = 0L;
    static Round roundInProgress = null;
    static Round lastRound = Round.OTHER;
    public static long inRoundDuration = 0L;
    public static long time;

    public static String color;
    public static float scale;
    static String recordCategory = "";
    static int chatsRemaining = 0; // Until sending the new record in chat after game end
    public static HashMap<String, Long> publicPersonalBests = new HashMap<String, Long>() {{
        put("Win", 0L);
        put("Complete%", 0L);
        put("Anvil", 0L);
        put("Iron Golem", 0L);
        put("Parkour", 0L);
        put("Jump off Platform", 0L);
        put("Pig off Platform", 0L);
        put("Full Inventory", 0L);
    }};
    public static HashMap<String, Long> worldRecords = new HashMap<String, Long>() {{
        put("Win", 0L);
        put("Complete%", 0L);
        put("Anvil", 0L);
        put("Iron Golem", 0L);
        put("Parkour", 0L);
        put("Jump off Platform", 0L);
        put("Pig off Platform", 0L);
        put("Full Inventory", 0L);
    }};
    static final HashMap<String, String> levelIDs = new HashMap<String, String>() {{
        put("5wke0mpw", "Anvil");
        put("592rm63d", "Iron Golem");
        put("29v0g8lw", "Parkour");
        put("xd4ep5rw", "Jump off Platform");
        put("xd0npe49", "Pig off Platform");
        put("rw6g27g9", "Full Inventory");
    }};

    @SubscribeEvent
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if (gameInProgress) inGameDuration = System.currentTimeMillis() - gameStartedTime;
        if (roundInProgress != null) inRoundDuration = System.currentTimeMillis() - roundStartedTime;
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean() && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean() && GameData.gameHasStarted) {
            if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Full Game")) {
                time = inGameDuration;
                String category = isWinEligible ? "Win" : "Complete%";
                long personalBest = Long.MAX_VALUE; // You can beat any record if it doesn't exist
                try {
                    personalBest = getPersonalBest(category);
                } catch (IllegalArgumentException ignored) {} // No personal best has been set
                color = inGameDuration <= personalBest
                        ? inGameDuration <= worldRecords.get(category)
                                ? gameInProgress ? "a" : "6" // Run is faster than world record
                                : gameInProgress
                                        ? "e" // Game in progress
                                        : (category.equals("Win") && isFirstPlaceFinish) || category.equals("Complete%") ? "2" : "e" // Run is faster than personal best
                        : "c"; // Run is slower than personal best
                try {
                    scale = (float) inGameDuration / getPersonalBest(category);
                } catch (IllegalArgumentException e) { // No personal best has been set
                    scale = (float) inGameDuration / worldRecords.get(category);
                }
            } else if (roundStartedTime > 0L && (!lastRound.equals(Round.OTHER) || ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Time All Rounds").getBoolean())) {
                time = inRoundDuration;
                color = null;
                if (lastRound != null) {
                    if (lastRound.equals(Round.OTHER)) {
                        color = "e";
                    } else {
                        long personalBest = Long.MAX_VALUE; // You can beat any record if it doesn't exist
                        try {
                            personalBest = getPersonalBest(lastRound.getReadableName());
                        } catch (IllegalArgumentException ignored) {} // No personal best has been set
                        color = inRoundDuration <= personalBest
                                ? inRoundDuration <= worldRecords.get(lastRound.getReadableName())
                                        ? roundInProgress != null ? "a" : "6" // Round is faster than world record
                                        : roundInProgress != null ? "e" : "2" // Round is slower than world record
                                : "c"; // Round is slower than personal best
                    }
                    try {
                        scale = lastRound.equals(Round.OTHER)? 0f : (float) inRoundDuration / getPersonalBest(lastRound.getReadableName());
                    } catch (IllegalArgumentException e) { // No personal best has been set
                        scale = (float) inRoundDuration / worldRecords.get(lastRound.getReadableName());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceivedEvent(ClientChatReceivedEvent event){
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Enabled").getBoolean() && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean() && GameData.gameHasStarted) {
            String message = StringUtils.stripControlCodes(event.message.getFormattedText());

            if (chatsRemaining > 0) {
                chatsRemaining--;
                if (chatsRemaining == 0) {
                    if (inGameDuration < worldRecords.get(recordCategory)) {
                        worldRecords.put(recordCategory, inGameDuration);
                        Utils.sendChat(String.format("You broke the world record for the \u00A7e%s \u00A7bcategory, with a time of \u00A76%s%s",
                                recordCategory,
                                Utils.formatTime(inGameDuration),
                                partyMembers.isEmpty() ? "" : "\u00A7b and with \u00A7e" + String.join("\u00A7b, \u00A7e", partyMembers) + "\u00A7b in your party"));
                    } else Utils.sendChat(String.format("You broke your record for the \u00A7e%s \u00A7bcategory, with a time of \u00A7e%s%s",
                            recordCategory,
                            Utils.formatTime(inGameDuration),
                            partyMembers.isEmpty() ? "" : "\u00A7b and with \u00A7e" + String.join("\u00A7b, \u00A7e", partyMembers) + "\u00A7b in your party"));
                    recordCategory = "";
                }
            }

            if (message.contains("1st Place - ") && !message.contains(":") && ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Full Game")) {
                // End game run
                inGameDuration = System.currentTimeMillis() - gameStartedTime;
                if ((ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Complete%").getInt() == 0 || inGameDuration < ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Complete%").getInt())
                        && (publicPersonalBests.get("Complete%") == 0L || inGameDuration < publicPersonalBests.get("Complete%"))) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Complete%").set((int) inGameDuration);
                    recordCategory = "Complete%";
                    chatsRemaining = 5;
                }
                if (message.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) && isWinEligible
                        && (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Win").getInt() == 0 || inGameDuration < ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Win").getInt())
                        && (publicPersonalBests.get("Win") == 0L || inGameDuration < publicPersonalBests.get("Win"))) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Win").set((int) inGameDuration);
                    isFirstPlaceFinish = true;
                    recordCategory = "Win";
                    chatsRemaining = 5;
                }
                gameInProgress = false;
                gameStartedTime = 0L;
                ConfigHandler.config.save();
            } else if (message.startsWith("NEXT TASK")) {
                color = null;
                if (1 < Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size() && Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size() < 7) {
                    isWinEligible = false;
                }
                if (getSpeedrunRound(message) != null && (!Objects.equals(getSpeedrunRound(message), Round.OTHER) || ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Time All Rounds").getBoolean())) {
                    // Start round run
                    roundStartedTime = System.currentTimeMillis();
                    roundInProgress = getSpeedrunRound(message);
                    if (roundInProgress != null) lastRound = roundInProgress;
                    if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds")) color = "e";
                }
            } else if (message.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) && message.contains("finished") && !message.contains(":")) {
                // End round run
                inRoundDuration = System.currentTimeMillis() - roundStartedTime;
                if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds") && roundInProgress != null && roundInProgress != Round.OTHER
                        && (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(roundInProgress.getReadableName()).getInt() == 0 || inRoundDuration < ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(roundInProgress.getReadableName()).getInt())
                        && (publicPersonalBests.get(roundInProgress.getReadableName()) == 0L || inRoundDuration < publicPersonalBests.get(roundInProgress.getReadableName()))) {
                    ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(roundInProgress.getReadableName()).set((int) inRoundDuration);
                    if (inRoundDuration < worldRecords.get(roundInProgress.getReadableName())) {
                        worldRecords.put(roundInProgress.getReadableName(), inRoundDuration);
                        Utils.sendChat(String.format("You broke the world record for the \u00A7e%s \u00A7bround, with a time of \u00A76%s", roundInProgress.getReadableName(), Utils.formatTime(inRoundDuration)));
                    } else Utils.sendChat(String.format("You broke your record for the \u00A7e%s \u00A7bround, with a time of \u00A7e%s", roundInProgress.getReadableName(), Utils.formatTime(inRoundDuration)));
                    ConfigHandler.config.save();
                }
                roundInProgress = null;
                time = inRoundDuration;
            } else if (message.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) && message.contains("failed") && !message.contains(":")) {
                // End round run if failed
                inRoundDuration = System.currentTimeMillis() - roundStartedTime;
                roundStartedTime = 0L;
                time = inRoundDuration;
                if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds") && (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Time All Rounds").getBoolean() || (roundInProgress != null && roundInProgress != Round.OTHER))) color = "c";
                roundInProgress = null;
            } else if (message.contains("Game ended") && !message.contains(":")) {
                // Clear timer
                roundInProgress = null;
                roundStartedTime = 0L;
                if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Level").getString().equals("Individual Rounds")) color = null;
            }
            String category = isWinEligible ? "Win" : "Complete%";

            // Requeue if the run is not fast enough to beat the personal best
            if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Requeue Slow Run").getBoolean()
                    // Don't requeue if in party
                    && gameInProgress && (!GameData.isInParty || ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_GENERAL).get("Queue With Party").getBoolean())
                    // Either local or public personal best is set
                    && Math.max(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(category).getInt(), publicPersonalBests.get(category)) != 0
                    // Run time plus 3s per remaining game round (roughly) is greater than personal best
                    && inGameDuration + (16 - GameData.round) * 3000L > Utils.notZeroMin(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(category).getInt(), publicPersonalBests.get(category))) {
                gameInProgress = false;
                Utils.sendChat(String.format("You were unable to beat your %s personal best", category));
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/play arcade_simon_says");
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        if (event.type.equals(RenderGameOverlayEvent.ElementType.BOSSHEALTH)) {
            if (color == null) {
                event.setCanceled(true);
            } else if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean() && GameData.inHypixelSays) {
                BossStatus.setBossStatus(new EntityWither(Minecraft.getMinecraft().theWorld), true);
                BossStatus.bossName = String.format("\u00A7%s%s", color, Utils.formatTime(time));
                BossStatus.healthScale = Math.min(scale, 1);
            }
        }
    }

    static Round getSpeedrunRound(String message) {
        message = message.toLowerCase();
        if (message.contains("add")) return Round.ANVIL; // Add enchantment to a material sword
        else if (message.contains("golem")) return Round.IRON_GOLEM; // Build an iron golem
        else if (message.contains("middle")) return Round.PARKOUR; // Climb to the middle
        else if (message.contains("jump off")) return Round.JUMP; // Jump off the platform
        else if (message.contains("ride a pig")) return Round.PIG; // Ride a pig off the platform
        else if (message.contains("inventory completely")) return Round.INVENTORY; // Make your inventory completely full
        else if (isFinishableRound(message)) return Round.OTHER; // Rounds that can be completed before the end of the round
        else return null;
    }

    static boolean isFinishableRound(String message) {
        message = message.toLowerCase();
        String[] roundIdentifiers = {
                "add", "armor", "arrow", "beacon", "break", "brew", "cookies", "craft", "drink", "eat", "effect", "eggs",
                "enchant", "golem", "grandma", "health", "inventory", "jukebox", "jump", "kill", "middle", "nod", "nose","pig",
                "portal", "present", "press", "repair", "sheep", "slap", "smelt", "snowman", "spin", "tame", "throw", "tree"
        };
        return message.matches(".*\\b(" + String.join("|", roundIdentifiers) + ")\\b.*");
    }

    public static void updatePersonalBests() {
        if (ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Enabled").getBoolean() && !ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").getString().isEmpty()) {
            Utils.asyncHttpGet("www.speedrun.com", String.format("api/v1/users/%s/personal-bests", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").getString()), new HashMap<String, String>() {{put("game", "hypixel_ag");}}, response -> {
                if (response.has("data")) {
                    for (JsonElement run : response.getAsJsonArray("data")) {
                        if (run.getAsJsonObject()
                                .getAsJsonObject("run")
                                .getAsJsonPrimitive("category")
                                .getAsString().equals("mkelxgjd")
                                && run.getAsJsonObject()
                                .getAsJsonObject("run")
                                .getAsJsonObject("values")
                                .getAsJsonPrimitive("dloy55d8")
                                .getAsString().equals("p12ddydq")) {
                            publicPersonalBests.put("Win",
                                    (long) (run.getAsJsonObject()
                                            .getAsJsonObject("run")
                                            .getAsJsonObject("times")
                                            .getAsJsonPrimitive("primary_t")
                                            .getAsFloat() * 1000f));
                        }
                        if (run.getAsJsonObject()
                                .getAsJsonObject("run")
                                .getAsJsonPrimitive("category")
                                .getAsString().equals("mkelxgjd")
                                && run.getAsJsonObject()
                                .getAsJsonObject("run")
                                .getAsJsonObject("values")
                                .getAsJsonPrimitive("dloy55d8")
                                .getAsString().equals("z19mmoyl")) {
                            publicPersonalBests.put("Complete%",
                                    (long) (run.getAsJsonObject()
                                            .getAsJsonObject("run")
                                            .getAsJsonObject("times")
                                            .getAsJsonPrimitive("primary_t")
                                            .getAsFloat() * 1000f));
                        }
                        if (run.getAsJsonObject()
                                .getAsJsonObject("run")
                                .getAsJsonPrimitive("category")
                                .getAsString().equals("wdmvrz4d")) {
                            publicPersonalBests.put(levelIDs.get(run.getAsJsonObject()
                                            .getAsJsonObject("run")
                                            .getAsJsonPrimitive("level")
                                            .getAsString()),
                                    (long) (run.getAsJsonObject()
                                            .getAsJsonObject("run")
                                            .getAsJsonObject("times")
                                            .getAsJsonPrimitive("primary_t")
                                            .getAsFloat() * 1000f));
                        }
                    }
                } else Utils.sendChat(String.format("The username %s does not exist on speedrun.com", ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get("Speedrun.com Username").getString()));
            });
        }
    }

    public static void resetPersonalBests() {
        publicPersonalBests.put("Win", 0L);
        publicPersonalBests.put("Complete%", 0L);
        publicPersonalBests.put("Anvil", 0L);
        publicPersonalBests.put("Iron Golem", 0L);
        publicPersonalBests.put("Parkour", 0L);
        publicPersonalBests.put("Jump off Platform", 0L);
        publicPersonalBests.put("Pig off Platform", 0L);
        publicPersonalBests.put("Full Inventory", 0L);
    }

    public static long getPersonalBest(String category) {
        try {
            return Utils.notZeroMin(publicPersonalBests.get(category), ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(category).getInt());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No " + category + " personal best exists");
        }
    }

    public static void getWorldRecords() {
        System.out.print("Here ------------------------------- ");
        for (String variable : new String[]{"p12ddydq", "z19mmoyl"}){
            Utils.asyncHttpGet("www.speedrun.com", "api/v1/leaderboards/hypixel_ag/category/mkelxgjd", new HashMap<String, String>() {{put("var-dloy55d8", variable);put("top", "1");}}, response ->
                    worldRecords.put(variable.equals("p12ddydq") ? "Win" : "Complete%", (long) (response.getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonArray("runs")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("run")
                    .getAsJsonObject("times")
                    .getAsJsonPrimitive("primary_t")
                    .getAsFloat() * 1000)
            ));
        }
        for (String level : levelIDs.keySet()) {
            Utils.asyncHttpGet("www.speedrun.com", "api/v1/leaderboards/hypixel_ag/level/" + level + "/wdmvrz4d", new HashMap<String, String>() {{put("top", "1");}}, response ->
                    worldRecords.put(levelIDs.get(level), (long) (response.getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonArray("runs")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("run")
                    .getAsJsonObject("times")
                    .getAsJsonPrimitive("primary_t")
                    .getAsFloat() * 1000)
            ));
        }

        // In case a local personal best is faster than the public world record
        for (String category : worldRecords.keySet()) {
            try {
                worldRecords.put(category, Utils.notZeroMin(worldRecords.get(category), ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_SPEEDRUN).get(category).getInt()));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
