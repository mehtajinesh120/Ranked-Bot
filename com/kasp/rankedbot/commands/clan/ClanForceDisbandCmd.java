/*    */ package com.kasp.rankedbot.commands.clan;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Clan;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ClanForceDisbandCmd extends Command {
/*    */   public ClanForceDisbandCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 2) {
/* 23 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     if (!ClanCache.containsClan(args[1])) {
/* 29 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-doesnt-exist"), 1);
/* 30 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     Clan clan = ClanCache.getClan(args[1]);
/*    */     
/* 36 */     clan.disband();
/*    */     
/* 38 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("clan-disbanded"), 1);
/* 39 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanForceDisbandCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */