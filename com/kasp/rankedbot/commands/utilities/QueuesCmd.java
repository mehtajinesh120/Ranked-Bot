/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.Queue;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class QueuesCmd
/*    */   extends Command {
/*    */   public QueuesCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 1) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     if (QueueCache.getQueues().size() < 1) {
/* 31 */       Embed reply = new Embed(EmbedType.ERROR, "Error", "There are currently no queues set up. Add one using `=addqueue`!", 1);
/* 32 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     Embed embed = new Embed(EmbedType.DEFAULT, "All server queues", "this also shows **currently** queueing players", 1);
/*    */     
/* 38 */     for (Queue q : QueueCache.getQueues().values()) {
/*    */       
/* 40 */       String content = "There are currently no players playing";
/* 41 */       if (q.getPlayers().size() > 0) {
/* 42 */         content = "";
/*    */         
/* 44 */         for (Player p : q.getPlayers()) {
/* 45 */           content = content + "<@" + content + ">\n";
/*    */         }
/*    */       } 
/*    */       
/* 49 */       embed.addField(guild.getVoiceChannelById(q.getID()).getName() + " - `" + guild.getVoiceChannelById(q.getID()).getName() + "/" + q.getPlayers().size() + "`", content, false);
/*    */     } 
/*    */ 
/*    */     
/* 53 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\QueuesCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */