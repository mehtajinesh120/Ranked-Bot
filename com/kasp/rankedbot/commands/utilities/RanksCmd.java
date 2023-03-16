/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Rank;
/*    */ import com.kasp.rankedbot.instance.cache.RankCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class RanksCmd extends Command {
/*    */   public RanksCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     Embed embed;
/* 23 */     if (args.length != 1) {
/* 24 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 25 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     List<String> ranks = new ArrayList<>();
/*    */     
/* 31 */     for (Rank r : RankCache.getRanks().values()) {
/* 32 */       ranks.add("<@&" + r.getID() + "> {" + r.getStartingElo() + "} - " + r.getEndingElo() + "** `+" + r.getWinElo() + " / -" + r.getLoseElo() + "` MVP: " + r.getMvpElo());
/*    */     }
/*    */     
/* 35 */     ranks.sort(new Comparator<String>() {
/*    */           public int compare(String o1, String o2) {
/* 37 */             return extractInt(o2) - extractInt(o1);
/*    */           }
/*    */           
/*    */           int extractInt(String s) {
/* 41 */             String num = s.substring(s.indexOf("{") + 1, s.indexOf("}"));
/* 42 */             return num.isEmpty() ? 0 : Integer.parseInt(num);
/*    */           }
/*    */         });
/*    */     
/* 46 */     String display = "";
/*    */ 
/*    */     
/* 49 */     if (ranks.size() == 0) {
/* 50 */       embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("no-ranks"), 1);
/*    */     } else {
/*    */       
/* 53 */       for (String role : ranks) {
/* 54 */         display = display + display + "\n";
/*    */       }
/* 56 */       embed = new Embed(EmbedType.DEFAULT, "All Ranks", display, 1);
/*    */     } 
/*    */     
/* 59 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\RanksCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */