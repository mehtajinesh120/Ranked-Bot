/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.GameMap;
/*    */ import com.kasp.rankedbot.instance.cache.MapCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class DeleteMapCmd extends Command {
/*    */   public DeleteMapCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 2) {
/* 23 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String name = args[1];
/*    */     
/* 30 */     if (!MapCache.containsMap(name)) {
/* 31 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("map-doesnt-exist"), 1);
/* 32 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     GameMap.delete(name);
/*    */     
/* 38 */     Embed success = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("map-deleted"), 1);
/* 39 */     msg.replyEmbeds(success.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\DeleteMapCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */