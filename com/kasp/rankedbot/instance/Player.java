/*     */ package com.kasp.rankedbot.instance;
/*     */ 
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.RankedBot;
/*     */ import com.kasp.rankedbot.Statistic;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.database.SQLPlayerManager;
/*     */ import com.kasp.rankedbot.database.SQLite;
/*     */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*     */ import com.kasp.rankedbot.instance.cache.ClanLevelCache;
/*     */ import com.kasp.rankedbot.instance.cache.LevelCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import com.kasp.rankedbot.instance.cache.RankCache;
/*     */ import com.kasp.rankedbot.instance.cache.ThemeCache;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Player
/*     */ {
/*     */   private String ID;
/*     */   private String ign;
/*     */   private int elo;
/*     */   private int peakElo;
/*     */   private int wins;
/*     */   private int losses;
/*     */   private int winStreak;
/*     */   private int lossStreak;
/*     */   private int highestWS;
/*     */   private int highestLS;
/*     */   private int mvp;
/*     */   
/*     */   public Player(String ID) {
/*  48 */     System.out.println("ID: " + ID);
/*  49 */     this.ID = ID;
/*     */     
/*  51 */     ResultSet resultSet = SQLite.queryData("SELECT * FROM players WHERE discordID='" + ID + "';");
/*     */     
/*     */     try {
/*  54 */       this.ign = resultSet.getString(3);
/*  55 */       this.elo = resultSet.getInt(4);
/*  56 */       this.peakElo = resultSet.getInt(5);
/*  57 */       this.wins = resultSet.getInt(6);
/*  58 */       this.losses = resultSet.getInt(7);
/*  59 */       this.winStreak = resultSet.getInt(8);
/*  60 */       this.lossStreak = resultSet.getInt(9);
/*  61 */       this.highestWS = resultSet.getInt(10);
/*  62 */       this.highestLS = resultSet.getInt(11);
/*  63 */       this.mvp = resultSet.getInt(12);
/*  64 */       this.kills = resultSet.getInt(13);
/*  65 */       this.deaths = resultSet.getInt(14);
/*  66 */       this.strikes = resultSet.getInt(15);
/*  67 */       this.scored = resultSet.getInt(16);
/*  68 */       this.gold = resultSet.getInt(17);
/*  69 */       this.level = LevelCache.getLevel(resultSet.getInt(18));
/*  70 */       this.xp = resultSet.getInt(19);
/*  71 */       this.theme = ThemeCache.getTheme(resultSet.getString(20));
/*     */       
/*  73 */       this.ownedThemes = new ArrayList<>();
/*     */       
/*  75 */       String[] themes = resultSet.getString(21).split(",");
/*  76 */       for (String s : themes) {
/*  77 */         this.ownedThemes.add(ThemeCache.getTheme(s));
/*     */       }
/*     */       
/*  80 */       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
/*  81 */       this.isBanned = Boolean.parseBoolean(resultSet.getString(22));
/*  82 */       if (this.isBanned) {
/*  83 */         this.bannedTill = LocalDateTime.parse(resultSet.getString(23), formatter);
/*  84 */         this.banReason = resultSet.getString(24);
/*     */       } 
/*  86 */     } catch (SQLException e) {
/*  87 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  90 */     PlayerCache.initializePlayer(this);
/*     */   }
/*     */   private int kills; private int deaths; private int strikes; private int scored; private int gold; private Level level; private int xp; private Theme theme; private ArrayList<Theme> ownedThemes; private boolean isBanned; private LocalDateTime bannedTill; private String banReason;
/*     */   public void fix() {
/*  94 */     Guild guild = RankedBot.getGuild();
/*     */     
/*  96 */     Member member = null;
/*  97 */     if (guild.getMemberById(this.ID) != null) {
/*  98 */       member = guild.getMemberById(this.ID);
/*     */     }
/*     */     
/* 101 */     ArrayList<Role> rolestoremove = new ArrayList<>();
/* 102 */     ArrayList<Role> rolestoadd = new ArrayList<>();
/*     */     
/* 104 */     rolestoadd.add(guild.getRoleById(Config.getValue("registered-role")));
/*     */     
/* 106 */     if (getRank() != null) {
/* 107 */       Rank rank = getRank();
/* 108 */       rolestoadd.add(guild.getRoleById(rank.getID()));
/*     */       
/* 110 */       for (Rank r : RankCache.getRanks().values()) {
/* 111 */         if (rank != r) {
/* 112 */           rolestoremove.add(guild.getRoleById(r.getID()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     if (this.isBanned) {
/* 118 */       if (getBannedTill().isBefore(LocalDateTime.now())) {
/* 119 */         rolestoremove.add(guild.getRoleById(Config.getValue("banned-role")));
/*     */       } else {
/*     */         
/* 122 */         rolestoadd.add(guild.getRoleById(Config.getValue("banned-role")));
/*     */       } 
/*     */     } else {
/*     */       
/* 126 */       rolestoremove.add(guild.getRoleById(Config.getValue("banned-role")));
/*     */     } 
/*     */     
/* 129 */     if (member != null) {
/* 130 */       guild.modifyMemberRoles(member, rolestoadd, rolestoremove).queue();
/* 131 */       member.modifyNickname(Config.getValue("elo-formatting").replaceAll("%elo%", "" + this.elo) + Config.getValue("elo-formatting").replaceAll("%elo%", "" + this.elo)).queue();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void wipe() {
/* 136 */     setElo(Integer.parseInt(Config.getValue("starting-elo")));
/* 137 */     setPeakElo(Integer.parseInt(Config.getValue("starting-elo")));
/* 138 */     setWins(0);
/* 139 */     setLosses(0);
/* 140 */     setWinStreak(0);
/* 141 */     setLossStreak(0);
/* 142 */     setHighestWS(0);
/* 143 */     setHighestLS(0);
/* 144 */     setMvp(0);
/* 145 */     setKills(0);
/* 146 */     setDeaths(0);
/*     */   }
/*     */   
/*     */   public void win(double eloMultiplier) {
/* 150 */     if (Boolean.parseBoolean(Config.getValue("levels-enabled"))) {
/* 151 */       updateXP(Integer.parseInt(Config.getValue("play-xp")), Integer.parseInt(Config.getValue("clanxp-play")));
/* 152 */       updateXP(Integer.parseInt(Config.getValue("win-xp")), Integer.parseInt(Config.getValue("clanxp-win")));
/*     */     } 
/*     */     
/* 155 */     setWins(this.wins + 1);
/* 156 */     setWinStreak(this.winStreak + 1);
/* 157 */     setElo(this.elo = (int)(this.elo + getRank().getWinElo() * eloMultiplier));
/*     */     
/* 159 */     if (this.peakElo < this.elo) {
/* 160 */       setPeakElo(this.elo);
/*     */     }
/*     */     
/* 163 */     if (this.lossStreak > 0) {
/* 164 */       setLossStreak(0);
/*     */     }
/*     */     
/* 167 */     if (this.highestWS < this.winStreak) {
/* 168 */       setHighestWS(this.winStreak);
/*     */     }
/*     */   }
/*     */   
/*     */   public void lose(double eloMultiplier) {
/* 173 */     if (Boolean.parseBoolean(Config.getValue("levels-enabled"))) {
/* 174 */       updateXP(Integer.parseInt(Config.getValue("play-xp")), Integer.parseInt(Config.getValue("clanxp-play")));
/*     */     }
/*     */     
/* 177 */     setLosses(this.losses + 1);
/* 178 */     setLossStreak(this.lossStreak + 1);
/*     */     
/* 180 */     if (this.elo - getRank().getLoseElo() * eloMultiplier > 0.0D) {
/* 181 */       setElo(this.elo = (int)(this.elo - getRank().getLoseElo() * eloMultiplier));
/*     */     } else {
/*     */       
/* 184 */       setElo(0);
/*     */     } 
/*     */     
/* 187 */     if (this.winStreak > 0) {
/* 188 */       setWinStreak(0);
/*     */     }
/*     */     
/* 191 */     if (this.highestLS < this.lossStreak) {
/* 192 */       setHighestLS(this.lossStreak);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateXP(int addXp, int addClanXP) {
/* 197 */     setXp(this.xp += addXp);
/*     */     
/* 199 */     for (int i = this.level.getLevel() + 1; i < LevelCache.getLevels().size(); i++) {
/* 200 */       if (this.xp >= LevelCache.getLevel(i).getNeededXP()) {
/* 201 */         setLevel(LevelCache.getLevel(i));
/*     */         
/* 203 */         for (String s : LevelCache.getLevel(i).getRewards()) {
/* 204 */           if (s.startsWith("GOLD")) {
/* 205 */             setGold(this.gold += Integer.parseInt(s.split("=")[1]));
/*     */           }
/*     */         } 
/*     */         
/* 209 */         Embed embed = new Embed(EmbedType.SUCCESS, "LEVEL UP", "You have leveled up to `LEVEL " + i + "` 🎉", 1);
/* 210 */         RankedBot.getGuild().getTextChannelById(Config.getValue("alerts-channel")).sendMessage("<@" + this.ID + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     if (ClanCache.getClan(this) != null) {
/* 217 */       Clan clan = ClanCache.getClan(this);
/* 218 */       clan.setXp(clan.getXp() + addClanXP);
/*     */       
/* 220 */       for (int j = clan.getLevel().getLevel() + 1; j < ClanLevelCache.getClanLevels().size(); j++) {
/* 221 */         if (clan.getXp() >= ClanLevelCache.getLevel(j).getNeededXP()) {
/* 222 */           clan.setLevel(ClanLevelCache.getLevel(j));
/*     */           
/* 224 */           Embed embed = new Embed(EmbedType.SUCCESS, "CLAN LEVEL UP", "Clan " + clan.getName() + " has leveled up to `LEVEL " + j + "` 🎉", 1);
/* 225 */           RankedBot.getGuild().getTextChannelById(Config.getValue("alerts-channel")).sendMessage("<@" + clan.getLeader().getID() + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPlacement(Statistic statistic) {
/* 235 */     List<String> lb = Leaderboard.getLeaderboard(statistic);
/*     */     
/* 237 */     for (String s : lb) {
/* 238 */       if (s.startsWith(this.ID)) {
/* 239 */         return lb.indexOf(s) + 1;
/*     */       }
/*     */     } 
/*     */     
/* 243 */     return 0;
/*     */   }
/*     */   
/*     */   public Rank getRank() {
/* 247 */     for (Rank r : RankCache.getRanks().values()) {
/* 248 */       if (this.elo >= r.getStartingElo() && this.elo <= r.getEndingElo()) {
/* 249 */         return r;
/*     */       }
/*     */     } 
/*     */     
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ban(LocalDateTime time, String reason) {
/* 258 */     if (this.isBanned) {
/* 259 */       return false;
/*     */     }
/*     */     
/* 262 */     setBanned(true);
/*     */     
/* 264 */     setBannedTill(time);
/* 265 */     setBanReason(reason);
/*     */     
/* 267 */     fix();
/*     */     
/* 269 */     return true;
/*     */   }
/*     */   
/*     */   public void unban() {
/* 273 */     setBanned(false);
/* 274 */     setBannedTill(null);
/* 275 */     setBanReason(null);
/* 276 */     fix();
/*     */   }
/*     */   
/*     */   public void joinParty(Party party) {
/* 280 */     party.getMembers().add(this);
/* 281 */     party.getInvitedPlayers().remove(this);
/*     */   }
/*     */   
/*     */   public double getStatistic(Statistic s) {
/* 285 */     if (s == Statistic.ELO) {
/* 286 */       return this.elo;
/*     */     }
/* 288 */     if (s == Statistic.PEAKELO) {
/* 289 */       return this.peakElo;
/*     */     }
/*     */     
/* 292 */     if (s == Statistic.WINS) {
/* 293 */       return this.wins;
/*     */     }
/*     */     
/* 296 */     if (s == Statistic.LOSSES) {
/* 297 */       return this.losses;
/*     */     }
/*     */     
/* 300 */     if (s == Statistic.WINSTREAK) {
/* 301 */       return this.winStreak;
/*     */     }
/*     */     
/* 304 */     if (s == Statistic.HIGHESTWS) {
/* 305 */       return this.highestWS;
/*     */     }
/*     */     
/* 308 */     if (s == Statistic.HIGHESTLS) {
/* 309 */       return this.highestLS;
/*     */     }
/*     */     
/* 312 */     if (s == Statistic.MVP) {
/* 313 */       return this.mvp;
/*     */     }
/*     */     
/* 316 */     if (s == Statistic.KILLS) {
/* 317 */       return this.kills;
/*     */     }
/*     */     
/* 320 */     if (s == Statistic.DEATHS) {
/* 321 */       return this.deaths;
/*     */     }
/*     */     
/* 324 */     if (s == Statistic.STRIKES) {
/* 325 */       return this.strikes;
/*     */     }
/*     */     
/* 328 */     if (s == Statistic.SCORED) {
/* 329 */       return this.scored;
/*     */     }
/*     */     
/* 332 */     if (s == Statistic.GOLD) {
/* 333 */       return this.gold;
/*     */     }
/*     */     
/* 336 */     if (s == Statistic.LEVEL) {
/* 337 */       return this.level.getLevel();
/*     */     }
/*     */     
/* 340 */     if (s == Statistic.XP) {
/* 341 */       return this.xp;
/*     */     }
/*     */     
/* 344 */     if (s == Statistic.WLR) {
/* 345 */       double templosses = getLosses();
/* 346 */       if (getLosses() == 0) {
/* 347 */         templosses = 1.0D;
/*     */       }
/* 349 */       return getWins() / templosses;
/*     */     } 
/*     */     
/* 352 */     if (s == Statistic.KDR) {
/* 353 */       double tempdeaths = getDeaths();
/* 354 */       if (getDeaths() == 0) {
/* 355 */         tempdeaths = 1.0D;
/*     */       }
/* 357 */       return getKills() / tempdeaths;
/*     */     } 
/*     */     
/* 360 */     if (s == Statistic.GAMES) {
/* 361 */       return (getWins() + getLosses());
/*     */     }
/*     */     
/* 364 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void leaveParty(Party party) {
/* 368 */     party.getMembers().remove(this);
/*     */   }
/*     */   
/*     */   public static boolean isRegistered(String ID) {
/* 372 */     return SQLPlayerManager.isRegistered(ID);
/*     */   }
/*     */   
/*     */   public String getID() {
/* 376 */     return this.ID;
/*     */   }
/*     */   
/*     */   public void setID(String ID) {
/* 380 */     this.ID = ID;
/*     */   }
/*     */   
/*     */   public String getIgn() {
/* 384 */     return this.ign;
/*     */   }
/*     */   
/*     */   public void setIgn(String ign) {
/* 388 */     this.ign = ign;
/* 389 */     SQLPlayerManager.updateIgn(this.ID);
/*     */   }
/*     */   
/*     */   public int getElo() {
/* 393 */     return this.elo;
/*     */   }
/*     */   
/*     */   public void setElo(int elo) {
/* 397 */     this.elo = elo;
/*     */     
/* 399 */     if (this.peakElo < elo) {
/* 400 */       setPeakElo(elo);
/*     */     }
/*     */     
/* 403 */     SQLPlayerManager.updateElo(this.ID);
/*     */   }
/*     */   
/*     */   public int getPeakElo() {
/* 407 */     return this.peakElo;
/*     */   }
/*     */   
/*     */   public void setPeakElo(int peakElo) {
/* 411 */     this.peakElo = peakElo;
/* 412 */     SQLPlayerManager.updatePeakElo(this.ID);
/*     */   }
/*     */   
/*     */   public int getWins() {
/* 416 */     return this.wins;
/*     */   }
/*     */   
/*     */   public void setWins(int wins) {
/* 420 */     this.wins = wins;
/* 421 */     SQLPlayerManager.updateWins(this.ID);
/*     */   }
/*     */   
/*     */   public int getLosses() {
/* 425 */     return this.losses;
/*     */   }
/*     */   
/*     */   public void setLosses(int losses) {
/* 429 */     this.losses = losses;
/* 430 */     SQLPlayerManager.updateLosses(this.ID);
/*     */   }
/*     */   
/*     */   public int getWinStreak() {
/* 434 */     return this.winStreak;
/*     */   }
/*     */   
/*     */   public void setWinStreak(int winStreak) {
/* 438 */     this.winStreak = winStreak;
/* 439 */     SQLPlayerManager.updateWinStreak(this.ID);
/*     */   }
/*     */   
/*     */   public int getLossStreak() {
/* 443 */     return this.lossStreak;
/*     */   }
/*     */   
/*     */   public void setLossStreak(int lossStreak) {
/* 447 */     this.lossStreak = lossStreak;
/* 448 */     SQLPlayerManager.updateLossStreak(this.ID);
/*     */   }
/*     */   
/*     */   public int getHighestWS() {
/* 452 */     return this.highestWS;
/*     */   }
/*     */   
/*     */   public void setHighestWS(int highestWS) {
/* 456 */     this.highestWS = highestWS;
/* 457 */     SQLPlayerManager.updateHighestWS(this.ID);
/*     */   }
/*     */   
/*     */   public int getHighestLS() {
/* 461 */     return this.highestLS;
/*     */   }
/*     */   
/*     */   public void setHighestLS(int highestLS) {
/* 465 */     this.highestLS = highestLS;
/* 466 */     SQLPlayerManager.updateHighestLS(this.ID);
/*     */   }
/*     */   
/*     */   public int getMvp() {
/* 470 */     return this.mvp;
/*     */   }
/*     */   
/*     */   public void setMvp(int mvp) {
/* 474 */     this.mvp = mvp;
/* 475 */     SQLPlayerManager.updateMvp(this.ID);
/*     */   }
/*     */   
/*     */   public int getStrikes() {
/* 479 */     return this.strikes;
/*     */   }
/*     */   
/*     */   public void setStrikes(int strikes) {
/* 483 */     this.strikes = strikes;
/* 484 */     SQLPlayerManager.updateStrikes(this.ID);
/*     */   }
/*     */   
/*     */   public int getScored() {
/* 488 */     return this.scored;
/*     */   }
/*     */   
/*     */   public void setScored(int scored) {
/* 492 */     this.scored = scored;
/* 493 */     SQLPlayerManager.updateScored(this.ID);
/*     */   }
/*     */   
/*     */   public int getKills() {
/* 497 */     return this.kills;
/*     */   }
/*     */   
/*     */   public void setKills(int kills) {
/* 501 */     this.kills = kills;
/* 502 */     SQLPlayerManager.updateKills(this.ID);
/*     */   }
/*     */   
/*     */   public int getDeaths() {
/* 506 */     return this.deaths;
/*     */   }
/*     */   
/*     */   public void setDeaths(int deaths) {
/* 510 */     this.deaths = deaths;
/* 511 */     SQLPlayerManager.updateDeaths(this.ID);
/*     */   }
/*     */   
/*     */   public int getGold() {
/* 515 */     return this.gold;
/*     */   }
/*     */   
/*     */   public void setGold(int gold) {
/* 519 */     this.gold = gold;
/* 520 */     SQLPlayerManager.updateGold(this.ID);
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 524 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 528 */     this.level = level;
/* 529 */     SQLPlayerManager.updateLevel(this.ID);
/*     */   }
/*     */   
/*     */   public int getXp() {
/* 533 */     return this.xp;
/*     */   }
/*     */   
/*     */   public void setXp(int xp) {
/* 537 */     this.xp = xp;
/* 538 */     SQLPlayerManager.updateXP(this.ID);
/*     */   }
/*     */   
/*     */   public Theme getTheme() {
/* 542 */     return this.theme;
/*     */   }
/*     */   
/*     */   public void setTheme(Theme theme) {
/* 546 */     this.theme = theme;
/* 547 */     SQLPlayerManager.updateTheme(this.ID);
/*     */   }
/*     */   
/*     */   public ArrayList<Theme> getOwnedThemes() {
/* 551 */     return this.ownedThemes;
/*     */   }
/*     */   
/*     */   public void giveTheme(Theme theme) {
/* 555 */     this.ownedThemes.add(theme);
/* 556 */     SQLPlayerManager.updateOwnedThemes(this.ID);
/*     */   }
/*     */   
/*     */   public void removeTheme(Theme theme) {
/* 560 */     this.ownedThemes.remove(theme);
/* 561 */     SQLPlayerManager.updateOwnedThemes(this.ID);
/*     */   }
/*     */   
/*     */   public boolean isBanned() {
/* 565 */     return this.isBanned;
/*     */   }
/*     */   
/*     */   public void setBanned(boolean banned) {
/* 569 */     this.isBanned = banned;
/* 570 */     SQLPlayerManager.updateIsBanned(this.ID);
/*     */   }
/*     */   
/*     */   public LocalDateTime getBannedTill() {
/* 574 */     return this.bannedTill;
/*     */   }
/*     */   
/*     */   public void setBannedTill(LocalDateTime bannedTill) {
/* 578 */     this.bannedTill = bannedTill;
/* 579 */     SQLPlayerManager.updateBannedTill(this.ID);
/*     */   }
/*     */   
/*     */   public String getBanReason() {
/* 583 */     return this.banReason;
/*     */   }
/*     */   
/*     */   public void setBanReason(String banReason) {
/* 587 */     this.banReason = banReason;
/* 588 */     SQLPlayerManager.updateBanReason(this.ID);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Player.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */