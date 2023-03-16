/*    */ package com.kasp.rankedbot.commands.moderation;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class Ban
/*    */   extends Command {
/*    */   public Ban(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     LocalDateTime unban;
/* 26 */     if (args.length < 4) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     String ID = args[1].replaceAll("[^0-9]", "");
/* 33 */     String duration = args[2];
/* 34 */     String reason = msg.getContentRaw().replaceAll(args[0], "").replaceAll(args[1], "").replaceAll(args[2], "").trim();
/*    */     
/* 36 */     Player player = PlayerCache.getPlayer(ID);
/*    */ 
/*    */ 
/*    */     
/* 40 */     if (duration.contains("m")) {
/* 41 */       unban = LocalDateTime.now().plusMinutes(Integer.parseInt(duration.replaceAll("[^0-9]", "")));
/* 42 */     } else if (duration.contains("h")) {
/* 43 */       unban = LocalDateTime.now().plusHours(Integer.parseInt(duration.replaceAll("[^0-9]", "")));
/* 44 */     } else if (duration.contains("d")) {
/* 45 */       unban = LocalDateTime.now().plusDays(Integer.parseInt(duration.replaceAll("[^0-9]", "")));
/*    */     } else {
/* 47 */       Embed error = new Embed(EmbedType.ERROR, "", Msg.getMsg("incorrect-time-format"), 1);
/* 48 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (player.ban(unban, reason)) {
/* 53 */       Embed embed = new Embed(EmbedType.DEFAULT, "`" + player.getIgn() + " has been banned`", "Please do `=fix` if you're still not unbanned after the time is over\nIf you think this is a false ban feel free to appeal / contact staff", 1);
/* 54 */       embed.addField("Ban duration", duration, false);
/* 55 */       embed.addField("Reason", reason, false);
/*    */       
/* 57 */       if (!Objects.equals(Config.getValue("ban-channel"), null)) {
/* 58 */         guild.getTextChannelById(Config.getValue("ban-channel")).sendMessageEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       }
/*    */       
/* 61 */       Embed success = new Embed(EmbedType.SUCCESS, "", "You have banned <@!" + ID + "> for `" + duration + "`\n**Reason:** " + reason, 1);
/* 62 */       msg.replyEmbeds(success.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } else {
/* 64 */       Embed error = new Embed(EmbedType.ERROR, "", "This player is already banned\nReason: `" + player.getBanReason() + "`", 1);
/* 65 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\moderation\Ban.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */