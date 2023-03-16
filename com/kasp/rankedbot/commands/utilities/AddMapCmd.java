/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.database.SQLUtilsManager;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.GameMap;
/*    */ import com.kasp.rankedbot.instance.cache.MapCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class AddMapCmd extends Command {
/*    */   public AddMapCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 23 */     if (args.length != 5) {
/* 24 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 25 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     String name = args[1];
/* 30 */     String height = args[2];
/* 31 */     String team1 = args[3];
/* 32 */     String team2 = args[4];
/*    */     
/* 34 */     if (MapCache.containsMap(name)) {
/* 35 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("map-already-exists"), 1);
/* 36 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     SQLUtilsManager.createMap(name, height, team1, team2);
/* 41 */     new GameMap(name);
/*    */     
/* 43 */     Embed embed = new Embed(EmbedType.SUCCESS, "✅ successfully added `" + name + "` map", "", 1);
/* 44 */     embed.addField("Height Limit:", height, true);
/* 45 */     embed.addField("Teams:", "`" + team1 + "` vs `" + team2 + "`", true);
/*    */     
/* 47 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\AddMapCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */