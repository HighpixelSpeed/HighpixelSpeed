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
`/hs play` Join Hypixel Says.

#### Autododge
Autododge will requeue if any players in your queue are over a certain threshold of wins.
Due to unfortunate circumstances, when it checks the API for the players' win count, it freezes the game.
You may experience small frame hitches every five seconds in the queue (not in game, so it doesn't matter) (sorry Aussies).
The solution to this issue is to not use autododge, you coward.  
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

#### Hypixel API key
If you use autododge, Highpixel Speed needs to access the Hypixel API.
To do this, it needs a valid API key.
The only way to get the key automatically is to reset it.
However, if you use your API key for any other application, resetting it is not an option.
You may manually enter your key in the config or by command.
If you don't use your key and don't want to deal with the setup, use automatic mode.  
`/hs key help` Show a summary of these commands.  
`/hs key automatic` Automatically handle your API key.
This will reset your key every time you restart the game.  
`/hs key manual` Use your saved API key.  
`/hs key <key>` Set your Hypixel API key. Be aware that it will be saved on your computer.  