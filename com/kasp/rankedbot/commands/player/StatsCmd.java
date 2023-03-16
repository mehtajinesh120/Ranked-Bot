/*     */ package com.kasp.rankedbot.commands.player;
/*     */ import com.kasp.rankedbot.CommandSubsystem;
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.Statistic;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.instance.Embed;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.Theme;
/*     */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.DecimalFormat;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import org.jsoup.nodes.Document;
/*     */ 
/*     */ public class StatsCmd extends Command {
/*     */   public StatsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/*  31 */     super(command, usage, aliases, description, subsystem);
/*     */   }
/*     */   
/*     */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*     */     String ID;
/*  36 */     if (args.length > 2) {
/*  37 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/*  38 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  43 */     if (args.length == 1) {
/*  44 */       ID = sender.getId();
/*     */     }
/*  46 */     else if (args[1].equals("full")) {
/*  47 */       ID = sender.getId();
/*     */     } else {
/*  49 */       ID = args[1].replaceAll("[^0-9]", "");
/*     */     } 
/*     */     
/*  52 */     if (PlayerCache.getPlayer(ID) == null) {
/*  53 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Player", Msg.getMsg("invalid-player"), 1);
/*  54 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  58 */     Player player = PlayerCache.getPlayer(ID);
/*     */     
/*  60 */     DecimalFormat f = new DecimalFormat("#.##");
/*     */     
/*  62 */     double templosses = 1.0D;
/*  63 */     if (player.getLosses() > 0) {
/*  64 */       templosses = player.getLosses();
/*     */     }
/*  66 */     int games = player.getWins() + player.getLosses();
/*     */     
/*  68 */     if (args.length == 2 && args[1].equals("full")) {
/*  69 */       msg.replyEmbeds(statsFulLEmbed(player, games, templosses).build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  73 */     if (Boolean.parseBoolean(Config.getValue("s-enabled")) && 
/*  74 */       ThemeCache.getTheme("default") != null) {
/*  75 */       String skinlink; Document document = null;
/*     */       try {
/*  77 */         document = Jsoup.connect("https://api.mineatar.io/uuid/" + player.getIgn()).get();
/*  78 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/*  81 */       if (document != null) {
/*  82 */         String UUID = document.body().text();
/*  83 */         skinlink = "https://visage.surgeplay.com/full/" + Config.getValue("skin-size") + "/" + UUID;
/*     */       } else {
/*     */         
/*  86 */         skinlink = "https://visage.surgeplay.com/full/" + Config.getValue("skin-size") + "/069a79f4-44e9-4726-a5be-fca90e38aaf5";
/*     */       } 
/*     */       try {
/*     */         BufferedImage skin;
/*  90 */         player.fix();
/*     */         
/*  92 */         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  93 */         ge.registerFont(Font.createFont(0, new File("RankedBot/fonts/stats.otf")));
/*     */         
/*  95 */         BufferedImage image = ImageIO.read((new File("RankedBot/themes/" + player.getTheme().getName() + ".png")).toURI().toURL());
/*     */ 
/*     */         
/*     */         try {
/*  99 */           skin = ImageIO.read(new URL(skinlink));
/* 100 */         } catch (Exception e) {
/* 101 */           Embed embed = new Embed(EmbedType.ERROR, "Something went wrong...", "Please try executing this command again", 1);
/* 102 */           msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           
/*     */           return;
/*     */         } 
/* 106 */         Graphics2D gfx = (Graphics2D)image.getGraphics();
/*     */         
/* 108 */         gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("ign-size"))));
/* 109 */         gfx.setColor(new Color(Integer.parseInt(Config.getValue("ign-color").split(",")[0]), 
/* 110 */               Integer.parseInt(Config.getValue("ign-color").split(",")[1]), 
/* 111 */               Integer.parseInt(Config.getValue("ign-color").split(",")[2])));
/* 112 */         gfx.drawString(player.getIgn(), Integer.parseInt(Config.getValue("ign-pixels").split(",")[0]), Integer.parseInt(Config.getValue("ign-pixels").split(",")[1]));
/*     */         
/* 114 */         for (Statistic s : Statistic.values()) {
/* 115 */           if (s != Statistic.ID) {
/*     */ 
/*     */             
/* 118 */             gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("" + s + "-size"))));
/* 119 */             gfx.setColor(new Color(Integer.parseInt(Config.getValue("" + s + "-color").split(",")[0]), 
/* 120 */                   Integer.parseInt(Config.getValue("" + s + "-color").split(",")[1]), 
/* 121 */                   Integer.parseInt(Config.getValue("" + s + "-color").split(",")[2])));
/* 122 */             if (s == Statistic.WLR || s == Statistic.KDR) {
/* 123 */               gfx.drawString(f.format(player.getStatistic(s)), Integer.parseInt(Config.getValue("" + s + "-pixels").split(",")[0]), Integer.parseInt(Config.getValue("" + s + "-pixels").split(",")[1]));
/*     */             } else {
/*     */               
/* 126 */               gfx.drawString("" + (int)player.getStatistic(s), Integer.parseInt(Config.getValue("" + s + "-pixels").split(",")[0]), Integer.parseInt(Config.getValue("" + s + "-pixels").split(",")[1]));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 131 */         if (LevelCache.containsLevel(player.getLevel().getLevel() + 1)) {
/* 132 */           gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("needed-xp-size"))));
/* 133 */           gfx.setColor(new Color(Integer.parseInt(Config.getValue("needed-xp-color").split(",")[0]), 
/* 134 */                 Integer.parseInt(Config.getValue("needed-xp-color").split(",")[1]), 
/* 135 */                 Integer.parseInt(Config.getValue("needed-xp-color").split(",")[2])));
/* 136 */           gfx.drawString("" + LevelCache.getLevel(player.getLevel().getLevel() + 1).getNeededXP(), Integer.parseInt(Config.getValue("needed-xp-pixels").split(",")[0]), Integer.parseInt(Config.getValue("needed-xp-pixels").split(",")[1]));
/*     */         } 
/*     */ 
/*     */         
/* 140 */         gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("theme-size"))));
/* 141 */         gfx.setColor(new Color(Integer.parseInt(Config.getValue("theme-color").split(",")[0]), 
/* 142 */               Integer.parseInt(Config.getValue("theme-color").split(",")[1]), 
/* 143 */               Integer.parseInt(Config.getValue("theme-color").split(",")[2])));
/* 144 */         gfx.drawString(player.getTheme().getName(), Integer.parseInt(Config.getValue("theme-pixels").split(",")[0]), Integer.parseInt(Config.getValue("theme-pixels").split(",")[1]));
/*     */ 
/*     */         
/* 147 */         Role role = guild.getRoleById(player.getRank().getID());
/* 148 */         gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("rbw-rank-size"))));
/* 149 */         gfx.setColor(role.getColor());
/* 150 */         gfx.drawString(role.getName(), Integer.parseInt(Config.getValue("rbw-rank-pixels").split(",")[0]), Integer.parseInt(Config.getValue("rbw-rank-pixels").split(",")[1]));
/*     */ 
/*     */         
/* 153 */         if (player.isBanned()) {
/* 154 */           gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("banned-size"))));
/* 155 */           gfx.setColor(new Color(Integer.parseInt(Config.getValue("banned-color").split(",")[0]), 
/* 156 */                 Integer.parseInt(Config.getValue("banned-color").split(",")[1]), 
/* 157 */                 Integer.parseInt(Config.getValue("banned-color").split(",")[2])));
/* 158 */           gfx.drawString("BANNED", Integer.parseInt(Config.getValue("banned-pixels").split(",")[0]), Integer.parseInt(Config.getValue("banned-pixels").split(",")[1]));
/*     */         } 
/*     */ 
/*     */         
/* 162 */         if (ClanCache.getClan(player) != null) {
/* 163 */           gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue("clan-size"))));
/* 164 */           gfx.setColor(new Color(Integer.parseInt(Config.getValue("clan-color").split(",")[0]), 
/* 165 */                 Integer.parseInt(Config.getValue("clan-color").split(",")[1]), 
/* 166 */                 Integer.parseInt(Config.getValue("clan-color").split(",")[2])));
/* 167 */           gfx.drawString(ClanCache.getClan(player).getName(), Integer.parseInt(Config.getValue("clan-pixels").split(",")[0]), Integer.parseInt(Config.getValue("clan-pixels").split(",")[1]));
/*     */         } 
/*     */ 
/*     */         
/* 171 */         gfx.drawImage(skin, Integer.parseInt(Config.getValue("skin-pixels").split(",")[0]), Integer.parseInt(Config.getValue("skin-pixels").split(",")[1]), (ImageObserver)null);
/*     */ 
/*     */         
/* 174 */         gfx.dispose();
/*     */         
/* 176 */         ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 177 */         ImageIO.write(image, "png", stream);
/*     */         
/* 179 */         channel.sendFile(stream.toByteArray(), "stats.png", new net.dv8tion.jda.api.utils.AttachmentOption[0]).queue();
/* 180 */       } catch (IOException e) {
/* 181 */         e.printStackTrace();
/* 182 */       } catch (FontFormatException e) {
/* 183 */         throw new RuntimeException(e);
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 190 */     msg.replyEmbeds(statsFulLEmbed(player, games, templosses).build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */   }
/*     */   
/*     */   private Embed statsFulLEmbed(Player player, int games, double templosses) {
/* 194 */     DecimalFormat f = new DecimalFormat("#.##");
/*     */     
/* 196 */     Embed embed = new Embed(EmbedType.DEFAULT, player.getIgn() + "'s Stats", "", 1);
/*     */     
/* 198 */     embed.addField("__General Stats__", "> `Elo` " + player
/* 199 */         .getElo() + " **(#" + player.getPlacement(Statistic.ELO) + ")**\n> ┗ `Peak` " + player
/* 200 */         .getPeakElo() + " **(#" + player.getPlacement(Statistic.PEAKELO) + ")**\n> `Games` " + games + " **(#" + player
/* 201 */         .getPlacement(Statistic.GAMES) + ")**\n> `WLR` " + f
/* 202 */         .format(player.getWins() / templosses) + " **(#" + player.getPlacement(Statistic.WLR) + ")**\n> `Mvp` " + player
/* 203 */         .getMvp() + " **(#" + player.getPlacement(Statistic.MVP) + ")**\n> `Strikes` " + player
/* 204 */         .getStrikes() + " **(#" + player.getPlacement(Statistic.STRIKES) + ")**\n> `Scored` " + player
/* 205 */         .getScored() + " **(#" + player.getPlacement(Statistic.SCORED) + ")**", false);
/*     */     
/* 207 */     embed.addField("__Games Stats__", "> **`Wins`** " + player
/* 208 */         .getWins() + " **(#" + player.getPlacement(Statistic.WINS) + ")**\n> `Winstreak` " + player
/* 209 */         .getWinStreak() + " **(#" + player.getPlacement(Statistic.WINSTREAK) + ")**\n> ┗ `Highest` " + player
/* 210 */         .getHighestWS() + " **(#" + player.getPlacement(Statistic.HIGHESTWS) + ")**\n> **`Losses`** " + player
/* 211 */         .getLosses() + " **(#" + player.getPlacement(Statistic.LOSSES) + ")**\n> `Losestreak` " + player
/* 212 */         .getLossStreak() + " **(#" + player.getPlacement(Statistic.LOSSSTREAK) + ")**\n> ┗ `Highest` " + player
/* 213 */         .getHighestLS() + " **(#" + player.getPlacement(Statistic.HIGHESTLS) + ")**", false);
/*     */     
/* 215 */     embed.addField("__K/D Stats__", "> `Kills` " + player
/* 216 */         .getKills() + " **(#" + player.getPlacement(Statistic.KILLS) + ")**\n> `Deaths` " + player
/* 217 */         .getDeaths() + " **(#" + player.getPlacement(Statistic.DEATHS) + ")**\n> `KDR` " + player
/* 218 */         .getHighestWS() + " **(#" + player.getPlacement(Statistic.KDR) + ")**", false);
/*     */     
/* 220 */     if (player.getOwnedThemes().get(0) == null) {
/* 221 */       embed.addField("__Other Stats__", "> `Gold` " + player
/* 222 */           .getGold() + " **(#" + player.getPlacement(Statistic.GOLD) + ")**\n> `Level` " + player
/* 223 */           .getLevel().getLevel() + " **(#" + player.getPlacement(Statistic.LEVEL) + ")**\n> `Xp` " + player
/* 224 */           .getXp() + " **(#" + player.getPlacement(Statistic.XP) + ")**", false);
/*     */     } else {
/*     */       
/* 227 */       StringBuilder themes = new StringBuilder();
/* 228 */       for (Theme t : player.getOwnedThemes()) {
/* 229 */         themes.append(t.getName() + " ");
/*     */       }
/*     */       
/* 232 */       embed.addField("__Other Stats__", "> `Gold` " + player
/* 233 */           .getGold() + " **(#" + player.getPlacement(Statistic.GOLD) + ")**\n> `Level` " + player
/* 234 */           .getLevel().getLevel() + " **(#" + player.getPlacement(Statistic.LEVEL) + ")**\n> `Xp` " + player
/* 235 */           .getXp() + " **(#" + player.getPlacement(Statistic.XP) + ")**\n> `Selected theme` " + player
/* 236 */           .getTheme().getName() + "\n> `Owned themes` " + themes, false);
/*     */     } 
/*     */ 
/*     */     
/* 240 */     return embed;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\StatsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */