# Lapland

<b>Only compatible with version 1.16.</b>

Lapland is a simple and straightforward party plugin that allows players to share their experience gains.

<br>

## 📝&nbsp;&nbsp;&nbsp;To-do
- [ ] Add an <code>info</code> subcommand to provide the player information pertaining to their current party. i.e. List of players, online/offline status, who the leader is.
- [ ] Add a <code>help</code> subcommand to provide the player with detailed usage information.
- [ ] Add a <code>leader</code> subcommand so the leader may pass on leadership to another party member.
- [ ] Add a <code>notify</code> subcommand so players can toggle experience gain messages.
- [ ] Add a <code>lapland</code> command that allows an administrator  to adjust the configuration within the game or server console.

<br>

## ⌨️&nbsp;&nbsp;&nbsp;Commands
| Command                           | Description                                                                                                                     |
|-----------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| /party create <code>name</code>   | Create a new party with a name.                                                                                                 |
| /party invite <code>player</code> | Send a party invitation to a player.                                                                                            |
| /party leave                      | Leave the party. A new leader will be chosen based on seniority. If you are the only party member, the party will be disbanded. |
| /party disband                    | Disband the party.                                                                                                              |
| /party kick <code>name</code>     | Is someone not playing fair? Kick them out. :-)                                                                                 |

### Aliases
| Command  | Alias(s) |
|----------|----------|
| /party   | /pt      |
| /lapland | /lp      |

<br> 

## 🔐&nbsp;&nbsp;&nbsp;Permissions
| Permission    | Description                                 |
|---------------|---------------------------------------------|
| lapland.*     | Allows the usage of all Lapland commands.   |
| lapland.party | Allows the usage of Lapland's party system. |

<br>

## ⚙️&nbsp;&nbsp;&nbsp;Configuration 
| Setting                | Default | Description                                                                                            |
|------------------------|---------|--------------------------------------------------------------------------------------------------------|
| maxRadius    | 10      | The maximum distance at which experience can be shared, in chunks. Use 0 for an infinite range.                  |
| crossWorldSharing      | false   | Experience is shared freely among all worlds ignoring maxRadius.                                      |
| maxPlayers             | 10      | The maximum amount of players allowed in a party.                                                      |
| experienceDividend     | 50      | The percentage of experience shared between the other party members. The remainder goes to the player. The lower <code>experienceDividend</code> is, the more experience the player receives. |

<br>

## 🎲&nbsp;&nbsp;&nbsp;Experience Sharing
By default, experience in only shared within a 10 chunk radius within the same world. However, server administrators can change the default radius. Cross world sharing may also be enabled. i.e. Player One can gain experience in the Nether and Player Two will receive a portion of it in the Overworld.

When experience is gained, 50% is kept by the player and the remaining 50% is split evenly amongst the rest of the party. Only party members who are online and alive will be given experience. The experience dividend may also be changed.
