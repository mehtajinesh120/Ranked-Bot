/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.database.SQLUtilsManager;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Rank;
/*    */ import com.kasp.rankedbot.instance.cache.RankCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ 
/*    */ public class AddRankCmd extends Command {
/*    */   public AddRankCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 15 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 20 */     if (args.length != 7) {
/* 21 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 22 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 26 */     Role role = null;
/* 27 */     String ID = args[1].replaceAll("[^0-9]", ""); 
/* 28 */     try { role = guild.getRoleById(ID); } catch (Exception exception) {}
/*    */     
/* 30 */     if (role == null) {
/* 31 */       Embed error = new Embed(EmbedType.ERROR, "", Msg.getMsg("invalid-role"), 1);
/* 32 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     if (RankCache.containsRank(ID)) {
/* 37 */       Embed error = new Embed(EmbedType.ERROR, "", Msg.getMsg("rank-already-exists"), 1);
/* 38 */       msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     String startingElo = args[2];
/* 43 */     String endingElo = args[3];
/* 44 */     String winElo = args[4];
/* 45 */     String loseElo = args[5];
/* 46 */     String mvpElo = args[6];
/*    */     
/* 48 */     SQLUtilsManager.createRank(ID, startingElo, endingElo, winElo, loseElo, mvpElo);
/* 49 */     new Rank(ID);
/*    */     
/* 51 */     Embed success = new Embed(EmbedType.SUCCESS, "✅ successfully added `" + role.getName() + "` rank", "", 1);
/* 52 */     success.addField("Role:", role.getAsMention(), true);
/* 53 */     success.addField("Starting elo:", startingElo, true);
/* 54 */     success.addField("Ending elo:", endingElo, true);
/* 55 */     success.addField("Win elo:", "+" + winElo, true);
/* 56 */     success.addField("Lose elo:", "-" + loseElo, true);
/* 57 */     success.addField("Mvp Elo:", mvpElo, true);
/*    */     
/* 59 */     msg.replyEmbeds(success.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\AddRankCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */