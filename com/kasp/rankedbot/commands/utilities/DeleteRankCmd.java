/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.cache.RankCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class DeleteRankCmd extends Command {
/*    */   public DeleteRankCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 14 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 19 */     if (args.length != 2) {
/* 20 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 21 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 25 */     Role role = null;
/* 26 */     String ID = args[1].replaceAll("[^0-9]", ""); 
/* 27 */     try { role = guild.getRoleById(ID); } catch (Exception exception) {}
/*    */     
/* 29 */     if (role == null) {
/* 30 */       Embed error = new Embed(EmbedType.ERROR, "", Msg.getMsg("invalid-role"), 1);
/* 31 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     if (!RankCache.containsRank(ID)) {
/* 36 */       Embed error = new Embed(EmbedType.ERROR, "", Msg.getMsg("rank-doesnt-exist"), 1);
/* 37 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     Rank.delete(ID);
/*    */     
/* 43 */     Embed success = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("rank-deleted"), 1);
/* 44 */     msg.replyEmbeds(success.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\DeleteRankCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */