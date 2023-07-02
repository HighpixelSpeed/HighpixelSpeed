# Highpixel Speed

Highpixel Speed is a quality of life mod for Hypixel's arcade game Hypixel Says.
In Hypixel Says, you can win even if you leave the game, as long as you still have the most points.
The core feature of this mod is detecting when you have enough points to be guaranteed to win and joining a new game.
There are also a number of other features that help you grind wins as quickly as possible.

### Instructions
Download the [latest release file](https://github.com/HighpixelSpeed/HighpixelSpeed/releases/latest), the one without `sources`.
Put this in the mods folder (which can be accessed by running `%appdata%` in search, then opening `.minecraft` and `mods`).
Launch minecraft with Forge 1.8.9.

### Commands
These settings can also be changed in the Forge mod config.

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
Beware that the players most likely to be on this list are possibly the players most likely to be nicked, which blacklist cannot yet handle.  
`/hs blacklist` Toggle blacklist.  
`/hs blacklist help` Show a summary of these commands.  
`/hs blacklist add <username> [username] ...` Add one or more players to your blacklist.  
`/hs blacklist list` Show all the players currently on your blacklist.  
`/hs blacklist remove <username> [username] ...` Remove one or more players to your blacklist.
