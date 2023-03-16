/*    */ package com.kasp.rankedbot.commands.moderation;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.temporal.ChronoUnit;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class BanInfo
/*    */   extends Command {
/*    */   public BanInfo(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 26 */     if (args.length != 2) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 34 */     Player player = PlayerCache.getPlayer(ID);
/*    */     
/* 36 */     if (!player.isBanned()) {
/* 37 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-not-banned"), 1);
/* 38 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 44 */     long diffDays = ChronoUnit.DAYS.between(LocalDateTime.now(), player.getBannedTill());
/* 45 */     long diffHours = ChronoUnit.HOURS.between(LocalDateTime.now(), player.getBannedTill());
/* 46 */     long diffMins = ChronoUnit.MINUTES.between(LocalDateTime.now(), player.getBannedTill());
/*    */     
/* 48 */     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
/* 49 */     String desc = "Unbanned On `" + formatter.format(player.getBannedTill()) + " GMT`\nTime till unbanned `≈ " + diffDays + "d / " + diffHours + "h / " + diffMins + "m`\n**Reason: **" + player.getBanReason();
/*    */     
/* 51 */     Embed embed = new Embed(EmbedType.DEFAULT, player.getIgn() + "'s Ban Info", desc, 1);
/* 52 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\moderation\BanInfo.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */