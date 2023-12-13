# Highpixel Speed

Highpixel Speed is a quality of life mod for Hypixel's arcade game Hypixel Says.
In Hypixel Says, you can win even if you leave the game, as long as you still have the most points.
The core feature of this mod is detecting when you have enough points to be guaranteed to win and joining a new game.
There are also a number of other features that help you grind wins as quickly as possible.

### Instructions
Download the [latest release file](https://github.com/HighpixelSpeed/HighpixelSpeed/releases/latest), the one without `sources`.
Put this in the mods folder (which can be accessed by running `%appdata%` in File Explorer, then opening `.minecraft` and `mods`).
Launch minecraft with Forge 1.8.9.

### Commands
These settings can also be changed in the Forge mod config. The default values can be seen there as well.

#### General
`/hs` Toggle enabling and disabling the mod. When this is enabled, you will requeue when you are guaranteed to win.  
`/hs forty` Toggle disabling requeuing if you can get a 40-point game.  
`/hs fortyonly` Toggle requeuing if you cannot get a 40-point game.  
`/hs empty` Toggle requeuing if there are not enough people in your queue to start a game.  
`/hs help` Show a summary of these commands.  
`/hs loss` Toggle requeuing if you cannot get enough points and are guaranteed to lose the game.  
`/hs party` Toggle requeuing normally even if you are in a party  
`/hs play` Join Hypixel Says.

#### Autododge
Autododge will requeue if any players in your queue are over a certain threshold of wins.  
`/hs autododge` Toggle autododge.  
`/hs autododge help` Show a summary of these commands.  
`/hs autododge <wins>` Set the number of wins the player must have to scare you.

#### Blacklist
You can add players who always beat you to your blacklist, and they will always be avoided in the future.
Players will stay blacklisted even if they change their usernames.
Beware that the players most likely to be on this list are possibly the players most likely to be nicked, which blacklist cannot handle.  
`/hs blacklist` Toggle blacklist.  
`/hs blacklist help` Show a summary of these commands.  
`/hs blacklist add <username> [username] ...` Add one or more players to your blacklist.  
`/hs blacklist list` Show all the players currently on your blacklist.  
`/hs blacklist remove <username> [username] ...` Remove one or more players to your blacklist.

#### Session Stats
This keeps track of the games played, wins, win-loss ratio, points, and points per win of your current play session.
These reset when you restart your game, but you can save them and keep stats from multiple sessions.
Beware, if you load your stats from a previous session and forget to save them again, they will be lost.  
`/hs stats` Toggle session stats.  
`/hs stats help` Show a summary of these commands.  
`/hs stats reset` Set all stats to 0.  
`/hs stats save` Save your current stats to be used later.  
`/hs stats load` Retrieve your stats from an earlier session.
You can either load these stats or add them to your current stats.

#### Speedrun
When speedrun is enabled, requeuing is disabled, and instead, a timer displays the length of the current game or round.
When you break your record for a category, your run time is saved.
Both the "Win" and "Complete%" categories and all six levels from [speedrun.com](https://www.speedrun.com/hypixel_ag?h=Hypixel_Says-Win&x=mkelxgjd-dloy55d8.p12ddydq) are timed.
Check the website for category rules.
You can enter your speedrun.com username, and Highpixel Speed will keep track of your previous official records.
The boss bar displays the progression of the run.
The bar reaching the end indicates your previous record if you have set one, and the world record otherwise.
The color of the timer indicates the following:  
**Green**: The run is faster than the world record.  
**Yellow**: The run is slower than the world record.  
**Red**: The run is slower than your personal best, or you have failed the round.  
After the run ends:  
**Dark Green**: You have beaten your personal best (or set a new one).  
**Gold**: You have beaten the world record.  
`/hs speedrun` Toggle speedrun.  
`/hs speedrun help` Show a summary of these commands.  
`/hs speedrun game` Enable Full Game mode.
The timer shows the duration of the entire game.  
If the run does not qualify for the "Win" category, the timer will switch to timing for the "Complete%" category.  
`/hs speedrun round` Enable Individuals Rounds mode.
The timer shows the duration of the current round.  
`/hs speedrun all`  Toggle Time All Rounds mode.
The timer is always enabled for the six levels on speedrun.com.
If Time All Rounds is enabled, the timer displays every round which can be finished before the end of the round.
These times are never saved.
This setting does nothing when Full Game is enabled.  
`/hs speedrun name` Display your username set for speedrun.com.  
`/hs speedrun name <username>` Enter your speedrun.com username if you have any posted Hypixel Says runs.
You can, of course, enter someone else's name and race against their runs instead.  
`/hs speedrun requeue` Toggle requeuing when your run time has surpassed your personal best.
Highpixel Speed conservatively estimates when it becomes impossible to complete the game in less time than your personal best.  
`/hs speedrun stats` Display your personal bests from the mod or speedrun.com for all categories.  
`/hs speedrun reset` Erase your personal bests.
This cannot be undone.

#### Tag Wins
Similar to the mod Levelhead, the Hypixel Says wins of players around you are shown. The color of the number indicates that the player has the following:  
**Gray**: 0 wins  
**Yellow**: At least one win  
**Green**: More wins than you  
**Gold**: Enough wins to dodge, if Autododge is enabled  
`/hs tagwins` Toggle whether to tag players' wins by their names.
