/*     */ package com.kasp.rankedbot.commands.clan;
/*     */ import com.kasp.rankedbot.CommandSubsystem;
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.commands.Command;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.instance.Clan;
/*     */ import com.kasp.rankedbot.instance.Embed;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import com.kasp.rankedbot.messages.Msg;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import org.jsoup.nodes.Document;
/*     */ 
/*     */ public class ClanStatsCmd extends Command {
/*     */   public ClanStatsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/*  34 */     super(command, usage, aliases, description, subsystem);
/*     */   }
/*     */   
/*     */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*     */     String clanName;
/*  39 */     if (args.length > 2) {
/*  40 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/*  41 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  46 */     if (args.length == 1) {
/*  47 */       if (ClanCache.getClan(PlayerCache.getPlayer(sender.getId())) != null) {
/*  48 */         clanName = ClanCache.getClan(PlayerCache.getPlayer(sender.getId())).getName();
/*     */       } else {
/*     */         
/*  51 */         Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-clan"), 1);
/*  52 */         msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */         return;
/*     */       } 
/*     */     } else {
/*  56 */       clanName = args[1];
/*     */     } 
/*     */     
/*  59 */     if (ClanCache.getClan(clanName) == null) {
/*  60 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Clan", Msg.getMsg("clan-doesnt-exist"), 1);
/*  61 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  65 */     Clan clan = ClanCache.getClan(clanName);
/*     */     
/*  67 */     Player clanLeader = clan.getLeader();
/*     */     
/*  69 */     if ((new File("RankedBot/clans/theme.png")).exists()) {
/*  70 */       String skinlink; Document document = null;
/*     */       try {
/*  72 */         document = Jsoup.connect("https://api.mineatar.io/uuid/" + clanLeader.getIgn()).get();
/*  73 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/*  76 */       if (document != null) {
/*  77 */         String UUID = document.body().text();
/*  78 */         skinlink = "https://visage.surgeplay.com/full/" + Config.getValue("leaderskin-size") + "/" + UUID;
/*     */       } else {
/*     */         
/*  81 */         skinlink = "https://visage.surgeplay.com/full/" + Config.getValue("leaderskin-size") + "/069a79f4-44e9-4726-a5be-fca90e38aaf5";
/*     */       } 
/*     */       try {
/*     */         BufferedImage image, icon, skin;
/*  85 */         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  86 */         ge.registerFont(Font.createFont(0, new File("RankedBot/fonts/stats.otf")));
/*     */ 
/*     */         
/*  89 */         if ((new File("RankedBot/clans/" + clan.getName() + "/theme.png")).exists()) {
/*  90 */           image = ImageIO.read((new File("RankedBot/clans/" + clan.getName() + "/theme.png")).toURI().toURL());
/*     */         } else {
/*     */           
/*  93 */           image = ImageIO.read((new File("RankedBot/clans/theme.png")).toURI().toURL());
/*     */         } 
/*     */ 
/*     */         
/*  97 */         if ((new File("RankedBot/clans/" + clan.getName() + "/icon.png")).exists()) {
/*  98 */           icon = ImageIO.read((new File("RankedBot/clans/" + clan.getName() + "/icon.png")).toURI().toURL());
/*     */         } else {
/*     */           
/* 101 */           icon = ImageIO.read((new File("RankedBot/clans/icon.png")).toURI().toURL());
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 106 */           skin = ImageIO.read(new URL(skinlink));
/* 107 */         } catch (Exception e) {
/* 108 */           Embed embed = new Embed(EmbedType.ERROR, "Something went wrong...", "Please try executing this command again", 1);
/* 109 */           msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           
/*     */           return;
/*     */         } 
/* 113 */         Graphics2D gfx = (Graphics2D)image.getGraphics();
/*     */         
/* 115 */         drawText(gfx, "reputation", "" + clan.getReputation());
/* 116 */         drawText(gfx, "name", clan.getName());
/* 117 */         drawText(gfx, "description", clan.getDescription());
/*     */ 
/*     */         
/* 120 */         gfx.drawImage(icon, Integer.parseInt(Config.getValue("icon-pixels").split(",")[0]), Integer.parseInt(Config.getValue("icon-pixels").split(",")[1]), (ImageObserver)null);
/*     */         
/* 122 */         List<Clan> clanLB = new ArrayList<>(Leaderboard.getClansLeaderboard());
/*     */         
/* 124 */         drawText(gfx, "ranking", "#" + clanLB.indexOf(clan) + 1);
/* 125 */         drawText(gfx, "xp", "" + clan.getXp());
/* 126 */         drawText(gfx, "level", "" + clan.getLevel().getLevel());
/*     */         
/* 128 */         int allElo = 0;
/* 129 */         int allGold = 0;
/* 130 */         for (Player p : clan.getMembers()) {
/* 131 */           allElo += p.getElo();
/* 132 */           allGold += p.getGold();
/*     */         } 
/*     */         
/* 135 */         drawText(gfx, "allelo", "" + allElo);
/* 136 */         drawText(gfx, "allgold", "" + allGold);
/*     */         
/* 138 */         drawText(gfx, "members", "" + clan.getMembers().size() + "/" + clan.getMembers().size());
/* 139 */         drawText(gfx, "invited", "" + clan.getInvitedPlayers().size());
/*     */         
/* 141 */         drawText(gfx, "cwplayed", "" + clan.getWins() + clan.getLosses());
/* 142 */         drawText(gfx, "cwwins", "" + clan.getWins());
/* 143 */         drawText(gfx, "cwlosses", "" + clan.getLosses());
/*     */         
/* 145 */         double tempLosses = clan.getLosses();
/* 146 */         if (clan.getLosses() < 1) {
/* 147 */           tempLosses = 1.0D;
/*     */         }
/*     */         
/* 150 */         DecimalFormat f = new DecimalFormat("#.##");
/*     */         
/* 152 */         drawText(gfx, "cwwlr", f.format(clan.getWins() / tempLosses));
/*     */ 
/*     */         
/* 155 */         drawText(gfx, "leaderign", clanLeader.getIgn());
/*     */ 
/*     */         
/* 158 */         gfx.drawImage(skin, Integer.parseInt(Config.getValue("leaderskin-pixels").split(",")[0]), Integer.parseInt(Config.getValue("leaderskin-pixels").split(",")[1]), (ImageObserver)null);
/*     */ 
/*     */         
/* 161 */         gfx.dispose();
/*     */         
/* 163 */         ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 164 */         ImageIO.write(image, "png", stream);
/*     */         
/* 166 */         channel.sendFile(stream.toByteArray(), "stats.png", new net.dv8tion.jda.api.utils.AttachmentOption[0]).queue();
/* 167 */       } catch (IOException e) {
/* 168 */         e.printStackTrace();
/* 169 */       } catch (FontFormatException e) {
/* 170 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawText(Graphics2D gfx, String configString, String value) {
/* 176 */     gfx.setFont(new Font(Config.getValue("s-text-font"), 0, Integer.parseInt(Config.getValue(configString + "-size"))));
/* 177 */     gfx.setColor(new Color(Integer.parseInt(Config.getValue(configString + "-color").split(",")[0]), 
/* 178 */           Integer.parseInt(Config.getValue(configString + "-color").split(",")[1]), 
/* 179 */           Integer.parseInt(Config.getValue(configString + "-color").split(",")[2])));
/* 180 */     gfx.drawString(value, Integer.parseInt(Config.getValue(configString + "-pixels").split(",")[0]), Integer.parseInt(Config.getValue(configString + "-pixels").split(",")[1]));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanStatsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */