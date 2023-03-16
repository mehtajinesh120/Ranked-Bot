/*     */ package com.kasp.rankedbot.instance;
/*     */ 
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.GameState;
/*     */ import com.kasp.rankedbot.PickingMode;
/*     */ import com.kasp.rankedbot.RankedBot;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.database.SQLGameManager;
/*     */ import com.kasp.rankedbot.database.SQLite;
/*     */ import com.kasp.rankedbot.instance.cache.GameCache;
/*     */ import com.kasp.rankedbot.instance.cache.MapCache;
/*     */ import com.kasp.rankedbot.instance.cache.PartyCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Game
/*     */ {
/*     */   private int number;
/*     */   private Guild guild;
/*     */   private Category channelsCategory;
/*     */   private Category vcsCategory;
/*     */   private String channelID;
/*     */   private String vc1ID;
/*     */   private String vc2ID;
/*     */   private Queue queue;
/*     */   private List<Player> players;
/*     */   private Map<Player, Integer> playersInParties;
/*     */   private Player captain1;
/*     */   private Player captain2;
/*     */   private Player currentCaptain;
/*     */   private List<Player> team1;
/*     */   private List<Player> team2;
/*     */   private List<Player> remainingPlayers;
/*     */   private GameState state;
/*     */   private boolean casual;
/*     */   private GameMap map;
/*     */   private Player mvp;
/*     */   private Member scoredBy;
/*     */   private TimerTask closingTask;
/*     */   private HashMap<Player, Integer> eloGain;
/*     */   
/*     */   public Game(List<Player> players, Queue queue) {
/*  62 */     this.guild = RankedBot.getGuild();
/*  63 */     this.players = new ArrayList<>(players);
/*  64 */     this.queue = queue;
/*     */     
/*  66 */     this.number = SQLGameManager.getGameSize() + 1;
/*     */     
/*  68 */     this.team1 = new ArrayList<>();
/*  69 */     this.team2 = new ArrayList<>();
/*     */     
/*  71 */     this.state = GameState.STARTING;
/*     */     
/*  73 */     this.casual = queue.isCasual();
/*     */     
/*  75 */     List<GameMap> maps = new ArrayList<>(MapCache.getMaps().values());
/*  76 */     Collections.shuffle(maps);
/*  77 */     this.map = maps.get(0);
/*     */     
/*  79 */     this.channelsCategory = this.guild.getCategoryById(Config.getValue("game-channels-category"));
/*  80 */     this.vcsCategory = this.guild.getCategoryById(Config.getValue("game-vcs-category"));
/*     */     
/*  82 */     TextChannel channel = (TextChannel)this.channelsCategory.createTextChannel(Config.getValue("game-channel-names").replaceAll("%number%", "" + this.number).replaceAll("%mode%", "" + queue.getPlayersEachTeam() + "v" + queue.getPlayersEachTeam())).complete();
/*  83 */     VoiceChannel vc1 = (VoiceChannel)this.vcsCategory.createVoiceChannel(Config.getValue("game-vc-names").replaceAll("%number%", "" + this.number).replaceAll("%mode%", "" + queue.getPlayersEachTeam() + "v" + queue.getPlayersEachTeam()).replaceAll("%team%", "1")).setUserlimit(Integer.valueOf(queue.getPlayersEachTeam())).complete();
/*  84 */     VoiceChannel vc2 = (VoiceChannel)this.vcsCategory.createVoiceChannel(Config.getValue("game-vc-names").replaceAll("%number%", "" + this.number).replaceAll("%mode%", "" + queue.getPlayersEachTeam() + "v" + queue.getPlayersEachTeam()).replaceAll("%team%", "2")).setUserlimit(Integer.valueOf(queue.getPlayersEachTeam())).complete();
/*     */     
/*  86 */     this.channelID = channel.getId();
/*  87 */     this.vc1ID = vc1.getId();
/*  88 */     this.vc2ID = vc2.getId();
/*     */     
/*  90 */     Collections.shuffle(players);
/*  91 */     this.remainingPlayers = new ArrayList<>(players);
/*     */     
/*  93 */     if (queue.getPickingMode() == PickingMode.CAPTAINS) {
/*  94 */       this.captain1 = players.get(0);
/*  95 */       this.captain2 = players.get(1);
/*     */       
/*  97 */       this.currentCaptain = this.captain1;
/*  98 */       if (this.captain1.getElo() > this.captain2.getElo()) {
/*  99 */         this.currentCaptain = this.captain2;
/*     */       }
/*     */       
/* 102 */       this.team1.add(this.captain1);
/* 103 */       this.team2.add(this.captain2);
/*     */       
/*     */       try {
/* 106 */         this.guild.moveVoiceMember(this.guild.getMemberById(this.captain1.getID()), vc1).queue();
/* 107 */         this.guild.moveVoiceMember(this.guild.getMemberById(this.captain2.getID()), vc2).queue();
/* 108 */       } catch (Exception exception) {}
/*     */       
/* 110 */       this.remainingPlayers.remove(this.captain1);
/* 111 */       this.remainingPlayers.remove(this.captain2);
/*     */     } 
/*     */     
/* 114 */     for (Player p : players) {
/* 115 */       channel.createPermissionOverride((IPermissionHolder)this.guild.getMemberById(p.getID())).setAllow(new Permission[] { Permission.VIEW_CHANNEL }).queue();
/* 116 */       vc1.createPermissionOverride((IPermissionHolder)this.guild.getMemberById(p.getID())).setAllow(new Permission[] { Permission.VIEW_CHANNEL }).setAllow(new Permission[] { Permission.VOICE_CONNECT }).queue();
/* 117 */       vc2.createPermissionOverride((IPermissionHolder)this.guild.getMemberById(p.getID())).setAllow(new Permission[] { Permission.VIEW_CHANNEL }).setAllow(new Permission[] { Permission.VOICE_CONNECT }).queue();
/*     */     } 
/*     */     
/* 120 */     for (Player p : this.remainingPlayers) {
/*     */       try {
/* 122 */         this.guild.moveVoiceMember(this.guild.getMemberById(p.getID()), vc1).queue();
/* 123 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 126 */     GameCache.initializeGame(this);
/*     */   }
/*     */   
/*     */   public Game(int number) {
/* 130 */     this.number = number;
/* 131 */     this.guild = RankedBot.getGuild();
/*     */     
/* 133 */     this.team1 = new ArrayList<>();
/* 134 */     this.team2 = new ArrayList<>();
/* 135 */     this.eloGain = new HashMap<>();
/* 136 */     this.remainingPlayers = new ArrayList<>();
/* 137 */     this.players = new ArrayList<>();
/*     */     
/* 139 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM games WHERE number=" + number + ";");
/*     */     
/*     */     try {
/* 142 */       this.state = GameState.valueOf(resultSet.getString(3).toUpperCase());
/* 143 */       this.casual = Boolean.parseBoolean(resultSet.getString(4));
/* 144 */       this.map = MapCache.getMap(resultSet.getString(5));
/* 145 */       this.channelID = resultSet.getString(6);
/* 146 */       this.vc1ID = resultSet.getString(7);
/* 147 */       this.vc2ID = resultSet.getString(8);
/* 148 */       this.queue = QueueCache.getQueue(resultSet.getString(9));
/*     */       
/* 150 */       if (this.state != GameState.STARTING) {
/* 151 */         if (this.state != GameState.SCORED) {
/* 152 */           for (int i = 0; i < this.queue.getPlayersEachTeam(); i++) {
/* 153 */             this.team1.add(PlayerCache.getPlayer(resultSet.getString(10).split(",")[i]));
/* 154 */             this.team2.add(PlayerCache.getPlayer(resultSet.getString(11).split(",")[i]));
/* 155 */             this.players.add(this.team1.get(i));
/* 156 */             this.players.add(this.team2.get(i));
/*     */           } 
/*     */         } else {
/*     */           
/* 160 */           for (int i = 0; i < this.queue.getPlayersEachTeam(); i++) {
/* 161 */             this.team1.add(PlayerCache.getPlayer(resultSet.getString(10).split(",")[i].split("=")[0]));
/* 162 */             this.eloGain.put(this.team1.get(i), Integer.valueOf(Integer.parseInt(resultSet.getString(10).split(",")[i].split("=")[1])));
/*     */             
/* 164 */             this.team2.add(PlayerCache.getPlayer(resultSet.getString(11).split(",")[i].split("=")[0]));
/* 165 */             this.eloGain.put(this.team2.get(i), Integer.valueOf(Integer.parseInt(resultSet.getString(11).split(",")[i].split("=")[1])));
/*     */             
/* 167 */             this.players.add(this.team1.get(i));
/* 168 */             this.players.add(this.team2.get(i));
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 173 */       if (this.state == GameState.SCORED) {
/* 174 */         if (resultSet.getString(12) != null) {
/* 175 */           this.mvp = PlayerCache.getPlayer(resultSet.getString(12));
/*     */         }
/* 177 */         this.scoredBy = this.guild.getMemberById(resultSet.getString(13));
/*     */       } 
/*     */       
/* 180 */       if (this.state == GameState.VOIDED) {
/* 181 */         this.scoredBy = this.guild.getMemberById(resultSet.getString(13));
/*     */       }
/* 183 */     } catch (SQLException e) {
/* 184 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 187 */     GameCache.initializeGame(this);
/*     */   }
/*     */   
/*     */   public void pickTeams() {
/* 191 */     if (this.queue.getPickingMode() == PickingMode.AUTOMATIC) {
/* 192 */       this.playersInParties = new HashMap<>();
/*     */       
/* 194 */       if (PartyCache.getParty(this.captain1) != null) {
/* 195 */         this.playersInParties.put(this.captain1, Integer.valueOf(PartyCache.getParty(this.captain1).getMembers().size()));
/*     */       }
/*     */       
/* 198 */       if (PartyCache.getParty(this.captain2) != null) {
/* 199 */         this.playersInParties.put(this.captain2, Integer.valueOf(PartyCache.getParty(this.captain2).getMembers().size()));
/*     */       }
/*     */ 
/*     */       
/* 203 */       List<Party> parties = new ArrayList<>();
/* 204 */       for (Player p : this.remainingPlayers) {
/* 205 */         if (PartyCache.getParty(p) != null) {
/* 206 */           this.playersInParties.put(p, Integer.valueOf(PartyCache.getParty(p).getMembers().size()));
/* 207 */           if (!parties.contains(PartyCache.getParty(p))) {
/* 208 */             parties.add(PartyCache.getParty(p));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 213 */       if (parties.size() > 0) {
/* 214 */         for (Party p : parties) {
/* 215 */           if (this.team1.size() < this.team2.size()) {
/* 216 */             if (this.queue.getPlayersEachTeam() - this.team1.size() >= p.getMembers().size()) {
/* 217 */               this.team1.addAll(p.getMembers());
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 222 */           else if (this.queue.getPlayersEachTeam() - this.team2.size() >= p.getMembers().size()) {
/* 223 */             this.team2.addAll(p.getMembers());
/*     */           } 
/*     */ 
/*     */           
/* 227 */           this.remainingPlayers.removeAll(p.getMembers());
/*     */         } 
/*     */       }
/*     */       
/* 231 */       Collections.shuffle(this.remainingPlayers);
/* 232 */       for (Player p : this.remainingPlayers) {
/* 233 */         if (this.queue.getPlayersEachTeam() - this.team1.size() > 0) {
/* 234 */           this.team1.add(p);
/*     */           continue;
/*     */         } 
/* 237 */         this.team2.add(p);
/*     */       } 
/*     */       
/* 240 */       this.remainingPlayers.clear();
/*     */       
/* 242 */       start();
/*     */     } else {
/*     */       
/* 245 */       sendGameMsg();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() {
/* 250 */     this.state = GameState.PLAYING;
/*     */     
/* 252 */     sendGameMsg();
/*     */     
/* 254 */     for (Player p : this.team2) {
/*     */       try {
/* 256 */         this.guild.moveVoiceMember(this.guild.getMemberById(p.getID()), this.guild.getVoiceChannelById(this.vc2ID)).queue();
/* 257 */       } catch (Exception exception) {}
/*     */     } 
/* 259 */     if (this.casual) {
/* 260 */       closeChannel(1800);
/*     */     }
/*     */     
/* 263 */     createGame(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendGameMsg() {
/* 268 */     String mentions = "";
/* 269 */     for (Player p : this.players) {
/* 270 */       mentions = mentions + mentions;
/*     */     }
/*     */     
/* 273 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game `#" + this.number + "`", "", 1);
/*     */     
/* 275 */     if (this.queue.getPickingMode() == PickingMode.CAPTAINS) {
/* 276 */       embed.setDescription(this.guild.getMemberById(this.currentCaptain.getID()).getAsMention() + "'s turn to `=pick`");
/*     */     }
/*     */     
/* 279 */     String team1 = "";
/* 280 */     for (Player p : this.team1) {
/* 281 */       team1 = team1 + "• <@!" + team1 + ">\n";
/*     */     }
/* 283 */     embed.addField("Team 1", team1, true);
/*     */     
/* 285 */     String team2 = "";
/* 286 */     for (Player p : this.team2) {
/* 287 */       team2 = team2 + "• <@!" + team2 + ">\n";
/*     */     }
/* 289 */     embed.addField("Team 2", team2, true);
/*     */     
/* 291 */     if (this.remainingPlayers.size() != 0) {
/* 292 */       String remaining = "";
/* 293 */       for (Player p : this.remainingPlayers) {
/* 294 */         remaining = remaining + "• <@!" + remaining + ">\n";
/*     */       }
/*     */       
/* 297 */       embed.addField("Remaining", remaining, false);
/*     */     } 
/*     */     
/* 300 */     embed.addField("Randomly Picked Map", "**" + this.map.getName() + "** — `Height: " + this.map.getHeight() + "` (" + this.map.getTeam1() + " vs " + this.map.getTeam2() + ")", false);
/*     */     
/* 302 */     if (this.remainingPlayers.size() == 0) {
/* 303 */       embed.setDescription("");
/* 304 */       embed.setTitle("Game `#" + this.number + "` has started!");
/*     */       
/* 306 */       this.guild.getTextChannelById(Config.getValue("games-announcing")).sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*     */       
/* 308 */       if (Config.getValue("party-invite-cmd") != null) {
/* 309 */         String party = Config.getValue("party-invite-cmd");
/* 310 */         for (Player p : this.players) {
/* 311 */           party = party + " " + party;
/*     */         }
/* 313 */         embed.addField("Party Invite Cmd", "`" + party + "`", false);
/*     */       } 
/*     */       
/* 316 */       if (this.casual) {
/* 317 */         embed.setDescription("You queued a casual queue meaning this game will have no impact on players' stats");
/*     */       }
/*     */       
/* 320 */       embed.setDescription("do not forget to `=submit` after your game ends");
/*     */     } 
/*     */     
/* 323 */     this.guild.getTextChannelById(this.channelID).sendMessage(mentions).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pickPlayer(Player sender, Player picked) {
/* 329 */     if (this.state != GameState.STARTING) {
/* 330 */       return false;
/*     */     }
/*     */     
/* 333 */     if (sender != this.currentCaptain) {
/* 334 */       return false;
/*     */     }
/*     */     
/* 337 */     if (!this.remainingPlayers.contains(picked)) {
/* 338 */       return false;
/*     */     }
/*     */     
/* 341 */     if (sender == picked) {
/* 342 */       return false;
/*     */     }
/*     */     
/* 345 */     getPlayerTeam(sender).add(picked);
/* 346 */     this.remainingPlayers.remove(picked);
/*     */     
/* 348 */     this.currentCaptain = getOppTeam(PlayerCache.getPlayer(sender.getID())).get(0);
/*     */     
/* 350 */     sendGameMsg();
/*     */     
/* 352 */     if (this.remainingPlayers.size() == 1) {
/* 353 */       getOppTeam(sender).add(this.remainingPlayers.get(0));
/* 354 */       this.remainingPlayers.remove(this.remainingPlayers.get(0));
/*     */       
/* 356 */       start();
/*     */     } 
/*     */     
/* 359 */     return true;
/*     */   }
/*     */   
/*     */   public List<Player> getPlayerTeam(Player player) {
/* 363 */     if (this.team1.contains(player)) {
/* 364 */       return this.team1;
/*     */     }
/*     */     
/* 367 */     if (this.team2.contains(player)) {
/* 368 */       return this.team2;
/*     */     }
/*     */     
/* 371 */     return null;
/*     */   }
/*     */   
/*     */   public List<Player> getOppTeam(Player player) {
/* 375 */     if (!this.team1.contains(player)) {
/* 376 */       return this.team1;
/*     */     }
/*     */     
/* 379 */     if (!this.team2.contains(player)) {
/* 380 */       return this.team2;
/*     */     }
/*     */     
/* 383 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scoreGame(List<Player> winningTeam, List<Player> losingTeam, Player mvp, Member scoredBy) {
/* 388 */     this.eloGain = new HashMap<>();
/*     */     
/* 390 */     for (Player p : this.players) {
/* 391 */       this.eloGain.put(p, Integer.valueOf(0));
/*     */ 
/*     */       
/* 394 */       int elo = p.getElo();
/*     */       
/* 396 */       double eloMultiplier = Double.parseDouble(Config.getValue("solo-multiplier"));
/* 397 */       if (this.queue.getPickingMode() == PickingMode.AUTOMATIC && 
/* 398 */         this.playersInParties.containsKey(p)) {
/* 399 */         eloMultiplier = Double.parseDouble(Config.getValue("party-multiplier-" + this.playersInParties.get(p)));
/*     */       }
/*     */ 
/*     */       
/* 403 */       if (winningTeam.contains(p)) {
/* 404 */         p.win(this.queue.getEloMultiplier() * eloMultiplier);
/*     */       }
/* 406 */       else if (losingTeam.contains(p)) {
/* 407 */         p.lose(this.queue.getEloMultiplier() * eloMultiplier);
/*     */       } 
/* 409 */       int difference = p.getElo() - elo;
/* 410 */       this.eloGain.put(p, Integer.valueOf(((Integer)this.eloGain.get(p)).intValue() + difference));
/* 411 */       p.fix();
/*     */     } 
/*     */     
/* 414 */     if (mvp != null) {
/* 415 */       setMvp(mvp);
/* 416 */       mvp.setMvp(mvp.getMvp() + 1);
/* 417 */       mvp.setElo(mvp.getElo() + mvp.getRank().getMvpElo());
/* 418 */       this.eloGain.put(mvp, Integer.valueOf(((Integer)this.eloGain.get(mvp)).intValue() + mvp.getRank().getMvpElo()));
/* 419 */       mvp.fix();
/*     */     } 
/*     */     
/* 422 */     setState(GameState.SCORED);
/*     */     
/* 424 */     setScoredBy(scoredBy);
/* 425 */     Player player = PlayerCache.getPlayer(scoredBy.getId());
/* 426 */     player.setScored(player.getScored() + 1);
/*     */     
/* 428 */     SQLGameManager.updateEloGain(this.number);
/*     */     
/* 430 */     closeChannel(Integer.parseInt(Config.getValue("game-deleting-time")));
/*     */   }
/*     */   
/*     */   public void undo() {
/* 434 */     setState(GameState.SUBMITTED);
/*     */     
/* 436 */     for (Player p : this.eloGain.keySet()) {
/* 437 */       if (((Integer)this.eloGain.get(p)).intValue() > 0) {
/* 438 */         p.setWins(p.getWins() - 1);
/*     */       } else {
/*     */         
/* 441 */         p.setLosses(p.getLosses() - 1);
/*     */       } 
/*     */       
/* 444 */       p.setElo(p.getElo() - ((Integer)this.eloGain.get(p)).intValue());
/*     */       
/* 446 */       p.fix();
/*     */     } 
/*     */     
/* 449 */     Player player = PlayerCache.getPlayer(this.scoredBy.getId());
/* 450 */     player.setScored(player.getScored() - 1);
/*     */     
/* 452 */     if (this.mvp != null) {
/* 453 */       this.mvp.setMvp(this.mvp.getMvp() - 1);
/*     */     }
/*     */     
/* 456 */     SQLGameManager.removeEloGain(this.number);
/*     */   }
/*     */   
/*     */   public void closeChannel(int timeSeconds) {
/* 460 */     if (this.guild.getTextChannelById(this.channelID) != null) {
/* 461 */       Embed embed = new Embed(EmbedType.DEFAULT, "", "Game channel deleting in `" + timeSeconds + "` seconds / `" + timeSeconds / 60 + "` minutes", 1);
/* 462 */       this.guild.getTextChannelById(this.channelID).sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*     */     } 
/*     */     
/* 465 */     if (this.closingTask != null) {
/* 466 */       this.closingTask.cancel();
/*     */     }
/*     */     
/* 469 */     this.closingTask = new TimerTask()
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 473 */             if (Game.this.guild.getTextChannelById(Game.this.channelID) != null) {
/* 474 */               Game.this.guild.getTextChannelById(Game.this.channelID).delete().queue();
/*     */             }
/* 476 */             if (Game.this.guild.getVoiceChannelById(Game.this.vc1ID) != null) {
/* 477 */               Game.this.guild.getVoiceChannelById(Game.this.vc1ID).delete().queue();
/*     */             }
/* 479 */             if (Game.this.guild.getVoiceChannelById(Game.this.vc2ID) != null) {
/* 480 */               Game.this.guild.getVoiceChannelById(Game.this.vc2ID).delete().queue();
/*     */             }
/* 482 */           } catch (Exception e) {
/* 483 */             e.printStackTrace();
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 488 */     (new Timer()).schedule(this.closingTask, timeSeconds * 1000L);
/*     */   }
/*     */   
/*     */   public static void createGame(Game g) {
/* 492 */     SQLGameManager.createGame(g);
/*     */   }
/*     */   
/*     */   public HashMap<Player, Integer> getEloGain() {
/* 496 */     return this.eloGain;
/*     */   }
/*     */   
/*     */   public int getNumber() {
/* 500 */     return this.number;
/*     */   }
/*     */   
/*     */   public String getChannelID() {
/* 504 */     return this.channelID;
/*     */   }
/*     */   
/*     */   public Guild getGuild() {
/* 508 */     return this.guild;
/*     */   }
/*     */   
/*     */   public void setGuild(Guild guild) {
/* 512 */     this.guild = guild;
/*     */   }
/*     */   
/*     */   public String getVC1ID() {
/* 516 */     return this.vc1ID;
/*     */   }
/*     */   
/*     */   public String getVC2ID() {
/* 520 */     return this.vc2ID;
/*     */   }
/*     */   
/*     */   public Queue getQueue() {
/* 524 */     return this.queue;
/*     */   }
/*     */   
/*     */   public List<Player> getPlayers() {
/* 528 */     return this.players;
/*     */   }
/*     */   
/*     */   public List<Player> getTeam1() {
/* 532 */     return this.team1;
/*     */   }
/*     */   
/*     */   public List<Player> getTeam2() {
/* 536 */     return this.team2;
/*     */   }
/*     */   
/*     */   public List<Player> getRemainingPlayers() {
/* 540 */     return this.remainingPlayers;
/*     */   }
/*     */   
/*     */   public GameState getState() {
/* 544 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(GameState state) {
/* 548 */     this.state = state;
/* 549 */     SQLGameManager.updateState(this.number);
/*     */   }
/*     */   
/*     */   public boolean isCasual() {
/* 553 */     return this.casual;
/*     */   }
/*     */   
/*     */   public GameMap getMap() {
/* 557 */     return this.map;
/*     */   }
/*     */   
/*     */   public Player getMvp() {
/* 561 */     return this.mvp;
/*     */   }
/*     */   
/*     */   public void setMvp(Player mvp) {
/* 565 */     this.mvp = mvp;
/* 566 */     SQLGameManager.updateMvp(this.number);
/*     */   }
/*     */   
/*     */   public Member getScoredBy() {
/* 570 */     return this.scoredBy;
/*     */   }
/*     */   
/*     */   public void setScoredBy(Member scoredBy) {
/* 574 */     this.scoredBy = scoredBy;
/* 575 */     SQLGameManager.updateScoredBy(this.number);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Game.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */