/*     */ package com.kasp.rankedbot;
/*     */ import com.kasp.rankedbot.commands.moderation.UnbanTask;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.database.SQLClanManager;
/*     */ import com.kasp.rankedbot.database.SQLGameManager;
/*     */ import com.kasp.rankedbot.database.SQLPlayerManager;
/*     */ import com.kasp.rankedbot.database.SQLTableManager;
/*     */ import com.kasp.rankedbot.database.SQLUtilsManager;
/*     */ import com.kasp.rankedbot.database.SQLite;
/*     */ import com.kasp.rankedbot.instance.Clan;
/*     */ import com.kasp.rankedbot.instance.ClanLevel;
/*     */ import com.kasp.rankedbot.instance.Game;
/*     */ import com.kasp.rankedbot.instance.GameMap;
/*     */ import com.kasp.rankedbot.instance.Level;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.Queue;
/*     */ import com.kasp.rankedbot.instance.Rank;
/*     */ import com.kasp.rankedbot.instance.Theme;
/*     */ import com.kasp.rankedbot.levelsfile.Levels;
/*     */ import com.kasp.rankedbot.listener.PartyInviteButton;
/*     */ import com.kasp.rankedbot.listener.QueueJoin;
/*     */ import java.io.File;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import net.dv8tion.jda.api.JDABuilder;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.requests.GatewayIntent;
/*     */ import net.dv8tion.jda.api.utils.MemberCachePolicy;
/*     */ 
/*     */ public class RankedBot {
/*  34 */   public static String version = "1.1.0";
/*     */   public static JDA jda;
/*     */   public static Guild guild;
/*     */   
/*     */   public static void main(String[] args) {
/*  39 */     (new File("RankedBot/fonts")).mkdirs();
/*  40 */     (new File("RankedBot/themes")).mkdirs();
/*     */     
/*  42 */     SQLite.connect();
/*  43 */     SQLTableManager.createPlayersTable();
/*  44 */     SQLTableManager.createRanksTable();
/*  45 */     SQLTableManager.createGamesTable();
/*  46 */     SQLTableManager.createMapsTable();
/*  47 */     SQLTableManager.createQueuesTable();
/*  48 */     SQLTableManager.createClansTable();
/*     */     
/*  50 */     Config.loadConfig();
/*  51 */     Perms.loadPerms();
/*  52 */     Msg.loadMsg();
/*  53 */     Levels.loadLevels();
/*  54 */     Levels.loadClanLevels();
/*     */ 
/*     */ 
/*     */     
/*  58 */     if (Config.getValue("token") == null) {
/*  59 */       System.out.println("[!] Please set your token in config.yml");
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     JDABuilder jdaBuilder = JDABuilder.createDefault(Config.getValue("token"));
/*  64 */     jdaBuilder.setStatus(OnlineStatus.valueOf(Config.getValue("status").toUpperCase()));
/*  65 */     jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
/*  66 */     jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
/*  67 */     jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, new GatewayIntent[0]);
/*  68 */     jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[0]);
/*  69 */     jdaBuilder.addEventListeners(new Object[] { new CommandManager(), new PagesEvents(), new QueueJoin(), new ServerJoin(), new PartyInviteButton() });
/*     */     try {
/*  71 */       jda = jdaBuilder.build();
/*  72 */     } catch (LoginException e) {
/*  73 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  76 */     System.out.println("\n[!] Finishing up... this might take around 10 seconds\n");
/*     */ 
/*     */     
/*  79 */     TimerTask task = new TimerTask()
/*     */       {
/*     */         public void run() {
/*  82 */           if (RankedBot.jda.getGuilds().size() == 0) {
/*  83 */             System.out.println("[!] Please invite this bot to your server first");
/*     */             
/*     */             return;
/*     */           } 
/*  87 */           RankedBot.guild = RankedBot.jda.getGuilds().get(0);
/*     */           int i;
/*  89 */           for (i = 1; i <= SQLUtilsManager.getRankSize(); i++) {
/*  90 */             ResultSet resultSet = SQLite.queryData("SELECT discordID FROM ranks where _ID='" + i + "'");
/*     */             try {
/*  92 */               new Rank(resultSet.getString(1));
/*  93 */             } catch (SQLException e) {
/*  94 */               e.printStackTrace();
/*  95 */               System.out.println("[!] a rank could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/*  99 */           for (i = 1; i <= SQLUtilsManager.getMapSize(); i++) {
/* 100 */             ResultSet resultSet = SQLite.queryData("SELECT name FROM maps where _ID='" + i + "'");
/*     */             try {
/* 102 */               new GameMap(resultSet.getString(1));
/* 103 */             } catch (SQLException e) {
/* 104 */               e.printStackTrace();
/* 105 */               System.out.println("[!] a map could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/* 109 */           for (i = 1; i <= SQLUtilsManager.getQueueSize(); i++) {
/* 110 */             ResultSet resultSet = SQLite.queryData("SELECT discordID FROM queues where _ID='" + i + "'");
/*     */             try {
/* 112 */               new Queue(resultSet.getString(1));
/* 113 */             } catch (SQLException e) {
/* 114 */               e.printStackTrace();
/* 115 */               System.out.println("[!] a queue could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/* 119 */           if (((new File("RankedBot/themes")).listFiles()).length > 0) {
/* 120 */             for (File f : (new File("RankedBot/themes")).listFiles()) {
/* 121 */               new Theme(f.getName().replaceAll(".png", ""));
/*     */             }
/*     */           }
/*     */           
/* 125 */           for (i = 0; i <= Integer.parseInt((String)Levels.levelsData.get("total-levels")); i++) {
/* 126 */             new Level(i);
/*     */           }
/*     */           
/* 129 */           for (i = 0; i <= Integer.parseInt((String)Levels.clanLevelsData.get("total-levels")); i++) {
/* 130 */             new ClanLevel(i);
/*     */           }
/*     */           
/* 133 */           for (i = 1; i <= SQLPlayerManager.getPlayerSize(); i++) {
/* 134 */             ResultSet resultSet = SQLite.queryData("SELECT discordID FROM players where _ID='" + i + "'");
/*     */             try {
/* 136 */               new Player(resultSet.getString(1));
/* 137 */             } catch (SQLException e) {
/* 138 */               e.printStackTrace();
/* 139 */               System.out.println("[!] a player could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/* 143 */           for (i = 1; i <= SQLGameManager.getGameSize(); i++) {
/* 144 */             ResultSet resultSet = SQLite.queryData("SELECT number FROM games where _ID='" + i + "'");
/*     */             try {
/* 146 */               new Game(resultSet.getInt(1));
/* 147 */             } catch (SQLException e) {
/* 148 */               e.printStackTrace();
/* 149 */               System.out.println("[!] a game could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/* 153 */           for (i = 1; i <= SQLClanManager.getClanSize(); i++) {
/* 154 */             ResultSet resultSet = SQLite.queryData("SELECT name FROM clans where _ID='" + i + "'");
/*     */             try {
/* 156 */               new Clan(resultSet.getString(1));
/* 157 */             } catch (SQLException e) {
/* 158 */               e.printStackTrace();
/* 159 */               System.out.println("[!] a clan could not be loaded! Please make a bug report on support discord asap");
/*     */             } 
/*     */           } 
/*     */           
/* 163 */           System.out.println("-------------------------------");
/*     */           
/* 165 */           System.out.println("RankedBot has been successfully enabled!");
/* 166 */           System.out.println("NOTE: this bot can only be in 1 server, otherwise it'll break");
/* 167 */           System.out.println("don't forget to configure config.yml and permissions.yml before using it\nYou can also edit messages.yml (optional)");
/*     */           
/* 169 */           System.out.println("-------------------------------");
/*     */         }
/*     */       };
/*     */     
/* 173 */     (new Timer()).schedule(task, 10000L);
/*     */     
/* 175 */     TimerTask unbanTask = new TimerTask()
/*     */       {
/*     */         public void run() {
/* 178 */           UnbanTask.checkAndUnbanPlayers();
/*     */         }
/*     */       };
/*     */     
/* 182 */     (new Timer()).schedule(unbanTask, 3600000L, 3600000L);
/*     */   }
/*     */   
/*     */   public static Guild getGuild() {
/* 186 */     return guild;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\RankedBot.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */