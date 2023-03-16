/*     */ package com.kasp.rankedbot.commands.clan;
/*     */ 
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
/*     */ import java.util.Arrays;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ 
/*     */ public class ClanSettingsCmd
/*     */   extends Command {
/*     */   public ClanSettingsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/*  22 */     super(command, usage, aliases, description, subsystem);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*  27 */     String[] settings = { "private", "eloreq", "description", "icon", "theme" };
/*  28 */     String[] settingsvalue = { "true/false", "number", "text", "attached 135x135 image", "attached 960x540 image" };
/*  29 */     String[] settingsdesc = { "make your clan private - only allow invited players to join\nor public - anyone will be able to join your clan", "change the required min. elo to join your clan - only works if your clan is set to public", "change the description of your clan", "change the icon of your clan", "change the =cstats theme of your clan" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  35 */     if (args.length < 2) {
/*  36 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/*  37 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  41 */     Player player = PlayerCache.getPlayer(sender.getId());
/*     */     
/*  43 */     if (ClanCache.getClan(player) == null) {
/*  44 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-clan"), 1);
/*  45 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     Clan clan = ClanCache.getClan(player);
/*     */     
/*  51 */     if (clan.getLeader() != player) {
/*  52 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-clan-leader"), 1);
/*  53 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     String setting = args[1];
/*     */     
/*  59 */     if (!Arrays.<String>asList(settings).contains(setting)) {
/*  60 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-setting"), 1);
/*  61 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*  62 */       Embed embed1 = new Embed(EmbedType.DEFAULT, "Available settings", "", 1);
/*  63 */       for (int i = 0; i < settings.length; i++) {
/*  64 */         embed1.addField(settings[i], "Value - `" + settingsvalue[i] + "`\n" + settingsdesc[i], false);
/*     */       }
/*  66 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     String value = "";
/*     */     
/*  72 */     int settingIndex = Arrays.<String>asList(settings).indexOf(setting);
/*     */     
/*     */     try {
/*  75 */       if (setting.equals("private")) {
/*  76 */         value = args[2];
/*  77 */         clan.setPrivate(Boolean.parseBoolean(args[2]));
/*     */       }
/*  79 */       else if (setting.equals("eloreq")) {
/*  80 */         value = args[2];
/*  81 */         clan.setEloJoinReq(Integer.parseInt(args[2]));
/*     */       }
/*  83 */       else if (setting.equals("description")) {
/*  84 */         if (msg.getContentRaw().replace(args[1], "").replace(args[0], "").trim().length() > Integer.parseInt(Config.getValue("clan-desc-max"))) {
/*  85 */           Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("desc-too-long"), 1);
/*  86 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/*  89 */         clan.setDescription(msg.getContentRaw().replace(args[1], "").replace(args[0], "").trim());
/*  90 */         value = msg.getContentRaw().replace(args[1], "").replace(args[0], "").trim();
/*     */       }
/*  92 */       else if (setting.equals("icon")) {
/*  93 */         if (clan.getLevel().getLevel() < Integer.parseInt(Config.getValue("allow-setting-icon"))) {
/*  94 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "Your clan needs to reach level " + Config.getValue("allow-setting-icon") + " for you to be able to set the icon", 1);
/*  95 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/*  98 */         if (!((Message.Attachment)msg.getAttachments().get(0)).isImage()) {
/*  99 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "The attached file has to be an image", 1);
/* 100 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 103 */         if (msg.getAttachments().size() < 1) {
/* 104 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "You have to attach a `135x135` image as your theme", 1);
/* 105 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 108 */         if (!((Message.Attachment)msg.getAttachments().get(0)).getFileName().equals("icon.png")) {
/* 109 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "The image file has to be named `icon.png`", 1);
/* 110 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 113 */         if (((Message.Attachment)msg.getAttachments().get(0)).getWidth() != 135 || ((Message.Attachment)msg.getAttachments().get(0)).getHeight() != 135) {
/* 114 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "You have to attach a `135x135` image as your theme", 1);
/* 115 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           
/*     */           return;
/*     */         } 
/* 119 */         ((Message.Attachment)msg.getAttachments().get(0)).downloadToFile("RankedBot/clans/" + clan.getName() + "/" + ((Message.Attachment)msg.getAttachments().get(0)).getFileName());
/*     */         
/* 121 */         value = "icon.png";
/*     */       }
/* 123 */       else if (setting.equals("theme")) {
/* 124 */         if (clan.getLevel().getLevel() < Integer.parseInt(Config.getValue("allow-setting-theme"))) {
/* 125 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "Your clan needs to reach level " + Config.getValue("allow-setting-theme") + " for you to be able to set the icon", 1);
/* 126 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 129 */         if (!((Message.Attachment)msg.getAttachments().get(0)).isImage()) {
/* 130 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "The attached file has to be an image", 1);
/* 131 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 134 */         if (msg.getAttachments().size() < 1) {
/* 135 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "You have to attach a `960x540` image as your theme", 1);
/* 136 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 139 */         if (!((Message.Attachment)msg.getAttachments().get(0)).getFileName().equals("theme.png")) {
/* 140 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "The image file has to be named `theme.png`", 1);
/* 141 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           return;
/*     */         } 
/* 144 */         if (((Message.Attachment)msg.getAttachments().get(0)).getWidth() != 960 || ((Message.Attachment)msg.getAttachments().get(0)).getHeight() != 540) {
/* 145 */           Embed reply = new Embed(EmbedType.ERROR, "Error", "You have to attach a `960x540` image as your theme", 1);
/* 146 */           msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */           
/*     */           return;
/*     */         } 
/* 150 */         ((Message.Attachment)msg.getAttachments().get(0)).downloadToFile("RankedBot/clans/" + clan.getName() + "/" + ((Message.Attachment)msg.getAttachments().get(0)).getFileName());
/*     */         
/* 152 */         value = "theme.png";
/*     */       } 
/* 154 */     } catch (Exception e) {
/* 155 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", "Something went wrong... please make sure that youre setting `" + settingsvalue[settingIndex] + "` as your value", 1);
/* 156 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/* 160 */     Embed embed = new Embed(EmbedType.SUCCESS, "Successfully updated the settings", "Successfully set `" + value + "` as your clan's `" + setting + "`", 1);
/* 161 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanSettingsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */