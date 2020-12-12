# Lapland

<b>Only compatible with version 1.16.</b>

Lapland is a simple and straightforward party plugin that allows players to share their experience gains.

<br>

<img src="https://i.postimg.cc/ZnCmMsRB/party-info.png" alt="/pt info" width="400"/>
<img src="https://i.postimg.cc/QN1ZH77r/invitation.png" alt="Player receiving a party invitation." width="400"/>

<br>

## 📝&nbsp;&nbsp;&nbsp;To-do
- [x] Add an <code>info</code> subcommand to provide the player information pertaining to their current party. i.e. List of players, online/offline status, who the leader is.
- [ ] Add a <code>help</code> subcommand to provide the player with detailed usage information.
- [ ] Add a <code>leader</code> subcommand so the leader may pass on leadership to another party member.
- [x] Add a <code>notify</code> subcommand so players can toggle experience gain messages.
- [ ] Add a <code>lapland</code> command that allows an administrator to adjust the configuration within the game or server console.

<br>

## ⌨️&nbsp;&nbsp;&nbsp;Commands
| Command                           | Description                                                                                                                     |
|-----------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| /party create <code>name</code>   | Create a new party with a name.                                                                                                 |
| /party invite <code>player</code> | Send a party invitation to a player.                                                                                            |
| /party leave                      | Leave the party. A new leader will be chosen based on seniority. If you are the only party member, the party will be disbanded. |
| /party disband                    | Disband the party.                                                                                                              |
| /party kick <code>name</code>     | Is someone not playing fair? Kick them out. :-)                                                                                 |
| /party notify                     | Toggle messages indicating how much experience you're gaining. This is off by default.                                          |
| /party info <code>page</code>     | Outputs information about the current party you're in. Use <code>/pt</code> to quickly view your party info.                    |

### Aliases
| Command  | Alias(s) |
|----------|----------|
| /party   | /pt      |
| /lapland | /lp      |

<br> 

## 🔐&nbsp;&nbsp;&nbsp;Permissions
| Permission    | Default | Description                                 |
|---------------|---------|---------------------------------------------|
| lapland.*     | op      | Allows the usage of all Lapland commands.   |
| lapland.party | true    | Allows the usage of Lapland's party system. |

<br>

## ⚙️&nbsp;&nbsp;&nbsp;Configuration 
| Setting                | Default | Description                                                                                            |
|------------------------|---------|--------------------------------------------------------------------------------------------------------|
| maxDistance    | 10      | The maximum distance at which experience can be shared, in chunks. Use 0 for an infinite range.                  |
| dimensionalSharing      | false   | Experience is shared freely among all worlds ignoring <code>maxDistance</code>.                                      |
| maxPlayers             | 10      | The maximum amount of players allowed in a party.                                                      |
| experienceKept     | 50      | The percentage of experience kept by the player. The remaining experience is evenly split among the rest of the party. The higher <code>experienceKept</code> is, the less experience the rest of the party receives. |

<br>

## 🎲&nbsp;&nbsp;&nbsp;Experience Sharing
By default, experience in only shared within a 10 chunk radius within the same world. However, server administrators can change the default radius. Multidimensional sharing may also be enabled. i.e. Player One can gain experience in the Nether and Player Two will receive a portion of it in the Overworld.

When experience is gained, <code>experienceKept</code><b>%</b> is kept by the player and the remaining <code>100 - experienceKept</code><b>%</b> is split evenly among the rest of the party. Only party members who are online and alive will be given experience. This percentage can also be configured.
