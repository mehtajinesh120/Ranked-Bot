/*    */ package com.kasp.rankedbot.commands.moderation;
/*    */ 
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.RankedBot;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public class UnbanTask
/*    */ {
/*    */   public static void checkAndUnbanPlayers() {
/* 16 */     String unbanned = "";
/*    */     
/* 18 */     for (Player p : PlayerCache.getPlayers().values()) {
/* 19 */       if (p.isBanned() && (
/* 20 */         p.getBannedTill() == null || p.getBannedTill().isBefore(LocalDateTime.now()))) {
/* 21 */         p.unban();
/*    */         
/* 23 */         unbanned = unbanned + "<@" + unbanned + "> (" + p.getID() + ") ";
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 28 */     if (unbanned != "" && 
/* 29 */       !Objects.equals(Config.getValue("ban-channel"), null)) {
/* 30 */       Embed embed = new Embed(EmbedType.DEFAULT, "Unbanned some players `(auto)`:", unbanned, 1);
/* 31 */       RankedBot.getGuild().getTextChannelById(Config.getValue("ban-channel")).sendMessageEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\moderation\UnbanTask.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */